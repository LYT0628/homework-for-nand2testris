package icu.lyt;

import java.util.HashMap;

public class SegmentMap extends HashMap<String,String> {
    private SegmentMap(){
        super();
        put("constant","CONSTANT");
        put("local","LCL");
        put("argument","ARG");
        put("this","THIS");
        put("that","THAT");
//        put("temp","TEMP");
//        put("pointer","POINTER");
        put("temp","5");
        put("pointer","3");
    }
    private static final SegmentMap instance = new SegmentMap();
    public static SegmentMap getIstance(){
        return instance;
    }
}
