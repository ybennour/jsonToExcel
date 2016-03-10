package com.ybennour;

import java.util.*;

import org.apache.commons.collections.IteratorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Created by Youssef BENNOUR on 25/02/16.
 */
public class Core {
	final private Logger log = LoggerFactory.getLogger(Core.class);

	private static int colIndex = 1;

	/*
	* structure map contains :
	* 	- key : path of leafs
	*   - value : index of column
	*/
	private Map<String, Integer> structure = new HashMap<String, Integer>();

    private Map<Position, Object> data = Collections.synchronizedSortedMap(new TreeMap<Position, Object>());

	public void exploreJson(JsonNode rootNode) {
		explore("", rootNode, 2);

		//debug
		printStructure();

        printDataRecur(data);
	}

    private void explore(String path, JsonNode node, int lineIndex) {
		if (node.isObject()) {
			Iterator<Map.Entry<String, JsonNode>> iterator = node.fields();
			while (iterator.hasNext()) {
				Map.Entry<String, JsonNode> entry = iterator.next();

				String childPath = path.isEmpty() ? entry.getKey() : path + "." + entry.getKey();
				explore(childPath, entry.getValue(), lineIndex);
			}

		} else if (node.isArray()) {
			List<JsonNode> arrayItemsList = IteratorUtils.toList(node.elements());
            boolean first = true;
			for (JsonNode arrayNode : arrayItemsList) {
                if (first) {
                    first = false;
                } else {
                    lineIndex++;
                }
				explore(path, arrayNode, lineIndex);
			}
		} else if (node.isValueNode()) {
			if (!structure.containsKey(path)) {
				structure.put(path, colIndex);
                data.put(new Position(lineIndex, colIndex), node.textValue());
                log.debug(node.asText() + " - " + lineIndex + "." + colIndex);
				colIndex++;
			} else {
                data.put(new Position(lineIndex, structure.get(path)), node.textValue());
                log.debug(node.asText() + " - " + lineIndex + "." + colIndex);
            }
		}
	}

	private void printStructure() {
		for (Map.Entry<String, Integer> entry : structure.entrySet()) {
			System.out.println(entry.getKey() + " -  " + entry.getValue());
		}
	}

    private void printDataRecur(Map<Position, Object> data) {
        for (Map.Entry<Position, Object> entry : data.entrySet()) {
            if (entry.getValue() instanceof String) {
                System.out.println(entry.getKey().line + "." + entry.getKey().col + " : " + entry.getValue());
            } else if (entry.getValue() instanceof Map) {
                printDataRecur((Map<Position, Object>) entry.getValue());
            }

        }
    }

    private class Position implements Comparable<Position> {
        int line;
        int col;

        public Position(int line, int col) {
            this.line = line;
            this.col = col;
        }

        public int compareTo(Position o) {
            if (this.line > o.line) {
                return 1;
            } else if (this.line < o.line) {
                return -1;
            } else {
                if (this.col > o.col) {
                    return 1;
                } else if (this.col < o.col) {
                    return -1;
                }
                return 0;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Position) {
                return this.col == ((Position) o).col && this.line == ((Position) o).line;
            }
            return false;
        }
    }
}
