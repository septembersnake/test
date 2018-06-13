package com.darker.myapplication.Structure;

import java.util.HashMap;
import java.util.Map;

public class MaintainStructure {
    public String day;
    public String label;
    public int price;
    public String trip;
    public String type;
    public String user_id;

    public MaintainStructure() {
        // Default constructor required for calls to DataSnapshot.getValue(MaintainStructure.class)
    }

    public MaintainStructure(String day,String label,int price,String trip,String type, String user_id) {
        this.day = day;
        this.user_id= user_id;
        this.price = price;
        this.trip = trip;
        this.type = type;
        this.label = label;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("day", day);
        result.put("label", label);
        result.put("price", price);
        result.put("trip", trip);
        result.put("type", type);
        result.put("user_id", user_id);
        return result;
    }
}
