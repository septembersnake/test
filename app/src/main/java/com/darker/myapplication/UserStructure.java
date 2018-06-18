package com.darker.myapplication;

import java.util.HashMap;
import java.util.Map;

public class UserStructure {
    public String email;
    public String password;
    public String user_id;

    public UserStructure() {
        // Default constructor required for calls to DataSnapshot.getValue(MaintainStructure.class)
    }

    public UserStructure(String email,String password,String user_id) {
        this.email = email;
        this.password = password;
        this.user_id= user_id;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("email", email);
        result.put("password", password);
        result.put("user_id", user_id);
        return result;
    }
}
