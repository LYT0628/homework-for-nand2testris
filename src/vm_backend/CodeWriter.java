package src.vm_backend;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

class CodeWriter {

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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
            arimetic_2operator_word("D=D+M");
        } else if (Parser.SUB.equals(command)) {
            arimetic_2operator_word("D=M-D");
        } else if (Parser.NEG.equals(command)) {
            arimatic_logic_1operator("D=-D");
        }else if (Parser.EQ.equals(command)){
            compare_logic_2operator_word("D;JEQ");
        }else if (Parser.LT.equals(command)){
            compare_logic_2operator_word("D;JLT");
        } else if (Parser.GT.equals(command)) {
            compare_logic_2operator_word("D;JGT");
        } else if (Parser.AND.equals(command)) {
            compare_logic_2operator_word("D=D&M");
        } else if (Parser.OR.equals(command)) {
            compare_logic_2operator_word("D=D|M");
        }else if (Parser.NOT.equals(command)){
            arimatic_logic_1operator("D=!D");
        }
    }

    public void writePushPop(String Type, String command) {
        String[] arg=command.trim().split(" ");
        if (Parser.C_PUSH.equals(Type)) {
            if (Parser.CONSTANT.equals(arg[1])){
                try {
                    bw.write("@"+arg[2]);
                    bw.newLine();
                    bw.write("D=A");
                    bw.newLine();
                    bw.write("@tmp");
                    bw.newLine();
                    bw.write("M=D");
                    bw.newLine();
                    bw.write("@SP");
                    bw.newLine();
                    bw.write("D=M");
                    bw.newLine();
                    bw.write("@tmp");
                    bw.newLine();
                    bw.write("D=D+M");
                    bw.newLine();
                    bw.write("@SP");
                    bw.newLine();
                    bw.write("D=D-M");
                    bw.newLine();
                    bw.write("A=M");
                    bw.newLine();
                    bw.write("M=D");
                    bw.newLine();
                    bw.write("@SP");
                    bw.newLine();
                    bw.write("M=M+1");
                    bw.newLine();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }else if (Parser.LOCAL.equals(command.split(" ")[1])||
                    Parser.ARGUMENT.equals(arg[1])||
                    Parser.THIS.equals(arg[1])||
                    Parser.THAT.equals(arg[1])){
                try {
                    if (Parser.LOCAL.endsWith(command.split(" ")[1]))
                        writeLine("@1");
                    else if (Parser.ARGUMENT.endsWith(command.split(" ")[1]))
                        writeLine("@2");
                     else if (Parser.THIS.equals(command.split(" ")[1]))
                        writeLine("@3");
                     else if (Parser.THAT.equals(command.split(" ")[1]))
                         writeLine("@4");
                    bw.write("A=M");
                    bw.newLine();
                    bw.write("D=A");
                    bw.newLine();
                    bw.write("@"+command.split(" ")[2]);
                    bw.newLine();
                    bw.write("D=D+A");
                    bw.newLine();
                    bw.write("@SP");
                    bw.newLine();
                    bw.write("A=M");
                    bw.newLine();
                    bw.write("M=D");
                    bw.newLine();
                    bw.write("@SP");
                    bw.newLine();
                    bw.write("M=M+1");
                    bw.newLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            else if (Parser.TEMP.equals(arg[1])||
                    Parser.POINT.equals(arg[1])){
                try {
                    if (Parser.TEMP.equals(command.split(" ")[1]))
                        bw.write("@5");
                    else if (Parser.POINT.equals(command.split(" ")[1]))
                        bw.write("@3");
                    bw.newLine();
                    bw.write("D=A");
                    bw.newLine();
                    bw.write("@"+command.split(" ")[2]);
                    bw.newLine();
                    bw.write("D=D+A");
                    bw.newLine();
                    bw.write("@SP");
                    bw.newLine();
                    bw.write("A=M");
                    bw.newLine();
                    bw.write("M=D");
                    bw.newLine();
                    bw.write("@SP");
                    bw.newLine();
                    bw.write("M=M+1");
                    bw.newLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else if (Parser.SP.equals(arg[1])) {
                System.out.println("哈哈海");
            } else if (Parser.STATIC.equals(arg[1])) {
                try {
                    bw.write("@"+filename+"."+arg[2]);
                    bw.newLine();
                    bw.write("D=M");
                    bw.newLine();
                    bw.write("@SP");
                    bw.newLine();
                    bw.write("A=M");
                    bw.newLine();
                    bw.write("M=D");
                    bw.newLine();
                    bw.write("@SP");
                    bw.newLine();
                    bw.write("M=M+1");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } else if (Parser.C_POP.equals(Type)) {
            if (Parser.LOCAL.equals(command.split(" ")[1])||
                    Parser.ARGUMENT.equals(arg[1])||
                    Parser.THIS.equals(arg[1])||
                    Parser.THAT.equals(arg[1])){
                try {
                    bw.write("@SP");
                    bw.newLine();
                    bw.write("A=A-1");
                    bw.newLine();
                    bw.write("D=M");
                    bw.newLine();
                    if (Parser.LOCAL.equals(arg[1])){
                        bw.write("@LCL");
                        bw.newLine();
                        bw.write("A=M");
                        bw.newLine();
                        bw.write("A=A+"+arg[2]);
                        bw.newLine();
                        bw.write("M=D");
                        bw.newLine();
                        bw.write("@LCL");
                        bw.newLine();
                        bw.write("M=M+1");
                        bw.newLine();
                    } else if (Parser.ARGUMENT.equals(arg[1])) {
                        bw.write("@ARG");
                        bw.newLine();
                        bw.write("A=M");
                        bw.newLine();
                        bw.write("A=A+"+arg[2]);
                        bw.newLine();
                        bw.write("M=D");
                        bw.newLine();
                        bw.write("@ARG");
                        bw.newLine();
                        bw.write("M=M+1");
                        bw.newLine();
                    }else if (Parser.THIS.equals(arg[1])){
                        bw.write("@THIS");
                        bw.newLine();
                        bw.write("A=M");
                        bw.newLine();
                        bw.write("A=A+"+arg[2]);
                        bw.newLine();
                        bw.write("M=D");
                        bw.newLine();
                        bw.write("@THIS");
                        bw.newLine();
                        bw.write("M=M+1");
                        bw.newLine();
                    } else if (Parser.THAT.equals(arg[1])) {
                        bw.write("@THAT");
                        bw.newLine();
                        bw.write("A=M");
                        bw.newLine();
                        bw.write("A=A+"+arg[2]);
                        bw.newLine();
                        bw.write("M=D");
                        bw.newLine();
                        bw.write("@THAT");
                        bw.newLine();
                        bw.write("M=M+1");
                        bw.newLine();
                    }
                    bw.write("@SP");
                    bw.newLine();
                    bw.write("M=M-1");
                    bw.newLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }else if (Parser.POINT.equals(arg[1])||
                    Parser.TEMP.equals(arg[1])){
                try {
                    bw.write("@SP");
                    bw.newLine();
                    bw.write("A=A-1");
                    bw.newLine();
                    bw.write("D=M");
                    bw.newLine();
                    if (Parser.POINT.equals(arg[1])){
                        bw.write("@3");
                        bw.newLine();
                        bw.write("A=M");
                        bw.newLine();
                        bw.write("A=A+"+arg[2]);
                        bw.newLine();
                        bw.write("M=D");
                        bw.newLine();
                    }else if (Parser.TEMP.equals(arg[1])){
                        bw.write("@5");
                        bw.newLine();
                        bw.write("A=M");
                        bw.newLine();
                        bw.write("A=A+"+arg[2]);
                        bw.newLine();
                        bw.write("M=D");
                        bw.newLine();
                    }
                    bw.write("@SP");
                    bw.newLine();
                    bw.write("M=M-1");
                    bw.newLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

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
    private void getValue2DRegister(){
        writeLine("@SP");
        writeLine("A=M");
        writeLine("D=M");
    }
    private void pointer2Value(){
        writeLine("@SP");
        writeLine("A=M");
    }
    private void pushDRegister(){
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
    private void compare_logic_2operator_word(String operation){
        stackPointerDown();
        getValue2DRegister();
        stackPointerDown();
        pointer2Value();
        writeLine("D=M-D");
        //if D==0 then goto TRUE
        writeLine("@TRUE"+LABEL_COUNT);
        writeLine(operation);
        //else goto FALSE
        jump2Label("@FALSE"+LABEL_COUNT);
        //TRUE stack.push(-1)
        writeLine("(TRUE"+LABEL_COUNT+")");
        setDRegisterTrue();
        pushDRegister();
        jump2Label("@END"+LABEL_COUNT);
        //FALSE stack.push(0)
        writeLine("(FALSE"+LABEL_COUNT+")");
        setDRegisterFalse();
        pushDRegister();
        //END
        writeLine("(END"+LABEL_COUNT+")");
        LABEL_COUNT++;
    }
    private void arimetic_2operator_word(String word){
        stackPointerDown();
        getValue2DRegister();
        stackPointerDown();
        pointer2Value();
        writeLine(word);
        pushDRegister();
    }
    private void arimatic_logic_1operator(String word){
        stackPointerDown();
        getValue2DRegister();
        writeLine(word);
        pushDRegister();
    }
    public static void main(String[] args) {
        CodeWriter codeWriter = new CodeWriter();
        codeWriter.setFileName("D:\\桌面\\out");
        codeWriter.writeArithtic("add");
        codeWriter.writePushPop(Parser.C_POP, "123456");
        codeWriter.close();
        System.out.println("Hello world!");
    }
}