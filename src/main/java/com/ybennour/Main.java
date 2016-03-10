package com.ybennour;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {

    public static void main(String[] args) {
        // bouchon
        String[] array = {"bin/sample.json"};
        args = array;


	    if (args.length < 1) {
            throw new RuntimeException("Command error : missing argument");
        }

        File file = new File(args[0]);
        JsonNode root;
        try {
            root = readFile(file);
        } catch (IOException e) {
            throw new RuntimeException("file not valid", e);
        }

        Core core = new Core();
        core.exploreJson(root);

    }

    private static JsonNode readFile(File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(file);
        return node;
    }
}
