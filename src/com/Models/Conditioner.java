package com.Models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Conditioner {
    boolean subMetro;
    Map<String, ArrayList<String>> mapper = new HashMap<>();
    public Conditioner (){}

    public boolean getSubMetro() {
        return subMetro;
    }

    public void setSubMetro(Boolean subMetro) {
        this.subMetro = subMetro;
    }

    public Map<String, ArrayList<String>> getMapper() {
        return mapper;
    }

    public void setMapper(Map<String, ArrayList<String>> mapper) {
        this.mapper = mapper;
    }
}
