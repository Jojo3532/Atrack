package com.jojo.atrack;

import java.text.SimpleDateFormat;

public class cls {

    public Boolean isNotEmpty(String text){
        if (text.trim().length() == 0) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean isValidDate(String dt){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            sdf.parse(dt);
            return false;
        } catch(Exception ee) {
            return false;
        }
    }

    public static String Uname;
}
