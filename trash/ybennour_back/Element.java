package ybennour_back;

import java.util.List;

/**
 * Created by Youssef on 25/02/16.
 */
public class Element {

    public String key;

    public int index;

    public Element parent;

    public List<Element> children;

    public Element(String key, Element parent, int index, List<Element> children) {
        this.key = key;
        this.parent = parent;
        this.index = index;
        this.children = children;
    }
}
