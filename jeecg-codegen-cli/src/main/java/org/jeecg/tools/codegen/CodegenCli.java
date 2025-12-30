package org.jeecg.tools.codegen;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.OutputStream;
import java.util.Locale;
import java.util.Properties;
import java.io.InputStream;

public final class CodegenCli {
    private static final Properties CONFIG = loadConfig();

    private CodegenCli() {}

    public static void main(String[] args) throws Exception {
        Args options = Args.parse(args);
        if (options == null || (options.input == null && options.ddl == null)) {
            Args.printUsage();
            System.exit(2);
        }

        if (options.ddl != null) {
            String ddl = new String(Files.readAllBytes(Paths.get(options.ddl)), StandardCharsets.UTF_8);
            DdlSpecMapper.Options mapOptions = new DdlSpecMapper.Options();
            if (options.jspMode != null && !options.jspMode.trim().isEmpty()) {
                mapOptions.jspMode = options.jspMode.trim();
            }
            mapOptions.projectPath = resolveProjectPath(options.output);
            if (options.bussiPackage != null && !options.bussiPackage.trim().isEmpty()) {
                mapOptions.bussiPackage = options.bussiPackage.trim();
            }
            if (options.entityPackage != null && !options.entityPackage.trim().isEmpty()) {
                mapOptions.entityPackage = options.entityPackage.trim();
            }
            if (options.fieldRowNum != null) {
                mapOptions.fieldRowNum = options.fieldRowNum;
            }

            CodegenSpec spec = DdlSpecMapper.fromDdl(ddl, mapOptions);
            applyDefaults(spec, options);
            if (options.specOut != null && !options.specOut.trim().isEmpty()) {
                writeYaml(spec, Paths.get(options.specOut));
            } else {
                Path out = defaultSpecOutPath(spec);
                writeYaml(spec, out);
            }
            return;
        }

        Path input = Paths.get(options.input);
        CodegenSpec spec = SpecLoader.load(input);
        applyDefaults(spec, options);
        if (options.templatePath != null && !options.templatePath.trim().isEmpty()) {
            spec.setTemplatePath(options.templatePath.trim());
        }

        CodegenExecutor executor = new CodegenExecutor(spec);
        executor.run();
    }

    private static void writeYaml(CodegenSpec spec, OutputStream out) throws Exception {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.writeValue(out, spec);
    }

    private static void writeYaml(CodegenSpec spec, Path out) throws Exception {
        try (OutputStream stream = Files.newOutputStream(out)) {
            writeYaml(spec, stream);
        }
    }

    private static void applyDefaults(CodegenSpec spec, Args options) {
        if (spec == null) {
            return;
        }
        if (isNotBlank(options.output)) {
            spec.setProjectPath(options.output.trim());
        }
        if (isBlank(spec.getProjectPath())) {
            String cfg = CONFIG.getProperty("project_path");
            spec.setProjectPath(isNotBlank(cfg) ? cfg.trim() : resolveProjectPath(null));
        }
        if (isNotBlank(options.bussiPackage)) {
            spec.setBussiPackage(options.bussiPackage.trim());
        }
        if (isBlank(spec.getBussiPackage())) {
            String cfg = CONFIG.getProperty("bussi_package");
            spec.setBussiPackage(isNotBlank(cfg) ? cfg.trim() : "org.jeecg.modules");
        }
        if (isBlank(spec.getSourceRootPackage())) {
            String cfg = CONFIG.getProperty("source_root_package");
            if (isNotBlank(cfg)) {
                spec.setSourceRootPackage(cfg.trim());
            }
        }
        if (isBlank(spec.getWebRootPackage())) {
            String cfg = CONFIG.getProperty("web_root_package");
            if (isBlank(cfg)) {
                cfg = CONFIG.getProperty("webroot_package");
            }
            if (isNotBlank(cfg)) {
                spec.setWebRootPackage(cfg.trim());
            }
        }
        if (spec.getTable() != null) {
            if (isNotBlank(options.entityPackage)) {
                spec.getTable().setEntityPackage(options.entityPackage.trim());
            }
            if (isBlank(spec.getTable().getEntityPackage())) {
                spec.getTable().setEntityPackage(deriveEntityPackage(spec.getTable().getTableName(), spec.getTable().getEntityName()));
            }
        }
        if (isNotBlank(options.frontendRoot)) {
            spec.setFrontendRoot(options.frontendRoot.trim());
        }
        if (isBlank(spec.getFrontendRoot())) {
            String cfg = CONFIG.getProperty("frontend_root");
            String frontendRoot = isNotBlank(cfg) ? cfg.trim() : resolveFrontendRoot(null);
            if (frontendRoot != null && !frontendRoot.isEmpty()) {
                spec.setFrontendRoot(frontendRoot);
            }
        }
    }

    private static String resolveProjectPath(String output) {
        if (output != null && !output.trim().isEmpty()) {
            return output.trim();
        }
        Path defaultPath = Paths.get("jeecg-boot/jeecg-module-system/jeecg-system-biz");
        if (Files.isDirectory(defaultPath)) {
            return defaultPath.toAbsolutePath().normalize().toString();
        }
        return Paths.get(".").toAbsolutePath().normalize().toString();
    }

