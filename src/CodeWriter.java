package src;

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
                //sp--
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
                //D=stack.top-D
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
                //sp--
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

                bw.write("D=-D");
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
        }else if (Parser.EQ.equals(command)){
            try {
                //sp--
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
                //D=stack.top-D
                bw.write("@SP");
                bw.newLine();
                bw.write("A=M");
                bw.newLine();
                bw.write("D=M-D");
                bw.newLine();
                //if D==0 then goto TRUE
                bw.write("@TRUE"+LABEL_COUNT);
                bw.newLine();
                bw.write("D;JEQ");
                bw.newLine();
                //else goto FALSE
                bw.write("@FALSE"+LABEL_COUNT);
                bw.newLine();
                bw.write("0;JMP");
                bw.newLine();
                //TRUE stack.push(-1)
                bw.write("(TRUE"+LABEL_COUNT+")");
                bw.newLine();
                bw.write("@1");
                bw.newLine();
                bw.write("D=A");
                bw.newLine();
                bw.write("D=-D");
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
                //goto END
                bw.write("@END"+LABEL_COUNT);
                bw.newLine();
                bw.write("0;JMP");
                bw.newLine();

                //FALSE stack.push(0)
                bw.write("(FALSE"+LABEL_COUNT+")");
                bw.newLine();
                bw.write("D=0");
                bw.newLine();
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
                //END
                bw.write("(END"+LABEL_COUNT+")");
                bw.newLine();
                LABEL_COUNT++;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }else if (Parser.LT.equals(command)){
            try {
                //sp--
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
                //D=stack.top-D
                bw.write("@SP");
                bw.newLine();
                bw.write("A=M");
                bw.newLine();
                bw.write("D=M-D");
                bw.newLine();
                //if D==0 then goto TRUE
                bw.write("@TRUE"+LABEL_COUNT);
                bw.newLine();
                bw.write("D;JLT");
                bw.newLine();
                //else goto FALSE
                bw.write("@FALSE"+LABEL_COUNT);
                bw.newLine();
                bw.write("0;JMP");
                bw.newLine();
                //TRUE stack.push(-1)
                bw.write("(TRUE"+LABEL_COUNT+")");
                bw.newLine();
                bw.write("@1");
                bw.newLine();
                bw.write("D=A");
                bw.newLine();
                bw.write("D=-D");
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
                //goto END
                bw.write("@END"+LABEL_COUNT);
                bw.newLine();
                bw.write("0;JMP");
                bw.newLine();

                //FALSE stack.push(0)
                bw.write("(FALSE"+LABEL_COUNT+")");
                bw.newLine();
                bw.write("D=0");
                bw.newLine();
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
                //END
                bw.write("(END"+LABEL_COUNT+")");
                bw.newLine();
                LABEL_COUNT++;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if (Parser.GT.equals(command)) {
            try {
                //sp--
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
                //D=stack.top-D
                bw.write("@SP");
                bw.newLine();
                bw.write("A=M");
                bw.newLine();
                bw.write("D=M-D");
                bw.newLine();
                //if D==0 then goto TRUE
                bw.write("@TRUE"+LABEL_COUNT);
                bw.newLine();
                bw.write("D;JGT");
                bw.newLine();
                //else goto FALSE
                bw.write("@FALSE"+LABEL_COUNT);
                bw.newLine();
                bw.write("0;JMP");
                bw.newLine();
                //TRUE stack.push(-1)
                bw.write("(TRUE"+LABEL_COUNT+")");
                bw.newLine();
                bw.write("@1");
                bw.newLine();
                bw.write("D=A");
                bw.newLine();
                bw.write("D=-D");
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
                //goto END
                bw.write("@END"+LABEL_COUNT);
                bw.newLine();
                bw.write("0;JMP");
                bw.newLine();

                //FALSE stack.push(0)
                bw.write("(FALSE"+LABEL_COUNT+")");
                bw.newLine();
                bw.write("D=0");
                bw.newLine();
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
                //END
                bw.write("(END"+LABEL_COUNT+")");
                bw.newLine();
                LABEL_COUNT++;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if (Parser.AND.equals(command)) {
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
                bw.write("D=D&M");
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
        } else if (Parser.OR.equals(command)) {
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
                bw.write("D=D|M");
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
        }else if (Parser.NOT.equals(command)){
            try {
                //sp--
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

                bw.write("D=!D");
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
                        bw.write("@1");
                    else if (Parser.ARGUMENT.endsWith(command.split(" ")[1]))
                        bw.write("@2");
                     else if (Parser.THIS.equals(command.split(" ")[1]))
                        bw.write("@3");
                     else if (Parser.THAT.equals(command.split(" ")[1]))
                         bw.write("@4");
                    bw.newLine();
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

    public static void main(String[] args) {
        CodeWriter codeWriter = new CodeWriter();
        codeWriter.setFileName("D:\\桌面\\out");
        codeWriter.writeArithtic("add");
        codeWriter.writePushPop(Parser.C_POP, "123456");
        codeWriter.close();
        System.out.println("Hello world!");
    }
}
