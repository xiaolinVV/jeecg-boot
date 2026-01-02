package org.jeecg.tools.codegen;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitOption;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
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
    private static final String DEFAULT_TEMPLATE_CLASSPATH = "/jeecg/code-template-online";

    CodegenExecutor(CodegenSpec spec) {
        this.spec = spec;
    }

    void run() throws Exception {
        normalizeAndValidate();
        CgformEnum cgform = CgformEnum.getCgformEnumByConfig(spec.getJspMode());
        if (cgform == null) {
            throw new IllegalArgumentException("Unknown jspMode: " + spec.getJspMode());
        }

        boolean templateProvided = !isBlank(spec.getTemplatePath());
        String templatePath = templateProvided ? spec.getTemplatePath() : cgform.getTemplatePath();
        TemplateResolution resolution = resolveTemplatePaths(templatePath, templateProvided);
        templatePath = resolution.codegenPath;
        String stylePath = cgform.getStylePath();
        applyGlobalConfig(templatePath);

        if (cgform.getType() == 2) {
            generateOneToMany(templatePath, stylePath);
        } else {
            generateOne(templatePath, stylePath);
        }
        routeFrontendOutputs();
    }

    Map<String, List<String>> dryRun() throws Exception {
        normalizeAndValidate();
        CgformEnum cgform = CgformEnum.getCgformEnumByConfig(spec.getJspMode());
        if (cgform == null) {
            throw new IllegalArgumentException("Unknown jspMode: " + spec.getJspMode());
        }
        boolean templateProvided = !isBlank(spec.getTemplatePath());
        String templatePath = templateProvided ? spec.getTemplatePath() : cgform.getTemplatePath();
        TemplateResolution resolution = resolveTemplatePaths(templatePath, templateProvided);
        templatePath = resolution.codegenPath;
        String stylePath = cgform.getStylePath();
        applyGlobalConfig(templatePath);

        String listTemplatePath = resolution.listPath != null ? resolution.listPath : templatePath;
        List<String> templateFiles = listTemplateFiles(listTemplatePath, stylePath);
        String projectPath = spec.getProjectPath();
        String sourceRootPackage = resolveSourceRootPackage();
        Path sourceRoot = Paths.get(projectPath, sourceRootPackage.replace(".", java.io.File.separator));
        String frontendRoot = spec.getFrontendRoot();
        boolean hasFrontendRoot = frontendRoot != null && !frontendRoot.trim().isEmpty();
        String bussiPackage = resolveBussiPackage();
        String entityPackage = resolveEntityPackage();
        String entityName = resolveEntityName();
        String currentDate = java.time.format.DateTimeFormatter.BASIC_ISO_DATE.format(java.time.LocalDate.now());
        List<CodegenSpec.SubTableSpec> subTables = spec.getSubTables();
        String vueStyle = spec.getVueStyle();

        LinkedHashSet<String> backend = new LinkedHashSet<>();
        LinkedHashSet<String> frontend = new LinkedHashSet<>();

        for (String rel : templateFiles) {
            if (rel == null || rel.isEmpty()) {
                continue;
            }
            if (!shouldIncludeVueStyle(rel, vueStyle)) {
                continue;
            }
            String outputRel = rel;
            String base = "";
            if (rel.startsWith("java/") || rel.startsWith("java\\")) {
                outputRel = rel.substring(5);
                base = sourceRootPackage;
            }
            List<String> expanded = expandSubTablePaths(outputRel, subTables);
            for (String entry : expanded) {
                String rendered = renderTemplatePath(entry, bussiPackage, entityPackage, entityName, currentDate);
                if (rendered == null || rendered.isEmpty()) {
                    continue;
                }
                rendered = normalizeTemplateExtension(rendered);
                Path output = base.isEmpty()
                    ? Paths.get(projectPath, rendered)
                    : Paths.get(projectPath, base.replace(".", java.io.File.separator), rendered);
                output = output.normalize();

                if (hasFrontendRoot && output.startsWith(sourceRoot)) {
                    Path relative = sourceRoot.relativize(output);
                    int vueIndex = indexOfVueSegment(relative, new String[] {"vue", "vue3", "vue3Native"});
                    if (vueIndex >= 0) {
                        Path targetRel = relative.subpath(vueIndex + 1, relative.getNameCount());
                        Path target = Paths.get(frontendRoot).resolve(targetRel).normalize();
                        frontend.add(target.toString());
                        continue;
                    }
                }
                backend.add(output.toString());
            }
        }

        List<String> backendList = backend.stream().sorted().collect(Collectors.toList());
        List<String> frontendList = frontend.stream().sorted().collect(Collectors.toList());
        Map<String, List<String>> out = new LinkedHashMap<>();
        out.put("backend", backendList);
        out.put("frontend", frontendList);
        return out;
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
            System.out.println("[codegen] frontendRoot not configured, skip routing");
            return;
        }
        String projectPath = spec.getProjectPath();
        if (projectPath == null || projectPath.trim().isEmpty()) {
            System.out.println("[codegen] projectPath not configured, skip routing");
            return;
        }
        String sourceRootPackage = spec.getSourceRootPackage();
        if (sourceRootPackage == null || sourceRootPackage.trim().isEmpty()) {
            sourceRootPackage = org.jeecgframework.codegenerate.a.a.h;
        }
        if (sourceRootPackage == null || sourceRootPackage.trim().isEmpty()) {
            sourceRootPackage = "src/main/java";
        }
        Path sourceRoot = Paths.get(projectPath, sourceRootPackage.replace(".", java.io.File.separator));
        if (!Files.isDirectory(sourceRoot)) {
            System.out.println("[codegen] sourceRoot not found: " + sourceRoot);
            return;
        }

        String entityPackage = resolveEntityPackage();
        if (isBlank(entityPackage)) {
            System.out.println("[codegen] entityPackage is blank, skip routing");
            return;
        }
        String bussiPackage = resolveBussiPackage();
        Path moduleRoot = sourceRoot.resolve(bussiPackage.replace(".", java.io.File.separator))
                                    .resolve(entityPackage.replace(".", java.io.File.separator));
        System.out.println("[codegen] moduleRoot: " + moduleRoot);
        if (!Files.isDirectory(moduleRoot)) {
            System.out.println("[codegen] moduleRoot not found, skip routing");
            return;
        }

        String[] vueDirs = {"vue", "vue3", "vue3Native"};
        List<String> conflicts = new ArrayList<>();
        int copied = 0;

        try (Stream<Path> stream = Files.walk(moduleRoot, 10)) {
            List<Path> files = stream.filter(Files::isRegularFile).collect(Collectors.toList());
            System.out.println("[codegen] found " + files.size() + " files in moduleRoot");
            for (Path path : files) {
                Path relative = moduleRoot.relativize(path);
                int vueIndex = indexOfVueSegment(relative, vueDirs);
                if (vueIndex < 0) {
                    continue;
                }
                if (vueIndex + 1 >= relative.getNameCount()) {
                    continue;
                }
                Path targetRel = relative.subpath(vueIndex + 1, relative.getNameCount());
                targetRel = Paths.get(entityPackage).resolve(targetRel);
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
        System.out.println("[codegen] routeFrontendOutputs completed");
    }

    private String resolveSourceRootPackage() {
        String sourceRootPackage = spec.getSourceRootPackage();
        if (isBlank(sourceRootPackage)) {
            sourceRootPackage = org.jeecgframework.codegenerate.a.a.h;
        }
        if (isBlank(sourceRootPackage)) {
            sourceRootPackage = "src/main/java";
        }
        return sourceRootPackage.trim();
    }

    private String resolveBussiPackage() {
        if (!isBlank(spec.getBussiPackage())) {
            return spec.getBussiPackage().trim();
        }
        String fallback = org.jeecgframework.codegenerate.a.a.g;
        return isBlank(fallback) ? "" : fallback.trim();
    }

    private String resolveEntityPackage() {
        String entityPackage = spec.getTable() != null ? spec.getTable().getEntityPackage() : null;
        if (!isBlank(entityPackage)) {
            return entityPackage.trim();
        }
        return "";
    }

    private String resolveEntityName() {
        String entityName = spec.getTable() != null ? spec.getTable().getEntityName() : null;
        if (!isBlank(entityName)) {
            return entityName.trim();
        }
        return "";
    }

    private TemplateResolution resolveTemplatePaths(String templatePath, boolean templateProvided) throws IOException {
        if (isBlank(templatePath)) {
            return new TemplateResolution(templatePath, templatePath);
        }
        Path configRoot = Paths.get(System.getProperty("user.dir"), "config", "jeecg", "code-template-online");
        Path direct = Paths.get(templatePath);
        if (Files.isDirectory(direct)) {
            materializeTemplateRoot(direct, configRoot, true);
            String listPath = direct.toAbsolutePath().normalize().toString();
            String codegenPath = DEFAULT_TEMPLATE_CLASSPATH;
            return new TemplateResolution(codegenPath, listPath);
        }

        String normalized = templatePath.startsWith("/") ? templatePath.substring(1) : templatePath;
        if (!templateProvided) {
            Path workspace = resolveWorkspaceTemplateRoot(normalized);
            if (workspace != null) {
                materializeTemplateRoot(workspace, configRoot, true);
                String listPath = workspace.toAbsolutePath().normalize().toString();
                String codegenPath = DEFAULT_TEMPLATE_CLASSPATH;
                return new TemplateResolution(codegenPath, listPath);
            }
        }

        URL url = CodegenExecutor.class.getClassLoader().getResource(normalized);
        if (url == null) {
            return new TemplateResolution(templatePath, templatePath);
        }
        try {
            URI uri = url.toURI();
            if ("file".equalsIgnoreCase(uri.getScheme())) {
                Path root = Paths.get(uri);
                if (Files.isDirectory(root)) {
                    String listPath = root.toAbsolutePath().normalize().toString();
                    return new TemplateResolution(templatePath, listPath);
                }
            }
            if ("jar".equalsIgnoreCase(uri.getScheme())) {
                materializeFromJar(uri, normalized, configRoot);
                String listPath = configRoot.toAbsolutePath().normalize().toString();
                return new TemplateResolution(templatePath, listPath);
            }
        } catch (Exception e) {
            throw new IOException("Failed to resolve templatePath: " + templatePath, e);
        }
        return new TemplateResolution(templatePath, templatePath);
    }

    private Path resolveWorkspaceTemplateRoot(String normalizedPath) {
        Path cwd = Paths.get(".").toAbsolutePath().normalize();
        Path relativeRoot = Paths.get("jeecg-module-system/jeecg-system-biz/src/main/resources").resolve(normalizedPath);
        Path current = cwd;
        for (int i = 0; i < 6 && current != null; i++) {
            Path candidate = resolveNestedCandidate(current, relativeRoot);
            if (candidate != null) {
                return candidate.toAbsolutePath().normalize();
            }
            current = current.getParent();
        }
        return null;
    }

    private Path resolveNestedCandidate(Path base, Path relativeRoot) {
        Path direct = base.resolve(relativeRoot);
        if (Files.isDirectory(direct)) {
            return direct;
        }
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(base)) {
            for (Path entry : stream) {
                if (!Files.isDirectory(entry)) {
                    continue;
                }
                Path nested = entry.resolve(relativeRoot);
                if (Files.isDirectory(nested)) {
                    return nested;
                }
            }
        } catch (IOException ignored) {
        }
        return null;
    }

    private boolean hasTemplateFiles(Path root) throws IOException {
        if (!Files.isDirectory(root)) {
            return false;
        }
        try (Stream<Path> stream = Files.walk(root, 2)) {
            return stream.anyMatch(Files::isRegularFile);
        }
    }

    private void materializeTemplateRoot(Path sourceRoot, Path targetRoot, boolean overwrite) throws IOException {
        if (!overwrite && hasTemplateFiles(targetRoot)) {
            return;
        }
        try (Stream<Path> stream = Files.walk(sourceRoot, FileVisitOption.FOLLOW_LINKS)) {
            for (Path path : (Iterable<Path>) stream::iterator) {
                Path rel = sourceRoot.relativize(path);
                Path target = targetRoot.resolve(rel.toString());
                if (Files.isDirectory(path)) {
                    Files.createDirectories(target);
                } else if (Files.isRegularFile(path)) {
                    if (target.getParent() != null) {
                        Files.createDirectories(target.getParent());
                    }
                    if (overwrite) {
                        Files.copy(path, target, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
                    } else if (!Files.exists(target)) {
                        Files.copy(path, target, StandardCopyOption.COPY_ATTRIBUTES);
                    }
                }
            }
        }
    }

    private void materializeFromJar(URI uri, String normalizedPath, Path targetRoot) throws IOException {
        if (hasTemplateFiles(targetRoot)) {
            return;
        }
        FileSystem fs = null;
        try {
            try {
                fs = FileSystems.newFileSystem(uri, Collections.emptyMap());
            } catch (java.nio.file.FileSystemAlreadyExistsException ex) {
                fs = FileSystems.getFileSystem(uri);
            }
            Path root = fs.getPath("/" + normalizedPath);
            if (!Files.exists(root)) {
                root = fs.getPath(normalizedPath);
            }
            if (Files.isDirectory(root)) {
                materializeTemplateRoot(root, targetRoot, false);
            }
        } finally {
            if (fs != null && fs.isOpen()) {
                fs.close();
            }
        }
    }

    private static final class TemplateResolution {
        private final String codegenPath;
        private final String listPath;

        private TemplateResolution(String codegenPath, String listPath) {
            this.codegenPath = codegenPath;
            this.listPath = listPath;
        }
    }

    private List<String> listTemplateFiles(String templatePath, String stylePath) throws IOException {
        if (isBlank(templatePath) || isBlank(stylePath)) {
            return Collections.emptyList();
        }
        String normalizedPath = templatePath.startsWith("/") ? templatePath.substring(1) : templatePath;
        String styleDir = stylePath.replace(".", "/");
        Path templateRoot = Paths.get(templatePath);
        if (Files.exists(templateRoot)) {
            return listTemplateFilesFromRoot(templateRoot, styleDir);
        }
        URL url = CodegenExecutor.class.getClassLoader().getResource(normalizedPath);
        if (url == null) {
            throw new IllegalArgumentException("templatePath not found: " + templatePath);
        }
        try {
            URI uri = url.toURI();
            if ("jar".equalsIgnoreCase(uri.getScheme())) {
                FileSystem fs = null;
                try {
                    try {
                        fs = FileSystems.newFileSystem(uri, Collections.emptyMap());
                    } catch (java.nio.file.FileSystemAlreadyExistsException ex) {
                        fs = FileSystems.getFileSystem(uri);
                    }
                    Path root = fs.getPath("/" + normalizedPath);
                    if (!Files.exists(root)) {
                        root = fs.getPath(normalizedPath);
                    }
                    return listTemplateFilesFromRoot(root, styleDir);
                } finally {
                    if (fs != null && fs.isOpen()) {
                        fs.close();
                    }
                }
            }
            return listTemplateFilesFromRoot(Paths.get(uri), styleDir);
        } catch (Exception e) {
            throw new IOException("Failed to resolve templatePath: " + templatePath, e);
        }
    }

    private List<String> listTemplateFilesFromRoot(Path templateRoot, String styleDir) throws IOException {
        Path styleRoot = templateRoot.resolve(styleDir);
        if (!styleRoot.isAbsolute()) {
            styleRoot = styleRoot.toAbsolutePath().normalize();
        }
        if (!Files.isDirectory(styleRoot)) {
            throw new IllegalArgumentException("template style path not found: " + styleRoot);
        }
        List<String> out = new ArrayList<>();
        try (Stream<Path> stream = Files.walk(styleRoot, FileVisitOption.FOLLOW_LINKS)) {
            for (Path path : (Iterable<Path>) stream::iterator) {
                if (!Files.isRegularFile(path)) {
                    continue;
                }
                Path current = path;
                if (!current.isAbsolute()) {
                    current = current.toAbsolutePath();
                }
                Path rel = styleRoot.relativize(current);
                String relStr = rel.toString().replace('\\', '/');
                out.add(relStr);
            }
        }
        return out;
    }

    private boolean shouldIncludeVueStyle(String relPath, String vueStyle) {
        if (isBlank(vueStyle)) {
            return true;
        }
        String[] segments = relPath.replace('\\', '/').split("/");
        for (String segment : segments) {
            if ("vue3Native".equals(segment)) {
                return "vue3Native".equalsIgnoreCase(vueStyle);
            }
            if ("vue3".equals(segment)) {
                return "vue3".equalsIgnoreCase(vueStyle);
            }
            if ("vue".equals(segment)) {
                return "vue".equalsIgnoreCase(vueStyle);
            }
        }
        return true;
    }

    private List<String> expandSubTablePaths(String relPath, List<CodegenSpec.SubTableSpec> subTables) {
        if (relPath == null || !relPath.contains("[1-n]")) {
            return Collections.singletonList(relPath);
        }
        if (subTables == null || subTables.isEmpty()) {
            return Collections.emptyList();
        }
        List<String> out = new ArrayList<>();
        for (CodegenSpec.SubTableSpec sub : subTables) {
            String name = sub != null ? sub.getEntityName() : null;
            if (isBlank(name)) {
                continue;
            }
            out.add(relPath.replace("[1-n]", name.trim()));
        }
        return out;
    }

    private String renderTemplatePath(String path, String bussiPackage, String entityPackage, String entityName, String currentDate) {
        if (path == null) {
            return null;
        }
        String rendered = path;
        if (bussiPackage != null) {
            rendered = rendered.replace("${bussiPackage}", bussiPackage.replace(".", "/"));
        }
        if (entityPackage != null) {
            rendered = rendered.replace("${entityPackage}", entityPackage.replace(".", "/"));
        }
        if (entityName != null) {
            rendered = rendered.replace("${entityName}", entityName);
        }
        if (currentDate != null) {
            rendered = rendered.replace("${currentDate}", currentDate);
        }
        return rendered;
    }

    private String normalizeTemplateExtension(String path) {
        if (path == null) {
            return null;
        }
        if (path.endsWith(".javai")) {
            return path.substring(0, path.length() - 1);
        }
        if (path.endsWith(".vuei")) {
            return path.substring(0, path.length() - 1);
        }
        if (path.endsWith(".tsi")) {
            return path.substring(0, path.length() - 1);
        }
        return path;
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
