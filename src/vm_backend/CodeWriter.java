package src.vm_backend;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class CodeWriter {
    private Map<String, String> segmentMap;
    static int LABEL_COUNT=0;
    String filename;
    BufferedWriter bw;

    CodeWriter(File asm_File) {
        try {
            if (!asm_File.exists()) {
                asm_File.createNewFile();
            }
            filename=asm_File.getName().split("\\.")[0];
            bw = new BufferedWriter(new FileWriter(asm_File));
        }catch (IOException e) {
            throw new RuntimeException(e);
        }

        segmentMap =new HashMap<>();
        segmentMap.put("constant","CONSTANT");
        segmentMap.put("local","LCL");
        segmentMap.put("argument","ARG");
        segmentMap.put("this","THIS");
        segmentMap.put("that","THAT");
        segmentMap.put("temp","TEMP");
        segmentMap.put("pointer","POINTER");
        segmentMap.put("temp","5");
        segmentMap.put("pointer","3");

        writeLine("@256");
        writeLine("D=A");
        writeLine("@SP");
        writeLine("M=D");
    }
    CodeWriter() {
    }
    public void setFileName(String filename) {
        try {
            if (null != bw) {
                bw.close();
            }
            File asm_File = new File(filename);
            if (!asm_File.exists()) {
                asm_File.createNewFile();
            }
            bw = new BufferedWriter(new FileWriter(asm_File));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeArithtic(String command) {
        if (Parser.ADD.equals(command)) {
            arimetic_2operator("D=D+M");
        }
        else if (Parser.SUB.equals(command)) {
            arimetic_2operator("D=M-D");
        }
        else if (Parser.NEG.equals(command)) {
            arimatic_logic_1operator("D=-D");
        }
        else if (Parser.EQ.equals(command)){
            compare_logic_2operator("D;JEQ");
        }
        else if (Parser.LT.equals(command)){
            compare_logic_2operator("D;JLT");
        }
        else if (Parser.GT.equals(command)) {
            compare_logic_2operator("D;JGT");
        }
        else if (Parser.AND.equals(command)) {
            compare_logic_2operator("D=D&M");
        }
        else if (Parser.OR.equals(command)) {
            compare_logic_2operator("D=D|M");
        }
        else if (Parser.NOT.equals(command)){
            arimatic_logic_1operator("D=!D");
        }
    }
    public void writePushPop(String Type, String command) {
        String[] arg=command.trim().split(" ");
        String segment = arg[1];
        String index = arg[2];
        if (Parser.C_PUSH.equals(Type)) {
            if (Parser.CONSTANT.equalsIgnoreCase(segment)){
                getValue2DRegister("@"+arg[2]);
                getDRegister2SP();
            }
            else if (Parser.LOCAL.equals(segment)||
                    Parser.ARGUMENT.equals(segment)||
                    Parser.THIS.equals(segment)||
                    Parser.THAT.equals(segment)) {
                push_indirect(segmentMap.get(segment),index);
            }
            else if (Parser.TEMP.equals(segment)||
                    Parser.POINTER.equals(segment)){
                push_direct(segmentMap.get(segment),index);
            }
            else if (Parser.STATIC.equals(segment)) {
                String value=arg[2];
                writeLine("@"+filename+"."+value);
                writeLine("D=M");
                getDRegister2SP();
            }
        }
        else if (Parser.C_POP.equals(Type)) {
            if (Parser.LOCAL.equals(command.split(" ")[1])||
                    Parser.ARGUMENT.equals(arg[1])||
                    Parser.THIS.equals(arg[1])||
                    Parser.THAT.equals(arg[1])) {
                pop_indirect(segmentMap.get(segment), index);
            }else if (Parser.POINTER.equals(segment)||
                    Parser.TEMP.equals(segment)){
                pop_direct(segmentMap.get(segment),index);

            }
            }
        }



    public void close() {
        if (null!=bw){
            try {
                bw.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private  void writeLine(String word){
        try {
            bw.write(word);
            bw.newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void stackPointerDown(){
        writeLine("@SP");
        writeLine("M=M-1");
    }
    private void stackPointerUp(){
        writeLine("@SP");
        writeLine("M=M+1");
    }
    private void getSPValue2DRegister(){
        getAddressValue2DRegister("@SP");
    }
    private void getAddressValue2DRegister(String address){
        writeLine(address);
        writeLine("A=M");
        writeLine("D=M");
    }
    private void getValue2DRegister(String value){
        writeLine(value);
        writeLine("D=A");
    }
    private void indexDRegisterByARegister(String index){
        writeLine(index);
        writeLine("D=D+A");
    }
    private void setARegister2SP(){
        writeLine("@SP");
        writeLine("A=M");
    }
    private void getDRegister2SP(){
        writeLine("@SP");
        writeLine("A=M");
        writeLine("M=D");
        writeLine("@SP");
        writeLine("M=M+1");
    }
    private void setDRegisterTrue(){
        writeLine("@1");
        writeLine("D=A");
        writeLine("D=-D");
    }
    private void setDRegisterFalse(){
        writeLine("D=0");
        writeLine("@SP");
        writeLine("A=M");
        writeLine("M=D");
    }
    private void jump2Label(String label){
        writeLine(label);
        writeLine("0;JMP");
    }
    private void compare_logic_2operator(String operation){
        stackPointerDown();
        getSPValue2DRegister();
        stackPointerDown();
        setARegister2SP();
        writeLine("D=M-D");
        //if D==0 then goto TRUE
        writeLine("@TRUE"+LABEL_COUNT);
        writeLine(operation);
        //else goto FALSE
        jump2Label("@FALSE"+LABEL_COUNT);
        //TRUE stack.push(-1)
        writeLine("(TRUE"+LABEL_COUNT+")");
        setDRegisterTrue();
        getDRegister2SP();
        jump2Label("@END"+LABEL_COUNT);
        //FALSE stack.push(0)
        writeLine("(FALSE"+LABEL_COUNT+")");
        setDRegisterFalse();
        getDRegister2SP();
        //END
        writeLine("(END"+LABEL_COUNT+")");
        LABEL_COUNT++;
    }
    private void arimetic_2operator(String word){
        stackPointerDown();
        getSPValue2DRegister();
        stackPointerDown();
        setARegister2SP();
        writeLine(word);
        getDRegister2SP();
    }
    private void arimatic_logic_1operator(String word){
        stackPointerDown();
        getSPValue2DRegister();
        writeLine(word);
        getDRegister2SP();
    }
    private void push_indirect(String from, String index){
        getAddressValue2DRegister("@"+from);
        indexDRegisterByARegister("@"+index);
        getAddressValue2DRegister("@D");
        getDRegister2SP();
    }
    private void push_direct(String from, String index){
        getValue2DRegister("@"+from);
        indexDRegisterByARegister("@"+index);
        getDRegister2SP();
    }
    private void pop_indirect(String segment, String index){
        writeLine("@"+segment);
        writeLine("D=M");
        writeLine("@"+index);
        writeLine("D=A+D");//目标地址
        stackPointerDown();
        writeLine("@SP");
        writeLine("A=M");
        writeLine("A=M");//值
        writeLine("D=A+D");
        writeLine("A=D-A");
        writeLine("D=D-A");
        writeLine("M=D");
    }
    private void pop_direct(String segment,String index ){
        writeLine("@"+segment);
        writeLine("D=A");
        writeLine("@"+index);
        writeLine("D=A+D");//目标地址
        stackPointerDown();
        writeLine("@SP");
        writeLine("A=M");
        writeLine("A=M");//值
        writeLine("D=A+D");
        writeLine("A=D-A");
        writeLine("D=D-A");
        writeLine("M=D");
    }




}