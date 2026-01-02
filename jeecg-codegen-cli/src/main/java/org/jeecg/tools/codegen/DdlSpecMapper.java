package org.jeecg.tools.codegen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class DdlSpecMapper {
    private DdlSpecMapper() {}

    static final class Options {
        String jspMode = "one";
        String projectPath = ".";
        String bussiPackage;
        String entityPackage;
        Integer fieldRowNum;
        Map<String, String> queryFields;
        boolean oneToMany;
        String mainTable;
        List<String> subTables;
        String treePidField;
        String treeTextField;
        String treeHasChildrenField;
    }

    static CodegenSpec fromDdl(String ddl, Options options) {
        if (ddl == null || ddl.trim().isEmpty()) {
            throw new IllegalArgumentException("ddl is required");
        }
        Options opts = options != null ? options : new Options();
        if (opts.oneToMany && !isOneToManyMode(opts.jspMode)) {
            throw new IllegalArgumentException("one-to-many requires jspMode in many/jvxe/erp/innerTable/tab");
        }
        if (!opts.oneToMany && isOneToManyMode(opts.jspMode)) {
            throw new IllegalArgumentException("jspMode " + opts.jspMode + " requires --one-to-many");
        }

        Map<String, ParsedTable> tables = parseTables(ddl);
        ParsedTable mainTable = resolveMainTable(tables, opts);
        List<CodegenSpec.ColumnSpec> columns = mainTable.columns;
        applyQuerySettings(columns, opts);

        CodegenSpec spec = new CodegenSpec();
        spec.setJspMode(opts.jspMode);
        spec.setProjectPath(opts.projectPath);
        spec.setBussiPackage(opts.bussiPackage);

        CodegenSpec.TableSpec table = new CodegenSpec.TableSpec();
        table.setTableName(mainTable.tableName);
        table.setEntityName(toPascal(mainTable.tableName));
        table.setEntityPackage(resolveEntityPackage(opts.entityPackage, mainTable.tableName));
        table.setFtlDescription(mainTable.tableComment != null ? mainTable.tableComment : mainTable.tableName);
        if (opts.fieldRowNum != null) {
            table.setFieldRowNum(opts.fieldRowNum);
        } else {
            table.setFieldRowNum(inferFieldRowNum(columns));
        }
        if ("tree".equalsIgnoreCase(opts.jspMode)) {
            applyTreeParams(table, columns, opts);
        }
        spec.setTable(table);
        spec.setColumns(columns);
        if (opts.oneToMany) {
            spec.setSubTables(buildSubTables(tables, mainTable, table, opts));
        }
        return spec;
    }

    private static Map<String, ParsedTable> parseTables(String ddl) {
        List<String> statements = splitCreateTableStatements(ddl);
        if (statements.isEmpty()) {
            throw new IllegalArgumentException("cannot find CREATE TABLE statements in DDL");
        }
        Map<String, ParsedTable> tables = new HashMap<>();
        for (String stmt : statements) {
            ParsedTable table = parseTableStatement(stmt);
            String key = normalizeTableKey(table.tableName);
            if (tables.containsKey(key)) {
                throw new IllegalArgumentException("duplicate table definition: " + table.tableName);
            }
            tables.put(key, table);
        }
        return tables;
    }

    private static ParsedTable parseTableStatement(String ddl) {
        String tableName = parseTableName(ddl);
        String tableComment = parseTableComment(ddl);
        String columnsBlock = extractColumnsBlock(ddl);
        List<String> segments = splitColumns(columnsBlock);

        Set<String> primaryKeys = new HashSet<>();
        List<CodegenSpec.ColumnSpec> columns = new ArrayList<>();
        for (String segment : segments) {
            String stmt = segment.trim();
            if (stmt.isEmpty()) {
                continue;
            }
            String lower = stmt.toLowerCase(Locale.ROOT);
            if (lower.startsWith("primary key")) {
                primaryKeys.addAll(parsePrimaryKeys(stmt));
                continue;
            }
            if (lower.startsWith("unique") || lower.startsWith("key") || lower.startsWith("index")) {
                continue;
            }
            CodegenSpec.ColumnSpec col = parseColumn(stmt, tableName);
            if (col != null) {
                columns.add(col);
            }
        }

        for (CodegenSpec.ColumnSpec col : columns) {
            if (primaryKeys.contains(col.getFieldDbName())) {
                col.setIsKey("Y");
            }
        }

        columns = reorderColumns(columns);
        resetFieldOrder(columns);

        ParsedTable table = new ParsedTable();
        table.tableName = tableName;
        table.tableComment = tableComment;
        table.columns = columns;
        table.primaryKeys = primaryKeys;
        return table;
    }

    private static ParsedTable resolveMainTable(Map<String, ParsedTable> tables, Options opts) {
        if (tables.size() == 1) {
            ParsedTable only = tables.values().iterator().next();
            if (opts.mainTable != null && !opts.mainTable.trim().isEmpty()) {
                String key = normalizeTableKey(opts.mainTable);
                ParsedTable selected = tables.get(key);
                if (selected == null) {
                    throw new IllegalArgumentException("main table not found: " + opts.mainTable);
                }
                return selected;
            }
            return only;
        }
        if (opts.mainTable == null || opts.mainTable.trim().isEmpty()) {
            throw new IllegalArgumentException("mainTable is required when DDL contains multiple CREATE TABLE statements");
        }
        String key = normalizeTableKey(opts.mainTable);
        ParsedTable main = tables.get(key);
        if (main == null) {
            throw new IllegalArgumentException("main table not found: " + opts.mainTable);
        }
        return main;
    }

    private static List<CodegenSpec.SubTableSpec> buildSubTables(Map<String, ParsedTable> tables,
                                                                 ParsedTable mainTable,
                                                                 CodegenSpec.TableSpec mainSpec,
                                                                 Options opts) {
        if (opts.subTables == null || opts.subTables.isEmpty()) {
            throw new IllegalArgumentException("subTables is required when one-to-many is enabled");
        }
        if (opts.mainTable == null || opts.mainTable.trim().isEmpty()) {
            throw new IllegalArgumentException("mainTable is required when one-to-many is enabled");
        }
        String mainKey = normalizeTableKey(mainTable.tableName);
        String mainPk = resolvePrimaryKey(mainTable);
        CodegenSpec.ColumnSpec mainPkCol = findColumnByName(mainTable.columns, mainPk);
        if (mainPkCol == null) {
            throw new IllegalArgumentException("main table primary key column not found: " + mainPk);
        }
        List<CodegenSpec.SubTableSpec> subTables = new ArrayList<>();
        for (String raw : opts.subTables) {
            String name = raw != null ? raw.trim() : null;
            if (name == null || name.isEmpty()) {
                continue;
            }
            String key = normalizeTableKey(name);
            if (key.equals(mainKey)) {
                throw new IllegalArgumentException("subTables cannot include main table: " + name);
            }
            ParsedTable sub = tables.get(key);
            if (sub == null) {
                throw new IllegalArgumentException("sub table not found: " + name);
            }
            String fk = resolveForeignKey(sub, mainTable, mainPkCol);
            CodegenSpec.SubTableSpec subSpec = new CodegenSpec.SubTableSpec();
            subSpec.setTableName(sub.tableName);
            subSpec.setEntityName(toPascal(sub.tableName));
            subSpec.setFtlDescription(sub.tableComment != null ? sub.tableComment : sub.tableName);
            subSpec.setForeignRelationType("0");
            List<String> foreignKeys = new ArrayList<>();
            foreignKeys.add(fk);
            subSpec.setForeignKeys(foreignKeys);
            List<String> foreignMainKeys = new ArrayList<>();
            foreignMainKeys.add(mainPk);
            subSpec.setForeignMainKeys(foreignMainKeys);
            subSpec.setColumns(sub.columns);
            subTables.add(subSpec);
        }
        return subTables;
    }

    private static String resolvePrimaryKey(ParsedTable table) {
        List<String> keys = new ArrayList<>();
        for (CodegenSpec.ColumnSpec col : table.columns) {
            if ("Y".equals(col.getIsKey())) {
                keys.add(col.getFieldDbName());
            }
        }
        if (keys.isEmpty()) {
            throw new IllegalArgumentException("main table primary key is required for one-to-many");
        }
        if (keys.size() > 1) {
            throw new IllegalArgumentException("composite primary key is not supported for one-to-many: " + table.tableName);
        }
        return keys.get(0);
    }

    private static String resolveForeignKey(ParsedTable subTable, ParsedTable mainTable, CodegenSpec.ColumnSpec mainPkCol) {
        List<CodegenSpec.ColumnSpec> candidates = new ArrayList<>();
        String mainTableName = mainTable.tableName.toLowerCase(Locale.ROOT);
        String mainBase = resolveTableBase(mainTable.tableName);
        String mainBaseLower = mainBase.toLowerCase(Locale.ROOT);
        String mainSingular = singularize(mainBaseLower);
        String mainEntity = toCamel(mainBase).toLowerCase(Locale.ROOT);
        Set<String> nameMatches = new HashSet<>();
        nameMatches.add(mainTableName + "_id");
        if (!mainBaseLower.isEmpty()) {
            nameMatches.add(mainBaseLower + "_id");
        }
        if (!mainSingular.isEmpty()) {
            nameMatches.add(mainSingular + "_id");
        }

        for (CodegenSpec.ColumnSpec col : subTable.columns) {
            String field = col.getFieldDbName() != null ? col.getFieldDbName().toLowerCase(Locale.ROOT) : "";
            if (!field.endsWith("_id")) {
                continue;
            }
            boolean matched = nameMatches.contains(field);
            if (!matched && col.getFiledComment() != null) {
                String comment = col.getFiledComment().toLowerCase(Locale.ROOT);
                if (comment.contains(mainTableName) || comment.contains(mainBaseLower) || comment.contains(mainEntity)) {
                    matched = true;
                }
            }
            if (matched) {
                candidates.add(col);
            }
        }
        if (candidates.isEmpty()) {
            throw new IllegalArgumentException("cannot resolve foreign key for sub table: " + subTable.tableName);
        }
        if (candidates.size() > 1) {
            throw new IllegalArgumentException("ambiguous foreign key for sub table: " + subTable.tableName);
        }
        CodegenSpec.ColumnSpec fk = candidates.get(0);
        validateColumnCompatibility(mainPkCol, fk, subTable.tableName);
        return fk.getFieldDbName();
    }

    private static void validateColumnCompatibility(CodegenSpec.ColumnSpec mainPk, CodegenSpec.ColumnSpec fk, String subTableName) {
        if (mainPk.getFieldDbType() != null && fk.getFieldDbType() != null
            && !mainPk.getFieldDbType().equalsIgnoreCase(fk.getFieldDbType())) {
            throw new IllegalArgumentException("foreign key type mismatch for sub table: " + subTableName);
        }
        if (mainPk.getCharmaxLength() != null && fk.getCharmaxLength() != null
            && !mainPk.getCharmaxLength().equals(fk.getCharmaxLength())) {
            throw new IllegalArgumentException("foreign key length mismatch for sub table: " + subTableName);
        }
        if (mainPk.getPrecision() != null && fk.getPrecision() != null
            && !mainPk.getPrecision().equals(fk.getPrecision())) {
            throw new IllegalArgumentException("foreign key precision mismatch for sub table: " + subTableName);
        }
        if (mainPk.getScale() != null && fk.getScale() != null
            && !mainPk.getScale().equals(fk.getScale())) {
            throw new IllegalArgumentException("foreign key scale mismatch for sub table: " + subTableName);
        }
    }

    private static String resolveTableBase(String tableName) {
        if (tableName == null || tableName.isEmpty()) {
            return "";
        }
        int idx = tableName.indexOf('_');
        if (idx > 0 && idx + 1 < tableName.length()) {
            return tableName.substring(idx + 1);
        }
        return tableName;
    }

    private static String singularize(String name) {
        if (name == null || name.isEmpty()) {
            return "";
        }
        if (name.endsWith("s") && name.length() > 1) {
            return name.substring(0, name.length() - 1);
        }
        return name;
    }

    private static CodegenSpec.ColumnSpec findColumnByName(List<CodegenSpec.ColumnSpec> columns, String name) {
        if (columns == null || name == null) {
            return null;
        }
        for (CodegenSpec.ColumnSpec col : columns) {
            if (col.getFieldDbName() != null && col.getFieldDbName().equalsIgnoreCase(name)) {
                return col;
            }
        }
        return null;
    }

    private static void applyTreeParams(CodegenSpec.TableSpec table,
                                        List<CodegenSpec.ColumnSpec> columns,
                                        Options opts) {
        String pidField = resolveTreeField(columns, opts.treePidField, "parent_id");
        String textField = resolveTreeField(columns, opts.treeTextField, "name", "title");
        String hasChildren = resolveTreeField(columns, opts.treeHasChildrenField, "has_child", "is_leaf");
        if (pidField == null || textField == null) {
            throw new IllegalArgumentException("tree mode requires parent_id and name/title fields");
        }
        if (table.getExtendParams() == null) {
            table.setExtendParams(new HashMap<>());
        }
        table.getExtendParams().put("pidField", pidField);
        table.getExtendParams().put("textField", textField);
        if (hasChildren != null) {
            table.getExtendParams().put("hasChildren", hasChildren);
        }
    }

    private static String resolveTreeField(List<CodegenSpec.ColumnSpec> columns, String override, String... defaults) {
        if (override != null && !override.trim().isEmpty()) {
            CodegenSpec.ColumnSpec col = findColumnByName(columns, override.trim());
            if (col == null) {
                throw new IllegalArgumentException("tree field not found: " + override);
            }
            return col.getFieldDbName();
        }
        for (String candidate : defaults) {
            CodegenSpec.ColumnSpec col = findColumnByName(columns, candidate);
            if (col != null) {
                return col.getFieldDbName();
            }
        }
        return null;
    }

    private static String normalizeTableKey(String name) {
        return name == null ? "" : name.trim().toLowerCase(Locale.ROOT);
    }

    private static List<String> splitCreateTableStatements(String ddl) {
        List<String> statements = new ArrayList<>();
        Pattern pattern = Pattern.compile("(?i)\\bcreate\\s+table\\b");
        Matcher matcher = pattern.matcher(ddl);
        List<Integer> starts = new ArrayList<>();
        while (matcher.find()) {
            starts.add(matcher.start());
        }
        for (int i = 0; i < starts.size(); i++) {
            int start = starts.get(i);
            int end = i + 1 < starts.size() ? starts.get(i + 1) : ddl.length();
            String stmt = ddl.substring(start, end).trim();
            if (!stmt.isEmpty()) {
                statements.add(stmt);
            }
        }
        return statements;
    }

    private static boolean isOneToManyMode(String jspMode) {
        if (jspMode == null) {
            return false;
        }
        String mode = jspMode.trim();
        return "many".equalsIgnoreCase(mode)
            || "jvxe".equalsIgnoreCase(mode)
            || "erp".equalsIgnoreCase(mode)
            || "innerTable".equalsIgnoreCase(mode)
            || "tab".equalsIgnoreCase(mode);
    }

    private static String parseTableName(String ddl) {
        Pattern pattern = Pattern.compile("(?i)create\\s+table\\s+(if\\s+not\\s+exists\\s+)?(?:(`?)([\\w]+)\\2\\.)?(`?)([\\w]+)\\4");
        Matcher matcher = pattern.matcher(ddl);
        if (!matcher.find()) {
            throw new IllegalArgumentException("cannot find table name in DDL");
        }
        return matcher.group(5);
    }

    private static String parseTableComment(String ddl) {
        Pattern pattern = Pattern.compile("(?i)comment\\s*=\\s*'([^']*)'");
        Matcher matcher = pattern.matcher(ddl);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return null;
    }

    private static String extractColumnsBlock(String ddl) {
        int start = ddl.indexOf('(');
        if (start < 0) {
            throw new IllegalArgumentException("cannot find column block");
        }
        int depth = 0;
        boolean inQuote = false;
        for (int i = start; i < ddl.length(); i++) {
            char c = ddl.charAt(i);
            if (c == '\'' && (i == 0 || ddl.charAt(i - 1) != '\\')) {
                inQuote = !inQuote;
            }
            if (inQuote) {
                continue;
            }
            if (c == '(') {
                depth++;
            } else if (c == ')') {
                depth--;
                if (depth == 0) {
                    return ddl.substring(start + 1, i);
                }
            }
        }
        throw new IllegalArgumentException("unterminated column block");
    }

    private static List<String> splitColumns(String block) {
        List<String> parts = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        int depth = 0;
        boolean inQuote = false;
        for (int i = 0; i < block.length(); i++) {
            char c = block.charAt(i);
            if (c == '\'' && (i == 0 || block.charAt(i - 1) != '\\')) {
                inQuote = !inQuote;
            }
            if (!inQuote) {
                if (c == '(') {
                    depth++;
                } else if (c == ')') {
                    depth--;
                } else if (c == ',' && depth == 0) {
                    parts.add(current.toString());
                    current.setLength(0);
                    continue;
                }
            }
            current.append(c);
        }
        if (current.length() > 0) {
            parts.add(current.toString());
        }
        return parts;
    }

    private static Set<String> parsePrimaryKeys(String stmt) {
        Set<String> keys = new HashSet<>();
        Pattern pattern = Pattern.compile("(?i)primary\\s+key\\s*\\(([^)]+)\\)");
        Matcher matcher = pattern.matcher(stmt);
        if (!matcher.find()) {
            return keys;
        }
        String body = matcher.group(1);
        for (String part : body.split(",")) {
            String name = part.trim();
            if (name.startsWith("`") && name.endsWith("`") && name.length() > 1) {
                name = name.substring(1, name.length() - 1);
            }
            if (!name.isEmpty()) {
                keys.add(name);
            }
        }
        return keys;
    }

    private static CodegenSpec.ColumnSpec parseColumn(String stmt, String tableName) {
        String trimmed = stmt.trim();
        if (trimmed.isEmpty()) {
            return null;
        }
        String name;
        int idx;
        if (trimmed.startsWith("`")) {
            int end = trimmed.indexOf('`', 1);
            if (end < 0) {
                return null;
            }
            name = trimmed.substring(1, end);
            idx = end + 1;
        } else {
            int space = trimmed.indexOf(' ');
            if (space < 0) {
                return null;
            }
            name = trimmed.substring(0, space);
            idx = space;
        }
        String rest = trimmed.substring(idx).trim();
        String typeToken = readTypeToken(rest);
        if (typeToken == null) {
            return null;
        }

        ColumnType type = mapType(typeToken);
        boolean notNull = containsIgnoreCase(rest, "not null");
        boolean isPrimaryInline = containsIgnoreCase(rest, "primary key");
        String comment = extractComment(rest);
        String defaultValue = extractDefault(rest);

        CodegenSpec.ColumnSpec col = new CodegenSpec.ColumnSpec();
        col.setFieldDbName(name);
        col.setFieldName(toCamel(name));
        col.setFiledComment(comment != null ? comment : name);
        col.setFieldDbType(type.fieldDbType);
        col.setFieldType(type.fieldType);
        col.setCharmaxLength(type.charmaxLength);
        col.setPrecision(type.precision);
        col.setScale(type.scale);
        if (type.charmaxLength != null) {
            try {
                col.setFieldLength(Integer.parseInt(type.charmaxLength));
            } catch (NumberFormatException ignored) {
                // ignore non-numeric length
            }
        }
        col.setNullable(notNull ? "N" : "Y");
        col.setIsKey(isPrimaryInline ? "Y" : "N");
        col.setIsShow(defaultShow(name) ? "Y" : "N");
        col.setIsShowList(defaultShowList(name) ? "Y" : "N");
        col.setIsQuery("N");
        col.setReadonly("N");
        col.setSort("N");
        col.setFieldDefault(defaultValue);

        applyInference(col, tableName);
        applyDictFromComment(col, comment);
        ensureUiLabel(col);
        col.setFieldShowType(col.getClassType());
        return col;
    }

    private static String readTypeToken(String rest) {
        int depth = 0;
        for (int i = 0; i < rest.length(); i++) {
            char c = rest.charAt(i);
            if (c == '(') {
                depth++;
            } else if (c == ')') {
                depth--;
            } else if (Character.isWhitespace(c) && depth == 0) {
                return rest.substring(0, i).trim();
            }
        }
        return rest.trim();
    }

    private static boolean containsIgnoreCase(String text, String needle) {
        return text.toLowerCase(Locale.ROOT).contains(needle.toLowerCase(Locale.ROOT));
    }

    private static String extractComment(String rest) {
        Pattern pattern = Pattern.compile("(?i)comment\\s+'([^']*)'");
        Matcher matcher = pattern.matcher(rest);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return null;
    }

    private static String extractDefault(String rest) {
        Pattern pattern = Pattern.compile("(?i)\\bdefault\\b\\s+([^\\s,]+)");
        Matcher matcher = pattern.matcher(rest);
        if (matcher.find()) {
            String raw = matcher.group(1).trim();
            if (raw.startsWith("'") && raw.endsWith("'") && raw.length() >= 2) {
                return raw.substring(1, raw.length() - 1);
            }
            return raw;
        }
        return null;
    }

    private static boolean defaultShow(String name) {
        String lower = name.toLowerCase(Locale.ROOT);
        // 表单默认隐藏所有系统字段
        return !(lower.equals("id")
            || lower.equals("del_flag")
            || lower.equals("create_time")
            || lower.equals("create_by")
            || lower.equals("update_time")
            || lower.equals("update_by"));
    }

    private static boolean defaultShowList(String name) {
        String lower = name.toLowerCase(Locale.ROOT);
        // 列表允许展示 create_*，隐藏其他系统字段
        return !(lower.equals("id")
            || lower.equals("del_flag")
            || lower.equals("update_time")
            || lower.equals("update_by"));
    }

    private static void applyInference(CodegenSpec.ColumnSpec col, String tableName) {
        String name = col.getFieldDbName().toLowerCase(Locale.ROOT);
        String classType = "default";
        String validType = null;
        String dictField = null;

        if (name.endsWith("_time") || name.endsWith("_date")) {
            classType = "date";
        }
        if ("datetime".equalsIgnoreCase(col.getFieldDbType())) {
            classType = "datetime";
        } else if ("date".equalsIgnoreCase(col.getFieldDbType())) {
            classType = "date";
        } else if ("time".equalsIgnoreCase(col.getFieldDbType())) {
            classType = "time";
        }

        if (name.startsWith("is_") || name.endsWith("_flag")) {
            classType = "switch";
            dictField = "is_open";
        }

        if (name.contains("status") || name.contains("state") || name.contains("type") || name.contains("category")) {
            classType = "list";
            dictField = tableName + "_" + name;
        }

        if (name.contains("sex") || name.contains("gender")) {
            classType = "radio";
            dictField = "sex";
        }

        if (name.contains("phone") || name.contains("mobile")) {
            validType = "m";
        } else if (name.contains("email")) {
            validType = "e";
        } else if (name.contains("url") || name.contains("link")) {
            validType = "url";
        }

        if (name.contains("file") || name.contains("attach")) {
            classType = "file";
        } else if (name.contains("img") || name.contains("image") || name.contains("pic") || name.contains("photo")) {
            classType = "image";
        } else if (name.contains("content") || name.contains("desc")) {
            classType = "textarea";
        } else if (name.contains("remark") || name.contains("note")) {
            classType = "textarea";
        }

        if (name.equals("create_time") || name.equals("update_time")
            || name.equals("create_by") || name.equals("update_by")) {
            col.setIsShow("N");
            col.setIsShowList("N");
            col.setIsQuery("N");
        }

        col.setClassType(classType);
        if (validType != null) {
            col.setFieldValidType(validType);
        }
        if (dictField != null) {
            col.setDictField(dictField);
        }
    }

    private static void applyDictFromComment(CodegenSpec.ColumnSpec col, String comment) {
        if (comment == null || comment.isEmpty()) {
            return;
        }
        String dict = extractDictCode(comment);
        if (dict == null || dict.isEmpty()) {
            return;
        }
        col.setDictField(dict);
        if (!CLASS_TYPE_NEEDS_DICT.contains(col.getClassType())) {
            // 使字典类字段始终走字典控件分支，符合模板要求
            col.setClassType("list");
        }
    }

    private static void ensureUiLabel(CodegenSpec.ColumnSpec col) {
        if (col == null) {
            return;
        }
        Map<String, Object> extendParams = col.getExtendParams();
        if (extendParams == null) {
            extendParams = new HashMap<>();
            col.setExtendParams(extendParams);
        }
        Object existing = extendParams.get("uiLabel");
        if (existing != null && !existing.toString().trim().isEmpty()) {
            return;
        }
        String label = cleanUiLabel(col.getFiledComment());
        extendParams.put("uiLabel", label);
    }

    private static String cleanUiLabel(String comment) {
        if (comment == null) {
            return null;
        }
        String value = comment.trim();
        if (value.isEmpty()) {
            return comment;
        }
        value = value.replaceAll("。?\\s*字典[:：].*$", "").trim();
        value = value.replaceAll("\\([^)]*\\)", "");
        value = value.replaceAll("（[^）]*）", "");
        value = value.trim();
        int colon = value.indexOf('：');
        int asciiColon = value.indexOf(':');
        int cut = -1;
        if (colon >= 0 && asciiColon >= 0) {
            cut = Math.min(colon, asciiColon);
        } else if (colon >= 0) {
            cut = colon;
        } else if (asciiColon >= 0) {
            cut = asciiColon;
        }
        if (cut > 0) {
            value = value.substring(0, cut).trim();
        }
        value = value.replaceAll("(?i)\\s*id$", "");
        value = value.replaceAll("^[\\s，；。:：]+|[\\s，；。:：]+$", "").trim();
        if (value.isEmpty()) {
            return comment;
        }
        return value;
    }

    private static void applyQuerySettings(List<CodegenSpec.ColumnSpec> columns, Options opts) {
        if (columns == null || columns.isEmpty()) {
            return;
        }
        Map<String, String> explicit = opts != null ? opts.queryFields : null;
        boolean hasExplicit = explicit != null && !explicit.isEmpty();
        Set<String> remaining = hasExplicit ? new HashSet<>(explicit.keySet()) : null;
        for (CodegenSpec.ColumnSpec col : columns) {
            String matched = null;
            String mode = null;
            if (hasExplicit) {
                String byDb = explicit.get(col.getFieldDbName());
                if (byDb != null) {
                    matched = col.getFieldDbName();
                    mode = byDb;
                } else if (col.getFieldName() != null) {
                    String byName = explicit.get(col.getFieldName());
                    if (byName != null) {
                        matched = col.getFieldName();
                        mode = byName;
                    }
                }
            }
            if (matched != null) {
                if (remaining != null) {
                    remaining.remove(matched);
                }
                setQuery(col, mode != null && !mode.trim().isEmpty() ? mode.trim() : "single");
                continue;
            }
            if (!hasExplicit && shouldInferQuery(col)) {
                setQuery(col, inferQueryMode(col));
            }
        }
        if (!hasExplicit) {
            moveCreateTimeToQueryEnd(columns);
            resetFieldOrder(columns);
        }
        if (remaining != null && !remaining.isEmpty()) {
            throw new IllegalArgumentException("unknown query fields: " + String.join(", ", remaining));
        }
    }

    private static boolean shouldInferQuery(CodegenSpec.ColumnSpec col) {
        String name = col.getFieldDbName() != null ? col.getFieldDbName().toLowerCase(Locale.ROOT) : "";
        if (name.isEmpty()) {
            return false;
        }
        if (name.equals("id") || name.equals("del_flag") || name.equals("create_by")
            || name.equals("update_by") || name.equals("update_time")) {
            return false;
        }
        if (name.endsWith("_time") || name.endsWith("_date")) {
            return true;
        }
        return name.contains("name")
            || name.contains("title")
            || name.contains("code")
            || name.contains("no")
            || name.contains("status")
            || name.contains("state")
            || name.contains("type")
            || name.contains("category")
            || name.contains("phone")
            || name.contains("mobile")
            || name.contains("email");
    }

    private static String inferQueryMode(CodegenSpec.ColumnSpec col) {
        String dbType = col.getFieldDbType() != null ? col.getFieldDbType().toLowerCase(Locale.ROOT) : "";
        if ("datetime".equals(dbType) || "date".equals(dbType) || "time".equals(dbType)) {
            return "group";
        }
        String name = col.getFieldDbName() != null ? col.getFieldDbName().toLowerCase(Locale.ROOT) : "";
        if (name.endsWith("_time") || name.endsWith("_date")) {
            return "group";
        }
        return "single";
    }

    private static void setQuery(CodegenSpec.ColumnSpec col, String mode) {
        if (!"single".equals(mode) && !"group".equals(mode)) {
            throw new IllegalArgumentException("column.queryMode must be single or group for " + col.getFieldDbName());
        }
        col.setIsQuery("Y");
        col.setQueryMode(mode);
    }

    private static void moveCreateTimeToQueryEnd(List<CodegenSpec.ColumnSpec> columns) {
        int createIndex = -1;
        CodegenSpec.ColumnSpec createCol = null;
        for (int i = 0; i < columns.size(); i++) {
            CodegenSpec.ColumnSpec col = columns.get(i);
            if (isCreateTime(col)) {
                createIndex = i;
                createCol = col;
                break;
            }
        }
        if (createCol == null || !"Y".equals(createCol.getIsQuery())) {
            return;
        }
        int lastQueryIndex = -1;
        for (int i = 0; i < columns.size(); i++) {
            CodegenSpec.ColumnSpec col = columns.get(i);
            if (col == createCol) {
                continue;
            }
            if ("Y".equals(col.getIsQuery())) {
                lastQueryIndex = i;
            }
        }
        if (lastQueryIndex < 0 || createIndex > lastQueryIndex) {
            return;
        }
        columns.remove(createIndex);
        if (createIndex < lastQueryIndex) {
            lastQueryIndex -= 1;
        }
        columns.add(lastQueryIndex + 1, createCol);
    }

    private static boolean isCreateTime(CodegenSpec.ColumnSpec col) {
        if (col == null || col.getFieldDbName() == null) {
            return false;
        }
        return "create_time".equalsIgnoreCase(col.getFieldDbName());
    }

    private static List<CodegenSpec.ColumnSpec> reorderColumns(List<CodegenSpec.ColumnSpec> columns) {
        List<CodegenSpec.ColumnSpec> normal = new ArrayList<>();
        List<CodegenSpec.ColumnSpec> files = new ArrayList<>();
        List<CodegenSpec.ColumnSpec> rich = new ArrayList<>();
        for (CodegenSpec.ColumnSpec col : columns) {
            String classType = col.getClassType();
            if ("file".equals(classType) || "image".equals(classType)) {
                files.add(col);
            } else if ("textarea".equals(classType) || "umeditor".equals(classType) || "markdown".equals(classType)) {
                rich.add(col);
            } else {
                normal.add(col);
            }
        }
        List<CodegenSpec.ColumnSpec> ordered = new ArrayList<>(columns.size());
        ordered.addAll(normal);
        ordered.addAll(files);
        ordered.addAll(rich);
        return ordered;
    }

    private static void resetFieldOrder(List<CodegenSpec.ColumnSpec> columns) {
        int order = 0;
        for (CodegenSpec.ColumnSpec col : columns) {
            col.setFieldOrderNum(order++);
        }
    }

    private static int inferFieldRowNum(List<CodegenSpec.ColumnSpec> columns) {
        int visibleCount = 0;
        for (CodegenSpec.ColumnSpec col : columns) {
            if (!"Y".equals(col.getIsShow())) {
                continue;
            }
            String fieldName = col.getFieldName();
            if (fieldName != null && "id".equalsIgnoreCase(fieldName)) {
                continue;
            }
            visibleCount++;
        }
        if (visibleCount <= 6) {
            return 1;
        }
        if (visibleCount <= 12) {
            return 2;
        }
        if (visibleCount <= 18) {
            return 3;
        }
        return 4;
    }

    private static ColumnType mapType(String typeToken) {
        String lower = typeToken.toLowerCase(Locale.ROOT);
        String base = lower;
        String charmaxLength = null;
        String precision = null;
        String scale = null;
        int paren = lower.indexOf('(');
        if (paren > 0) {
            base = lower.substring(0, paren).trim();
            int end = lower.indexOf(')', paren + 1);
            if (end > paren) {
                String inside = lower.substring(paren + 1, end);
                String[] parts = inside.split(",");
                if (parts.length == 1) {
                    charmaxLength = parts[0].trim();
                } else if (parts.length >= 2) {
                    precision = parts[0].trim();
                    scale = parts[1].trim();
                }
            }
        }

        ColumnType type = new ColumnType();
        type.charmaxLength = charmaxLength;
        type.precision = precision;
        type.scale = scale;

        if (base.contains("char") || base.contains("varchar") || base.contains("text")) {
            type.fieldDbType = "string";
            type.fieldType = "String";
            if (base.contains("text")) {
                type.fieldDbType = "Text";
            }
            return type;
        }
        if (base.contains("bigint")) {
            type.fieldDbType = "int";
            type.fieldType = "Long";
            return type;
        }
        if (base.contains("int")) {
            type.fieldDbType = "int";
            type.fieldType = "Integer";
            return type;
        }
        if (base.contains("decimal") || base.contains("numeric")) {
            type.fieldDbType = "BigDecimal";
            type.fieldType = "BigDecimal";
            return type;
        }
        if (base.contains("double") || base.contains("float")) {
            type.fieldDbType = "double";
            type.fieldType = "Double";
            return type;
        }
        if (base.contains("date") || base.contains("timestamp") || base.contains("datetime")) {
            type.fieldDbType = base.contains("date") && !base.contains("time") ? "Date" : "Datetime";
            type.fieldType = "Date";
            return type;
        }
        if (base.contains("time")) {
            type.fieldDbType = "time";
            type.fieldType = "Date";
            return type;
        }
        if (base.contains("blob") || base.contains("binary")) {
            type.fieldDbType = "Blob";
            type.fieldType = "byte[]";
            return type;
        }
        throw new IllegalArgumentException("unsupported column type: " + typeToken);
    }

    private static String extractDictCode(String comment) {
        Pattern pattern = Pattern.compile("(?:字典|dict)[:：]\\s*([A-Za-z0-9_\\-]+)");
        Matcher matcher = pattern.matcher(comment);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return null;
    }

    private static String resolveEntityPackage(String override, String tableName) {
        if (override != null && !override.trim().isEmpty()) {
            return override.trim();
        }
        int idx = tableName.indexOf('_');
        if (idx > 0) {
            return tableName.substring(0, idx);
        }
        return "demo";
    }

    private static String toCamel(String name) {
        String[] parts = name.split("[_\\-\\s]+");
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            if (part.isEmpty()) {
                continue;
            }
            String lower = part.toLowerCase(Locale.ROOT);
            if (i == 0) {
                out.append(lower);
            } else {
                out.append(Character.toUpperCase(lower.charAt(0))).append(lower.substring(1));
            }
        }
        return out.toString();
    }

    private static String toPascal(String name) {
        String camel = toCamel(name);
        if (camel.isEmpty()) {
            return camel;
        }
        return Character.toUpperCase(camel.charAt(0)) + camel.substring(1);
    }

    private static final Set<String> CLASS_TYPE_NEEDS_DICT = new HashSet<>();

    static {
        String[] needsDict = {
            "list", "radio", "checkbox", "list_multi",
            "sel_search", "sel_user", "sel_depart", "sel_tree", "cat_tree", "popup"
        };
        for (String t : needsDict) {
            CLASS_TYPE_NEEDS_DICT.add(t);
        }
    }

    private static final class ColumnType {
        String fieldDbType;
        String fieldType;
        String charmaxLength;
        String precision;
        String scale;
    }

    private static final class ParsedTable {
        String tableName;
        String tableComment;
        List<CodegenSpec.ColumnSpec> columns;
        Set<String> primaryKeys;
    }
}
