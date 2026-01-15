package org;

import org.apache.commons.io.FileUtils;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Main {
    final static String usage = """
            migrateit --project=/some/path/to/project/directory --mappings=/path/to/json/mappings.json
            json mappings must be a json file with an object:
            {
                "old_test_id" : "new_test_id",
                "old_test_id" : "new_test_id",
                "old_test_id" : "new_test_id"
            }
            """;

    public static void main(String[] args) throws Exception {
        if (args.length == 0)
            throw new Exception("""
                    Cannot run with empty arguments list, usage:
                    %s
                    """.formatted(usage));
        var projectPathFull = Arrays.stream(args)
                .filter(string -> string.contains("--project"))
                .findFirst()
                .orElseThrow(() -> new Exception("""
                        --project key was not found in arguments list, usage:
                        %s
                        """.formatted(usage))
                );
        var projectPath = extractPath(projectPathFull);

        var mappingsJsonPathFull = Arrays.stream(args)
                .filter(string -> string.contains("--mappings"))
                .findFirst()
                .orElseThrow(() -> new Exception("""
                        --mappings key was not found in arguments list, usage:
                        %s
                        """.formatted(usage)));
        var mappingsPath = extractPath(mappingsJsonPathFull);

        var projectFolder = new File(projectPath);
        if (!projectFolder.isDirectory())
            throw new Exception("Project path is not a directory");

        var mappingsJsonFile = new File(mappingsPath);
        if (!mappingsJsonFile.isFile())
            throw new Exception("Mappings path provided is not a file");

        final var mappings = readMappingsFile(mappingsJsonFile);

        processFolder(projectFolder, mappings);
    }

    static String extractPath(String unsplitPath) throws Exception {
        var splitPath = unsplitPath.split("=");
        if (splitPath.length != 2) throw new Exception("""
                Failed to split string into key&value pair, usage:
                %s
                """.formatted(usage));
        if (splitPath[1].isEmpty()) throw new Exception("""
                Empty value provided, usage:
                %s
                """.formatted(usage));
        return splitPath[1];
    }

    static Map<String, String> readMappingsFile(File mappingsFile) throws Exception {

        final ObjectMapper objectMapper = new ObjectMapper();
        var rootNode = objectMapper.readTree(mappingsFile);
        if (!rootNode.isObject()) throw new Exception("""
                Root node of json file must be an object, usage:
                %s
                """.formatted(usage));
        var objectNode = (ObjectNode) rootNode;

        var map = new HashMap<String, String>();

        for (var node : objectNode.properties())
            map.put(node.getKey(), node.getValue().asString());

        return map;
    }

    static void processFolder(File projectDirectory, Map<String, String> mappings) throws IOException {

        for (var file : Objects.requireNonNull(projectDirectory.listFiles())) {
            if (file.isFile()) {
                if (file.getName().contains(".java"))
                    replaceEntries(file, mappings);
            } else if (file.isDirectory())
                processFolder(file, mappings);
        }
    }

    static void replaceEntries(File sourceFile, Map<String, String> mappings) throws IOException {
        var sourceFileContents = FileUtils.readFileToString(sourceFile, "UTF-8");
        for (var pair : mappings.entrySet())
            sourceFileContents = sourceFileContents.replace(pair.getKey(), pair.getValue());

        FileUtils.write(sourceFile, sourceFileContents, "UTF-8", false);
    }
}