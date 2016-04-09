package com.asdfgaems.core;

import java.util.HashMap;
import java.util.Map;

public class AiMarker {
    private String name;
    private Map <String, String> parameters;

    public AiMarker(String name) {
        this.name = name;
        parameters = new HashMap<String, String>();
    }

    public void addParam(String name, String val) {
        parameters.put(name, val);
    }
    public String getParam(String name) {
        return parameters.get(name);
    }
}
