package org.jeecg.tools.codegen;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import org.jeecg.common.constant.enums.CgformEnum;
import org.jeecgframework.codegenerate.generate.impl.CodeGenerateOne;
import org.jeecgframework.codegenerate.generate.impl.CodeGenerateOneToMany;
import org.jeecgframework.codegenerate.generate.pojo.ColumnVo;
import org.jeecgframework.codegenerate.generate.pojo.TableVo;
import org.jeecgframework.codegenerate.generate.pojo.onetomany.MainTableVo;
import org.jeecgframework.codegenerate.generate.pojo.onetomany.SubTableVo;

final class CodegenExecutor {
    private final CodegenSpec spec;

    CodegenExecutor(CodegenSpec spec) {
        this.spec = spec;
    }

    void run() throws Exception {
        normalizeAndValidate();
        CgformEnum cgform = CgformEnum.getCgformEnumByConfig(spec.getJspMode());
        if (cgform == null) {
            throw new IllegalArgumentException("Unknown jspMode: " + spec.getJspMode());
        }

        String templatePath = spec.getTemplatePath() != null ? spec.getTemplatePath() : cgform.getTemplatePath();
        String stylePath = cgform.getStylePath();
        applyGlobalConfig(templatePath);

        if (cgform.getType() == 2) {
            generateOneToMany(templatePath, stylePath);
        } else {
            generateOne(templatePath, stylePath);
        }
        routeFrontendOutputs();
    }

    private void normalizeAndValidate() {
        if (spec == null) {
            throw new IllegalArgumentException("spec is required");
        }
        if (isBlank(spec.getProjectPath())) {
            throw new IllegalArgumentException("projectPath is required");
        }
        if (isBlank(spec.getJspMode())) {
            throw new IllegalArgumentException("jspMode is required");
        }
        String jspMode = spec.getJspMode().trim();
        if (!VALID_JSP_MODE.contains(jspMode)) {
            throw new IllegalArgumentException("invalid jspMode: " + jspMode);
        }
        CgformEnum cgform = CgformEnum.getCgformEnumByConfig(jspMode);
        if (cgform == null) {
            throw new IllegalArgumentException("Unknown jspMode: " + jspMode);
        }
        if (!isBlank(spec.getVueStyle())) {
            validateVueStyle(cgform, spec.getVueStyle().trim());
        }
        if (spec.getTable() == null) {
            throw new IllegalArgumentException("table is required");
        }
        normalizeTable(spec.getTable());
        if (spec.getColumns() == null || spec.getColumns().isEmpty()) {
            throw new IllegalArgumentException("columns is required");
        }
        for (CodegenSpec.ColumnSpec column : spec.getColumns()) {
            normalizeColumn(column);
        }
        if (cgform.getType() == 2) {
            if (spec.getSubTables() == null || spec.getSubTables().isEmpty()) {
                throw new IllegalArgumentException("subTables is required for onetomany modes");
            }
            for (CodegenSpec.SubTableSpec sub : spec.getSubTables()) {
                normalizeSubTable(sub);
            }
        }
    }

    private void normalizeTable(CodegenSpec.TableSpec table) {
        if (isBlank(table.getTableName())) {
            throw new IllegalArgumentException("table.tableName is required");
        }
        if (isBlank(table.getEntityName())) {
            throw new IllegalArgumentException("table.entityName is required");
        }
        if (table.getFieldRowNum() == null) {
            table.setFieldRowNum(2);
        }
        if (table.getExtendParams() == null) {
            table.setExtendParams(new HashMap<>());
        }
        if (!table.getExtendParams().containsKey("scroll")) {
            table.getExtendParams().put("scroll", "0");
        }
    }

