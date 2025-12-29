package org.jeecg.tools.codegen;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.OutputStream;

public final class CodegenCli {
    private CodegenCli() {}

    public static void main(String[] args) throws Exception {
        Args options = Args.parse(args);
        if (options == null || (options.input == null && options.ddl == null)) {
            Args.printUsage();
            System.exit(2);
        }

        if (options.ddl != null) {
            String ddl = Files.readString(Paths.get(options.ddl), StandardCharsets.UTF_8);
            DdlSpecMapper.Options mapOptions = new DdlSpecMapper.Options();
            if (options.jspMode != null && !options.jspMode.trim().isEmpty()) {
                mapOptions.jspMode = options.jspMode.trim();
            }
            if (options.output != null && !options.output.trim().isEmpty()) {
                mapOptions.projectPath = options.output.trim();
            }
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
            if (options.specOut != null && !options.specOut.trim().isEmpty()) {
                writeYaml(spec, Paths.get(options.specOut));
            } else {
                writeYaml(spec, System.out);
            }
            return;
        }

        Path input = Paths.get(options.input);
        CodegenSpec spec = SpecLoader.load(input);
        if (options.output != null && !options.output.trim().isEmpty()) {
            spec.setProjectPath(options.output.trim());
        }
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
            System.err.println("    [--field-row-num <n>]");
        }
    }
}
