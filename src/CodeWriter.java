package src;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

class CodeWriter {
    BufferedWriter bw;


    CodeWriter(File asm_File) {
        try {
            if (!asm_File.exists()) {
                asm_File.createNewFile();
            }
            bw = new BufferedWriter(new FileWriter(asm_File));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            bw.write("@256");
            bw.newLine();
            bw.write("D=A");
            bw.newLine();
            bw.write("@SP");
            bw.newLine();
            bw.write("M=D");
            bw.newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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
            try {
                //stack.pop
                bw.write("@SP");
                bw.newLine();
                bw.write("M=M-1");
                bw.newLine();
                //D=stack.top
                bw.write("@SP");
                bw.newLine();
                bw.write("A=M");
                bw.newLine();
                bw.write("D=M");
                bw.newLine();
                //stack.pop
                bw.write("@SP");
                bw.newLine();
                bw.write("M=M-1");
                bw.newLine();
                //D=D+stack.top
                bw.write("@SP");
                bw.newLine();
                bw.write("A=M");
                bw.newLine();
                bw.write("D=D+M");
                bw.newLine();
                //stack.push(D)
                bw.write("@SP");
                bw.newLine();
                bw.write("A=M");
                bw.newLine();
                bw.write("M=D");
                bw.newLine();
                //sp++
                bw.write("@SP");
                bw.newLine();
                bw.write("M=M+1");
                bw.newLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if (Parser.SUB.equals(command)) {
            try {
                //stack.pop
                bw.write("@SP");
                bw.newLine();
                bw.write("M=M-1");
                bw.newLine();
                //D=stack.top
                bw.write("@SP");
                bw.newLine();
                bw.write("A=M");
                bw.newLine();
                bw.write("D=M");
                bw.newLine();
                //stack.pop
                bw.write("@SP");
                bw.newLine();
                bw.write("M=M-1");
                bw.newLine();
                //D=D+stack.top
                bw.write("@SP");
                bw.newLine();
                bw.write("A=M");
                bw.newLine();
                bw.write("D=M-D");
                bw.newLine();
                //stack.push(D)
                bw.write("@SP");
                bw.newLine();
                bw.write("A=M");
                bw.newLine();
                bw.write("M=D");
                bw.newLine();
                //sp++
                bw.write("@SP");
                bw.newLine();
                bw.write("M=M+1");
                bw.newLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if (Parser.NEG.equals(command)) {
            try {
                bw.write("NEG");

                bw.newLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void writePushPop(String Type, String command) {
        if (Parser.C_PUSH.equals(Type)) {
            String[] arg=command.trim().split(" ");
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

            }

        } else if (Parser.C_POP.equals(Type)) {
//            try {
//                bw.write(Type + command);
//                bw.newLine();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
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

    public static void main(String[] args) {
        CodeWriter codeWriter = new CodeWriter();
        codeWriter.setFileName("D:\\桌面\\out");
        codeWriter.writeArithtic("add");
        codeWriter.writePushPop(Parser.C_POP, "123456");
        codeWriter.close();
        System.out.println("Hello world!");
    }
}