    private static String resolveFrontendRoot(String frontendRoot) {
        if (frontendRoot != null && !frontendRoot.trim().isEmpty()) {
            return frontendRoot.trim();
        }
        Path defaultPath = Paths.get("ant-design-vue-jeecg/src/views");
        if (Files.isDirectory(defaultPath)) {
            return defaultPath.toAbsolutePath().normalize().toString();
        }
        return null;
    }

    private static Path defaultSpecOutPath(CodegenSpec spec) throws Exception {
        String specOutDir = CONFIG.getProperty("spec_out_dir");
        Path base;
        if (isNotBlank(specOutDir)) {
            Path configured = Paths.get(specOutDir.trim());
            if (configured.isAbsolute()) {
                base = configured;
            } else {
                base = Paths.get(spec.getProjectPath()).resolve(configured);
            }
        } else {
            base = Paths.get(spec.getProjectPath()).resolve("codegen-specs");
        }
        Files.createDirectories(base);
        String tableName = spec.getTable() != null ? spec.getTable().getTableName() : null;
        String fileName = isNotBlank(tableName) ? tableName + ".yaml" : "codegen-spec.yaml";
        return base.resolve(fileName);
    }

    private static String deriveEntityPackage(String tableName, String entityName) {
        if (tableName == null || tableName.trim().isEmpty()) {
            return lowerCamel(entityName, "demo");
        }
        String value = tableName.trim();
        int idx = value.indexOf('_');
        if (idx > 0) {
            return value.substring(0, idx).toLowerCase(Locale.ROOT);
        }
        return lowerCamel(entityName, "demo");
    }

    private static String lowerCamel(String name, String fallback) {
        if (name == null || name.trim().isEmpty()) {
            return fallback;
        }
        String n = name.trim();
        return Character.toLowerCase(n.charAt(0)) + n.substring(1);
    }

    private static Properties loadConfig() {
        Properties p = new Properties();
        try (InputStream in = CodegenCli.class.getClassLoader().getResourceAsStream("jeecg/jeecg_config.properties")) {
            if (in != null) {
                p.load(in);
            }
        } catch (Exception ignored) {
        }
        return p;
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static boolean isNotBlank(String s) {
        return s != null && !s.trim().isEmpty();
    }

    static final class Args {
        String input;
        String ddl;
        String output;
        String templatePath;
        String specOut;
        String jspMode;
        String bussiPackage;
        String entityPackage;
        Integer fieldRowNum;
        String frontendRoot;

        static Args parse(String[] args) {
            Args options = new Args();
            if (args == null) {
                return options;
            }
            for (int i = 0; i < args.length; i++) {
                String arg = args[i];
                if ("--input".equals(arg) || "-i".equals(arg)) {
                    if (i + 1 < args.length) {
                        options.input = args[++i];
                    }
                } else if ("--ddl".equals(arg)) {
                    if (i + 1 < args.length) {
                        options.ddl = args[++i];
                    }
                } else if ("--output".equals(arg) || "-o".equals(arg)) {
                    if (i + 1 < args.length) {
                        options.output = args[++i];
                    }
                } else if ("--template".equals(arg) || "-t".equals(arg)) {
                    if (i + 1 < args.length) {
                        options.templatePath = args[++i];
                    }
                } else if ("--spec-out".equals(arg)) {
                    if (i + 1 < args.length) {
                        options.specOut = args[++i];
                    }
                } else if ("--jsp-mode".equals(arg)) {
                    if (i + 1 < args.length) {
                        options.jspMode = args[++i];
                    }
                } else if ("--bussi-package".equals(arg)) {
                    if (i + 1 < args.length) {
                        options.bussiPackage = args[++i];
                    }
                } else if ("--entity-package".equals(arg)) {
                    if (i + 1 < args.length) {
                        options.entityPackage = args[++i];
                    }
                } else if ("--field-row-num".equals(arg)) {
                    if (i + 1 < args.length) {
                        options.fieldRowNum = Integer.parseInt(args[++i]);
                    }
                } else if ("--frontend-root".equals(arg)) {
                    if (i + 1 < args.length) {
                        options.frontendRoot = args[++i];
                    }
                } else if ("--help".equals(arg) || "-h".equals(arg)) {
                    return null;
                }
            }
            return options;
        }

        static void printUsage() {
            System.err.println("Usage:");
            System.err.println("  java -jar jeecg-codegen-cli.jar --input <spec.yaml|json> [--output <path>] [--template <templatePath>]");
            System.err.println("  java -jar jeecg-codegen-cli.jar --ddl <ddl.sql> [--spec-out <spec.yaml>] [--output <projectPath>]");
            System.err.println("    [--jsp-mode one|tree|many|jvxe|erp|innerTable|tab] [--bussi-package <pkg>] [--entity-package <pkg>]");
            System.err.println("    [--field-row-num <n>] [--frontend-root <path>]");
            System.err.println("  note: when --spec-out is omitted, output path can be configured by jeecg/jeecg_config.properties (spec_out_dir)");
        }
    }
}
