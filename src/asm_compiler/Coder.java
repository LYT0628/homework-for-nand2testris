package src.asm_compiler;

import java.util.Map;

public class Coder {
    public static Map<String, String> destMap;
    public static Map<String, String> compMap;
    public static Map<String, String> jumpMap;
    Coder(){
        destMap.put("","000");
        destMap.put("M","001");
        destMap.put("D","010");
        destMap.put("MD","011");
        destMap.put("A","100");
        destMap.put("AM","101");
        destMap.put("AD","110");
        destMap.put("AMD","111");

        compMap.put("0","101010");
        compMap.put("1","111111");
        compMap.put("-1","111010");
        compMap.put("D","001100");
        compMap.put("A","110000");
        compMap.put("!D","001101");
        compMap.put("!A","110001");
        compMap.put("-D","001111");
        compMap.put("-A","110011");
        compMap.put("D+1","011111");
        compMap.put("A+1","110111");
        compMap.put("D-1","001110");
        compMap.put("A-1","110010");
        compMap.put("D+A","000010");
        compMap.put("D-A","010011");
        compMap.put("A-D","000111");
        compMap.put("D&A","000000");
        compMap.put("D|A","010101");

        jumpMap.put("","000");
        jumpMap.put("JGT","001");
        jumpMap.put("JEQ","010");
        jumpMap.put("JGE","011");
        jumpMap.put("GLT","100");
        jumpMap.put("JNE","101");
        jumpMap.put("JLE","110");
        jumpMap.put("JMP","111");
    }


    public String dest(String d){
        return  destMap.get(d);
    }
    public String comp(String c){
        String a = !c.contains("M")?"0":"1";
        c = c.replace("M", "A");
        return compMap.get(c);
    }
    public String jump(String j){
        return jumpMap.get(j);
    }

    public String int_to_bin_16(Integer integer){
        /*
        *
        * def int_to_bin_16(integer):
                return str(bin(integer))[2:].zfill(16)
         */
        return "";
    }


}
