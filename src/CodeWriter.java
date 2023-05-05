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
                bw.write("add");
                bw.newLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if (Parser.SUB.equals(command)) {
            try {
                bw.write("sub");

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
            try {
                bw.write(Type + command);
                bw.newLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if (Parser.C_POP.equals(Type)) {
            try {
                bw.write(Type + command);
                bw.newLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
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

    public static void main(String[] args) {
        CodeWriter codeWriter = new CodeWriter();
        codeWriter.setFileName("D:\\桌面\\out");
        codeWriter.writeArithtic("add");
        codeWriter.writePushPop(Parser.C_POP, "123456");
        codeWriter.close();
        System.out.println("Hello world!");
    }
}
