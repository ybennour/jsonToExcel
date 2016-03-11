package com.ybennour.jsonToExcel.process;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by Youssef BENNOUR on 25/02/16.
 */
public class Core {

    private JSONExplorer jsonExplorer = new JSONExplorer();

    public void exploreJson(File file) {
        JsonNode rootNode;
        try {
            rootNode = readFile(file);
        } catch (IOException e) {
            throw new RuntimeException("file not valid", e);
        }

        jsonExplorer.exploreJson(rootNode);

    }

    public void writeCSV(File csvFile) {
        CSVHelper csvHelper = new CSVHelper(jsonExplorer.getStructure(), jsonExplorer.getData());
        csvHelper.writeCsv(csvFile);
    }

    private JsonNode readFile(File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(file);
        return node;
    }

}
