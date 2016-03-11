package com.ybennour.jsonToExcel;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ybennour.jsonToExcel.process.Core;
import com.ybennour.jsonToExcel.process.JSONExplorer;

/**
 * Created by Youssef BENNOUR on 25/02/16.
 */
public class Main {
    final static private Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        if (args.length < 1) {
            log.error("Command error : missing argument");
            return;
        }

        File file = new File(args[0]);

        if (!file.exists()) {
            log.error("File does not exist");
            return;
        }

        Core core = new Core();

        core.exploreJson(file);
        log.info("JSON file explored");

        File csvFile = initOutputFile();
        core.writeCSV(csvFile);
        log.info("output.csv created");
    }

    /**
     * create new file
     * 
     * @return created file
     */
    private static File initOutputFile() {
        File file = new File("output.csv");
        if (file.exists()) {
            file.delete();
        }

        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return file;
    }
}
