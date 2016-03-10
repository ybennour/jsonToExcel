package com.ybennour.jsonToExcel.process;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by Youssef BENNOUR on 25/02/16.
 */
public class Core {
    final private Logger log = LoggerFactory.getLogger(Core.class);

    public JSONExplorer exploreJson(File file) {
        JsonNode rootNode;
        try {
            rootNode = readFile(file);
        } catch (IOException e) {
            throw new RuntimeException("file not valid", e);
        }

        JSONExplorer explorer = new JSONExplorer();
        explorer.exploreJson(rootNode);

        return explorer;
    }

    public void writeCSV(JSONExplorer explorer, File csvFile) {
        CSVHelper csvHelper = new CSVHelper(explorer.getStructure(), explorer.getData());
        csvHelper.writeCsv(csvFile);
    }

    private JsonNode readFile(File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(file);
        return node;
    }

}