    private void normalizeSubTable(CodegenSpec.SubTableSpec sub) {
        if (isBlank(sub.getTableName())) {
            throw new IllegalArgumentException("subTables.tableName is required");
        }
        if (isBlank(sub.getEntityName())) {
            throw new IllegalArgumentException("subTables.entityName is required");
        }
        if (sub.getColumns() == null || sub.getColumns().isEmpty()) {
            throw new IllegalArgumentException("subTables.columns is required");
        }
        if (sub.getForeignKeys() == null || sub.getForeignKeys().isEmpty()) {
            throw new IllegalArgumentException("subTables.foreignKeys is required");
        }
        if (sub.getForeignMainKeys() == null || sub.getForeignMainKeys().isEmpty()) {
            throw new IllegalArgumentException("subTables.foreignMainKeys is required");
        }
        if (sub.getExtendParams() == null) {
            sub.setExtendParams(new HashMap<>());
        }
        for (CodegenSpec.ColumnSpec column : sub.getColumns()) {
            normalizeColumn(column);
        }
        if (sub.getOriginalColumns() != null) {
            for (CodegenSpec.ColumnSpec column : sub.getOriginalColumns()) {
                normalizeColumn(column);
            }
        }
    }

    private void normalizeColumn(CodegenSpec.ColumnSpec col) {
        if (isBlank(col.getFieldDbName())) {
            throw new IllegalArgumentException("column.fieldDbName is required");
        }
        if (isBlank(col.getFieldName())) {
            throw new IllegalArgumentException("column.fieldName is required");
        }
        // defaults
        if (col.getIsShow() == null) {
            col.setIsShow(defaultShow(col.getFieldDbName()) ? "Y" : "N");
        }
        if (col.getIsShowList() == null) {
            col.setIsShowList(defaultShowList(col.getFieldDbName()) ? "Y" : "N");
        }
        if (col.getIsQuery() == null) {
            col.setIsQuery("N");
        }
        if (col.getNullable() == null) {
            col.setNullable("Y");
        }
        if (col.getSort() == null) {
            col.setSort("N");
        }
        if (col.getReadonly() == null) {
            col.setReadonly("N");
        }
        if (col.getIsKey() == null) {
            col.setIsKey("N");
        }
        if (col.getQueryMode() == null) {
            col.setQueryMode("single");
        }
        if (isBlank(col.getClassType())) {
            col.setClassType("default");
        }
        if (isBlank(col.getFieldShowType())) {
            col.setFieldShowType(col.getClassType());
        }
        if (col.getExtendParams() == null) {
            col.setExtendParams(new HashMap<>());
        }
        ensureUiLabel(col);
        // enum checks
        enforceYN("isShow", col.getIsShow());
        enforceYN("isShowList", col.getIsShowList());
        enforceYN("isQuery", col.getIsQuery());
        enforceYN("nullable", col.getNullable());
        enforceYN("sort", col.getSort());
        enforceYN("readonly", col.getReadonly());
        enforceYN("isKey", col.getIsKey());
        if (!"single".equals(col.getQueryMode()) && !"group".equals(col.getQueryMode())) {
            throw new IllegalArgumentException("column.queryMode must be single or group for " + col.getFieldDbName());
        }
        if (!VALID_CLASS_TYPES.contains(col.getClassType())) {
            throw new IllegalArgumentException("column.classType not supported: " + col.getClassType() + " @ " + col.getFieldDbName());
        }
        if (!col.getClassType().equals(col.getFieldShowType())) {
            throw new IllegalArgumentException("column.fieldShowType must equal classType for " + col.getFieldDbName());
        }
        if (CLASS_TYPE_NEEDS_DICT.contains(col.getClassType())) {
            if (isBlank(col.getDictField())) {
                throw new IllegalArgumentException("column.dictField is required for classType=" + col.getClassType() + " @ " + col.getFieldDbName());
            }
        }
    }

    private void enforceYN(String field, String value) {
        if (!"Y".equals(value) && !"N".equals(value)) {
            throw new IllegalArgumentException(field + " must be Y or N");
        }
    }

