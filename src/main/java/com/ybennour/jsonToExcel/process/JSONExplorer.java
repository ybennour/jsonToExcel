package com.ybennour.jsonToExcel.process;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.collections.IteratorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;

public class JSONExplorer {

    final private Logger log = LoggerFactory.getLogger(JSONExplorer.class);

    private int colIndex = 1;

    private int generalOffset = 0;

    /**
     * structure map contains : - key : path of leafs - value : index of column
     */
    private Map<String, Integer> structure = new HashMap<String, Integer>();

    /**
     * Data map contains : - key : Position(line, col) - value : Content 
     */
    private Map<Position, Object> data = new TreeMap<Position, Object>();

    public void exploreJson(JsonNode rootNode) {
        explore("", rootNode, 0);
        structure = sortByValue(structure);

        // printStructure();
        // printDataRecur(this.data);
    }

    public int explore(String path, JsonNode node, int lineIndex) {
        if (generalOffset > 0) {
            lineIndex = lineIndex + generalOffset;
            generalOffset = 0;
        }

        if (node.isObject()) {
            int offset = 0;
            Iterator<Map.Entry<String, JsonNode>> iterator = node.fields();
            while (iterator.hasNext()) {
                Map.Entry<String, JsonNode> entry = iterator.next();

                String childPath = path.isEmpty() ? entry.getKey() : path + "." + entry.getKey();
                offset = offset + explore(childPath, entry.getValue(), lineIndex);

                log.debug("offset : " + offset);
            }
            generalOffset = generalOffset + offset;
        } else if (node.isArray()) {
            int offset = 0;
            List<JsonNode> arrayItemsList = IteratorUtils.toList(node.elements());
            boolean first = true;
            for (JsonNode arrayNode : arrayItemsList) {
                if (first) {
                    first = false;
                } else {
                    log.debug(">> lineIndex++");
                    lineIndex++;
                    offset++;
                }
                explore(path, arrayNode, lineIndex);
            }
            return offset;
        } else if (node.isValueNode()) {
            int col = 0;
            if (!structure.containsKey(path)) {
                structure.put(path, colIndex);
                col = colIndex;
                colIndex++;
            } else {
                col = structure.get(path);
            }
            Position position = new Position(lineIndex, col);
            if (data.containsKey(position)) {
                log.warn("Program will override data in " + lineIndex + "." + col);
            }
            data.put(position, node.textValue());
            log.debug(node.asText() + " - " + lineIndex + "." + col);
        }
        return 0;
    }

    public Map<String, Integer> getStructure() {
        return structure;
    }

    public Map<Position, Object> getData() {
        return data;
    }

    // for debug
    private void printStructure() {
        for (Map.Entry<String, Integer> entry : structure.entrySet()) {
            System.out.println(entry.getKey() + " -  " + entry.getValue());
        }
    }

    // for debug
    private void printDataRecur(Map<Position, Object> data) {
        for (Map.Entry<Position, Object> entry : data.entrySet()) {
            if (entry.getValue() instanceof String) {
                System.out.println(entry.getKey().line + "." + entry.getKey().col + " : " + entry.getValue());
            } else if (entry.getValue() instanceof Map) {
                printDataRecur((Map<Position, Object>) entry.getValue());
            }

        }
    }

    public <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
}
