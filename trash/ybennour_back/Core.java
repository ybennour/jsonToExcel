package ybennour_back;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Youssef on 25/02/16.
 */
public class Core {

    private static int index = 0;

    public void exploreJson(JsonNode root) {

    }

    private void exploreNode(String nodeKey, JsonNode node, Element parentElement) {
        if (node.isObject()) {
            Element element = new Element(nodeKey, parentElement, 0, new ArrayList<Element>());

            Iterator<Map.Entry<String, JsonNode>> iterator = node.fields();
            while (iterator.hasNext()) {
                Map.Entry<String, JsonNode> entry = iterator.next();
                exploreNode(entry.getKey(), entry.getValue(), element);
            }
        } else if (node.isArray()) {
            Element element = new Element(parentElement, 0, new ArrayList<Element>());

            for (JsonNode child : node) {
                exploreNode(child, element);
            }
        } else if (node.isValueNode()) {
            Element element = new Element(parentElement, index, null);
            index++;
        }
    }
}
