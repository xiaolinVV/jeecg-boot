package org.jeecg.tools.codegen;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class CodegenCli {
    private CodegenCli() {}

    public static void main(String[] args) throws Exception {
        Args options = Args.parse(args);
        if (options == null || options.input == null) {
            Args.printUsage();
            System.exit(2);
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

    static final class Args {
        String input;
        String output;
        String templatePath;

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
                } else if ("--output".equals(arg) || "-o".equals(arg)) {
                    if (i + 1 < args.length) {
                        options.output = args[++i];
                    }
                } else if ("--template".equals(arg) || "-t".equals(arg)) {
                    if (i + 1 < args.length) {
                        options.templatePath = args[++i];
                    }
                } else if ("--help".equals(arg) || "-h".equals(arg)) {
                    return null;
                }
            }
            return options;
        }

        static void printUsage() {
            System.err.println("Usage: java -jar jeecg-codegen-cli.jar --input <spec.yaml|json> [--output <path>] [--template <templatePath>]");
        }
    }
}
