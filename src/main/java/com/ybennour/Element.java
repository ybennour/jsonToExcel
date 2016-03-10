package com.ybennour;

import java.util.List;

/**
 * Created by Youssef on 25/02/16.
 */
public class Element {

    public String key;

    public int index;

    public Element parent;

    public List<Element> children;
    
    public boolean isArray;

    public Element(String key, Element parent, int index, List<Element> children, boolean isArray) {
        this.key = key;
        this.parent = parent;
        this.index = index;
        this.isArray = isArray;
        this.children = children;
    }
    
    public boolean isInArray() {
    	return parent.isArray;
    }
    
    @Override
    public String toString() {
    	return key + " " + String.valueOf(index);
    }
}
