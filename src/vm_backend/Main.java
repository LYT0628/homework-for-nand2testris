package src.vm_backend;

import java.io.*;

public class Main {
    public static void main(String[] args) {
//        File VM_File = new File(args[0]);
        File VM_File = new File("C:\\Users\\陆语庭\\Desktop\\jack.vm");
        Parser parser =new Parser(VM_File);
        File asm_File =new File(VM_File.getPath().split("\\.")[0]+".asm");
        CodeWriter codeWriter =new CodeWriter(asm_File);
        while(parser.hasMoreCommand()){
            parser.advance();
            if (Parser.C_ARITHMETIC.equals(parser.commandType())){
                codeWriter.writeArithtic(parser.getCommand());
            }else if (Parser.C_PUSH.equals(parser.commandType())||
                    Parser.C_POP.equals(parser.commandType())){
                codeWriter.writePushPop(parser.commandType(),parser.getCommand());
            }
        }
        codeWriter.close();
    }
}