    private void validateVueStyle(CgformEnum cgform, String vueStyle) {
        String[] allowed = cgform.getVueStyle();
        if (allowed == null || allowed.length == 0) {
            throw new IllegalArgumentException("jspMode " + spec.getJspMode() + " does not support vueStyle");
        }
        for (String style : allowed) {
            if (style.equalsIgnoreCase(vueStyle)) {
                return;
            }
        }
        throw new IllegalArgumentException("vueStyle " + vueStyle + " not supported by jspMode " + spec.getJspMode());
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private boolean defaultShow(String name) {
        String lower = name.toLowerCase(java.util.Locale.ROOT);
        return !(lower.equals("id")
            || lower.equals("create_time")
            || lower.equals("update_time")
            || lower.equals("create_by")
            || lower.equals("update_by"));
    }

    private boolean defaultShowList(String name) {
        String lower = name.toLowerCase(java.util.Locale.ROOT);
        return !(lower.equals("id")
            || lower.equals("del_flag")
            || lower.equals("update_time")
            || lower.equals("update_by"));
    }

    private void ensureUiLabel(CodegenSpec.ColumnSpec col) {
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

    private String cleanUiLabel(String comment) {
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

    private void applyGlobalConfig(String templatePath) {
        org.jeecgframework.codegenerate.a.a.a(spec.getProjectPath());
        org.jeecgframework.codegenerate.a.a.b(templatePath);
        if (spec.getBussiPackage() != null && !spec.getBussiPackage().trim().isEmpty()) {
            org.jeecgframework.codegenerate.a.a.g = spec.getBussiPackage().trim();
        }
        if (spec.getSourceRootPackage() != null && !spec.getSourceRootPackage().trim().isEmpty()) {
            org.jeecgframework.codegenerate.a.a.h = spec.getSourceRootPackage().trim();
        }
        if (spec.getWebRootPackage() != null && !spec.getWebRootPackage().trim().isEmpty()) {
            org.jeecgframework.codegenerate.a.a.i = spec.getWebRootPackage().trim();
        }
        if (spec.getPrimaryKeyField() != null && !spec.getPrimaryKeyField().trim().isEmpty()) {
            org.jeecgframework.codegenerate.a.a.l = spec.getPrimaryKeyField().trim();
        }
    }

    private void generateOne(String templatePath, String stylePath) throws Exception {
        TableVo tableVo = toTableVo(spec.getTable());
        List<ColumnVo> columns = toColumnVoList(spec.getColumns());
        List<ColumnVo> originalColumns = new ArrayList<>(columns);
        CodeGenerateOne generator = new CodeGenerateOne(tableVo, columns, originalColumns);
        generator.generateCodeFile(spec.getProjectPath(), templatePath, stylePath);
    }

    private void generateOneToMany(String templatePath, String stylePath) throws Exception {
        MainTableVo mainTableVo = toMainTableVo(spec.getTable());
        List<ColumnVo> mainColumns = toColumnVoList(spec.getColumns());
        List<ColumnVo> originalMainColumns = new ArrayList<>(mainColumns);
        List<SubTableVo> subTables = toSubTables(spec.getSubTables(), spec.getTable().getEntityPackage());
        CodeGenerateOneToMany generator = new CodeGenerateOneToMany(mainTableVo, mainColumns, originalMainColumns, subTables);
        generator.generateCodeFile(spec.getProjectPath(), templatePath, stylePath);
    }

    private void routeFrontendOutputs() throws IOException {
        String frontendRoot = spec.getFrontendRoot();
        if (frontendRoot == null || frontendRoot.trim().isEmpty()) {
            return;
        }
        String projectPath = spec.getProjectPath();
        if (projectPath == null || projectPath.trim().isEmpty()) {
            return;
        }
        String sourceRootPackage = spec.getSourceRootPackage();
        if (sourceRootPackage == null || sourceRootPackage.trim().isEmpty()) {
            sourceRootPackage = org.jeecgframework.codegenerate.a.a.h;
        }
        Path sourceRoot = Paths.get(projectPath, sourceRootPackage.replace(".", java.io.File.separator));
        if (!Files.isDirectory(sourceRoot)) {
            return;
        }

        String[] vueDirs = {"vue", "vue3", "vue3Native"};
        List<String> conflicts = new ArrayList<>();
        int copied = 0;

        try (Stream<Path> stream = Files.walk(sourceRoot, FileVisitOption.FOLLOW_LINKS)) {
            for (Path path : (Iterable<Path>) stream::iterator) {
                if (!Files.isRegularFile(path)) {
                    continue;
                }
                Path relative = sourceRoot.relativize(path);
                int vueIndex = indexOfVueSegment(relative, vueDirs);
                if (vueIndex < 0) {
                    continue;
                }
                Path targetRel = relative.subpath(vueIndex + 1, relative.getNameCount());
                if (vueIndex > 0) {
                    String moduleSegment = relative.getName(vueIndex - 1).toString();
                    if (!moduleSegment.isEmpty()) {
                        targetRel = Paths.get(moduleSegment).resolve(targetRel);
                    }
                }
                Path target = Paths.get(frontendRoot).resolve(targetRel);
                if (Files.exists(target)) {
                    conflicts.add(target.toString());
                    continue;
                }
                if (target.getParent() != null) {
                    Files.createDirectories(target.getParent());
                }
                Files.copy(path, target);
                copied++;
            }
        }

        if (!conflicts.isEmpty()) {
            System.out.println("[codegen] frontendRoot conflicts: " + conflicts.size());
            for (String item : conflicts) {
                System.out.println("[codegen] skip existing: " + item);
            }
        }
        if (copied > 0) {
            System.out.println("[codegen] frontendRoot copied files: " + copied);
        }
    }

    private int indexOfVueSegment(Path relative, String[] vueDirs) {
        for (int i = 0; i < relative.getNameCount(); i++) {
            String segment = relative.getName(i).toString();
            for (String vueDir : vueDirs) {
                if (vueDir.equals(segment)) {
                    return i;
                }
            }
        }
        return -1;
    }

    private TableVo toTableVo(CodegenSpec.TableSpec table) {
        TableVo vo = new TableVo();
        vo.setTableName(table.getTableName());
        vo.setEntityPackage(table.getEntityPackage());
        vo.setEntityName(table.getEntityName());
        vo.setFtlDescription(table.getFtlDescription());
        vo.setPrimaryKeyPolicy(table.getPrimaryKeyPolicy());
        vo.setSequenceCode(table.getSequenceCode());
        vo.setFieldRowNum(table.getFieldRowNum());
        vo.setSearchFieldNum(table.getSearchFieldNum());
        vo.setFieldRequiredNum(table.getFieldRequiredNum());
        vo.setExtendParams(mergeExtendParams(table.getExtendParams()));
        return vo;
    }

    private MainTableVo toMainTableVo(CodegenSpec.TableSpec table) {
        MainTableVo vo = new MainTableVo();
        vo.setTableName(table.getTableName());
        vo.setEntityPackage(table.getEntityPackage());
        vo.setEntityName(table.getEntityName());
        vo.setFtlDescription(table.getFtlDescription());
        vo.setPrimaryKeyPolicy(table.getPrimaryKeyPolicy());
        vo.setSequenceCode(table.getSequenceCode());
        vo.setFieldRowNum(table.getFieldRowNum());
        vo.setSearchFieldNum(table.getSearchFieldNum());
        vo.setFieldRequiredNum(table.getFieldRequiredNum());
        vo.setExtendParams(mergeExtendParams(table.getExtendParams()));
        return vo;
    }

    private Map<String, Object> mergeExtendParams(Map<String, Object> extendParams) {
        Map<String, Object> merged = new HashMap<>();
        if (extendParams != null) {
            merged.putAll(extendParams);
        }
        if (spec.getVueStyle() != null && !spec.getVueStyle().trim().isEmpty()) {
            merged.put("vueStyle", spec.getVueStyle().trim());
        }
        return merged;
    }

    private static final Set<String> VALID_CLASS_TYPES = new HashSet<>();
    private static final Set<String> CLASS_TYPE_NEEDS_DICT = new HashSet<>();
    private static final Set<String> VALID_JSP_MODE = new HashSet<>();

    static {
        String[] classTypes = {
            "default", "date", "datetime", "time", "textarea", "password",
            "list", "radio", "checkbox", "list_multi", "sel_search",
            "sel_user", "sel_depart", "sel_tree", "cat_tree", "popup",
            "switch", "file", "image", "umeditor", "markdown", "pca"
        };
        for (String t : classTypes) {
            VALID_CLASS_TYPES.add(t);
        }
        String[] needsDict = {
            "list", "radio", "checkbox", "list_multi",
            "sel_search", "sel_user", "sel_depart", "sel_tree", "cat_tree", "popup"
        };
        for (String t : needsDict) {
            CLASS_TYPE_NEEDS_DICT.add(t);
        }
        String[] jspModes = {"one", "tree", "many", "jvxe", "erp", "innerTable", "tab"};
        for (String m : jspModes) {
            VALID_JSP_MODE.add(m);
        }
    }

    private List<ColumnVo> toColumnVoList(List<CodegenSpec.ColumnSpec> specs) {
        List<ColumnVo> columns = new ArrayList<>();
        for (CodegenSpec.ColumnSpec specColumn : specs) {
            columns.add(toColumnVo(specColumn));
        }
        return columns;
    }

    private ColumnVo toColumnVo(CodegenSpec.ColumnSpec specColumn) {
        ColumnVo vo = new ColumnVo();
        vo.setFieldDbName(specColumn.getFieldDbName());
        vo.setFieldName(specColumn.getFieldName());
        vo.setFiledComment(specColumn.getFiledComment());
        vo.setFieldType(specColumn.getFieldType());
        vo.setFieldDbType(specColumn.getFieldDbType());
        vo.setCharmaxLength(specColumn.getCharmaxLength());
        vo.setPrecision(specColumn.getPrecision());
        vo.setScale(specColumn.getScale());
        vo.setNullable(specColumn.getNullable());
        vo.setClassType(specColumn.getClassType());
        vo.setClassType_row(specColumn.getClassTypeRow());
        vo.setOptionType(specColumn.getOptionType());

        vo.setFieldLength(specColumn.getFieldLength());
        vo.setFieldHref(specColumn.getFieldHref());
        vo.setFieldValidType(specColumn.getFieldValidType());
        vo.setFieldDefault(specColumn.getFieldDefault());
        vo.setFieldShowType(specColumn.getFieldShowType());
        vo.setFieldOrderNum(specColumn.getFieldOrderNum());
        vo.setIsKey(specColumn.getIsKey());
        vo.setIsShow(specColumn.getIsShow());
        vo.setIsShowList(specColumn.getIsShowList());
        vo.setIsQuery(specColumn.getIsQuery());
        vo.setQueryMode(specColumn.getQueryMode());
        vo.setDictField(specColumn.getDictField());
        vo.setDictTable(specColumn.getDictTable());
        vo.setDictText(specColumn.getDictText());
        vo.setSort(specColumn.getSort());
        vo.setReadonly(specColumn.getReadonly());
        vo.setDefaultVal(specColumn.getDefaultVal());
        vo.setUploadnum(specColumn.getUploadnum());
        vo.setExtendParams(specColumn.getExtendParams());
        return vo;
    }

    private List<SubTableVo> toSubTables(List<CodegenSpec.SubTableSpec> subSpecs, String defaultEntityPackage) {
        List<SubTableVo> subTables = new ArrayList<>();
        for (CodegenSpec.SubTableSpec subSpec : subSpecs) {
            if (subSpec.getForeignKeys() == null || subSpec.getForeignKeys().isEmpty()) {
                throw new IllegalArgumentException("subTables.foreignKeys is required");
            }
            if (subSpec.getForeignMainKeys() == null || subSpec.getForeignMainKeys().isEmpty()) {
                throw new IllegalArgumentException("subTables.foreignMainKeys is required");
            }
            SubTableVo vo = new SubTableVo();
            vo.setTableName(subSpec.getTableName());
            vo.setEntityPackage(subSpec.getEntityPackage() != null ? subSpec.getEntityPackage() : defaultEntityPackage);
            vo.setEntityName(subSpec.getEntityName());
            vo.setFtlDescription(subSpec.getFtlDescription());
            vo.setPrimaryKeyPolicy(subSpec.getPrimaryKeyPolicy());
            vo.setSequenceCode(subSpec.getSequenceCode());
            vo.setForeignRelationType(subSpec.getForeignRelationType());
            vo.setForeignKeys(subSpec.getForeignKeys().toArray(new String[0]));
            vo.setForeignMainKeys(subSpec.getForeignMainKeys().toArray(new String[0]));
            vo.setExtendParams(subSpec.getExtendParams());

            List<ColumnVo> columns = toColumnVoList(subSpec.getColumns());
            vo.setColums(columns);
            if (subSpec.getOriginalColumns() != null && !subSpec.getOriginalColumns().isEmpty()) {
                vo.setOriginalColumns(toColumnVoList(subSpec.getOriginalColumns()));
            } else {
                vo.setOriginalColumns(new ArrayList<>(columns));
            }
            subTables.add(vo);
        }
        return subTables;
    }
}
