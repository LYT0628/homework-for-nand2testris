package icu.lyt;


import java.io.*;
import java.util.stream.Collectors;



/**
 * a asm compiler for Jack
 * @version : 1.0 2023/7/23
 * @author : lyt0628
 * */
public class Jackc {
    public static void main(String[] args) throws IOException {
        main0(args);
    }

    /**
     * enter compiler
     * @param args args
     * @throws IOException
     */
    public static void main0(String[] args) throws IOException {
        File path = new File(args[0]);
        if (!path.exists()){
            throw new IllegalArgumentException("path is not exits!!!");
        }

        CodeWriter codeWriter = new CodeWriter();
        FileUtil.walk(path)
                .stream()
                .filter(FileUtil::isVmFile)
                .map(File::getPath)
                .collect(Collectors.toList())
                .forEach(vm->{
                    try{
                        codeWriter.setFilename(vm);
                        compilerVMFile(codeWriter,vm);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
        codeWriter.close();
    }

    /**
     * compiler a VM to asm
     * @author : lyt0628
     * */
    private static void compilerVMFile(CodeWriter codeWriter,String vm) throws IOException {
        Parser parser = new Parser(vm);
        while(parser.hasMoreCommand()){
            parser.advance();
            String type = parser.commandType();
            switch (parser.commandType()){
                case CommandConstant.C_ARITHMETIC:
                    codeWriter.writeArithmetic(type);
                    break;
                case CommandConstant.C_PUSH:
                case CommandConstant.C_POP:
                    codeWriter.writePushPop(type, parser.arg1(),parser.arg2());
                    break;
                default:
                    throw new IllegalArgumentException("command is not supported");
            }
        }
    }

}

