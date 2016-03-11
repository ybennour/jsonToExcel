package com.ybennour.jsonToExcel.process;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class CSVHelper {

    private static final String COL_SEP = ";";

    private static final String LINE_SEP = "\r\n";

    private Map<String, Integer> structure;

    private Map<Position, Object> data;

    public CSVHelper(Map<String, Integer> structure, Map<Position, Object> data) {
        this.structure = structure;
        this.data = data;
    }

    public void writeCsv(File csvFile) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(csvFile));
            String titles = "";
            // write first line
            for (Map.Entry<String, Integer> entry : structure.entrySet()) {
                String title = getLastKey(entry.getKey());
                titles = titles.isEmpty() ? title : titles + COL_SEP + title;
            }
            writer.write(titles);
            writer.write(LINE_SEP);

            // write data
            writeData(data, writer);
        } catch (IOException e) {
            throw new RuntimeException("problem while writing CSV file", e);
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
            }
        }
    }

    private String getLastKey(String path) {
        return path.contains(".") ? path.substring(path.lastIndexOf(".") + 1) : path;
    }

    private void writeData(Map<Position, Object> data, BufferedWriter writer) throws IOException {
        for (int i = 0; i < getMaxLine() + 1; i++) {
            Map<Position, Object> dataLine = getDataLine(this.data, i);

            writeLine(dataLine, writer);
            writer.write(LINE_SEP);
        }
    }

    private void writeLine(Map<Position, Object> dataLine, BufferedWriter writer) throws IOException {
        String line = "";
        boolean first = true;
        for (int i = 1; i < structure.size() + 1; i++) {
            String datium = getDatiumForColumnIndex(dataLine, i);
            if (first) {
                line = datium;
                first = false;
            } else {
                line = line + COL_SEP + datium;
            }
        }

        writer.write(line);
    }

    private String getDatiumForColumnIndex(Map<Position, Object> dataLine, int index) {
        for (Map.Entry<Position, Object> entry : dataLine.entrySet()) {
            if (entry.getKey().col == index) {
                return (String) entry.getValue();
            }
        }
        return "";
    }

    private Map<Position, Object> getDataLine(Map<Position, Object> data, int lineIndex) {
        Map<Position, Object> dataLine = new TreeMap<Position, Object>();
        for (Map.Entry<Position, Object> entry : data.entrySet()) {
            if (entry.getValue() instanceof String) {
                if (entry.getKey().line == lineIndex) {
                    dataLine.put(entry.getKey(), entry.getValue());
                }
            } else if (entry.getValue() instanceof Map) {
                dataLine.putAll(getDataLine((Map<Position, Object>) entry.getValue(), lineIndex));
            }
        }
        return dataLine;
    }

    private int getMaxLine() {
        int max = 0;
        for (Map.Entry<Position, Object> entry : data.entrySet()) {
            if (entry.getKey().line > max) {
                max = entry.getKey().line;
            }
        }
        return max;
    }

}
