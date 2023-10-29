package icu.lyt;




import icu.lyt.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
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
    private static void main0(String[] args)  {
        if (args.length <1){
            throw new IllegalArgumentException("path is needed!!!");
        }
        File path = new File(args[0]);
        if (!path.exists()){
            throw new IllegalArgumentException("path is not exits!!!");
        }

        CodeWriter codeWriter = new CodeWriter();
        List<String> collect = FileUtil.walk(path)
                .stream()
                .filter(FileUtil::isVmFile)
                .map(File::getPath)
                .collect(Collectors.toList());
        collect
                .forEach(vm->{
                    try{
                        codeWriter.setFilename(FileUtil.filename(vm)+".asm");
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
                    codeWriter.writeArithmetic(parser.command());
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

