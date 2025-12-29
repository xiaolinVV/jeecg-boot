package org.jeecg.tools.codegen;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.IOException;
import java.nio.file.Path;

final class SpecLoader {
    private SpecLoader() {}

    static CodegenSpec load(Path input) throws IOException {
        String name = input.getFileName().toString().toLowerCase();
        ObjectMapper mapper = name.endsWith(".yml") || name.endsWith(".yaml")
            ? new ObjectMapper(new YAMLFactory())
            : new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.readValue(input.toFile(), CodegenSpec.class);
    }
}
