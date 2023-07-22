package src.jack_compiler.backend;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
;

public class Main {
    public static void main(String[] args) throws IOException {

        File vmFile;// = new File(args[0]);
        CodeWriter codeWriter;
        vmFile = new File("C:\\Users\\lyt06\\Desktop\\jack.vm");
        vmFile = new File("C:\\Users\\lyt06\\Desktop\\test");
        if (!vmFile.exists()){
            throw new RuntimeException("目标路径不存在文件或文件夹!!!");
        }
        if (vmFile.isFile()){
            String asmFilename = asmFilename(vmFile.getPath());
            File asm_File =new File(asmFilename);
            codeWriter =new CodeWriter(asm_File);
            compilerVMFile(codeWriter,vmFile);
        }
        else {
            codeWriter = new CodeWriter();
            try(Stream<File> fileStream = Files.walk(vmFile.toPath()).map(Path::toFile).filter(Main::isVmFile)){
                fileStream.forEach(file -> {
                    String asmFilename = asmFilename(file.getPath());
                    try {
                        codeWriter.setFilename(asmFilename);
                        compilerVMFile(codeWriter,file);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }
        codeWriter.close();
    }

    private static void compilerVMFile(CodeWriter codeWriter,File vmFile) throws IOException {
        Parser parser = new Parser(vmFile);
        while(parser.hasMoreCommand()){
            parser.advance();
            String type = parser.commandType();
            if (Parser.C_ARITHMETIC.equals(type)){
                codeWriter.writeArithtic(type);
            }else if (Parser.C_PUSH.equals(type)|| Parser.C_POP.equals(type)){
                codeWriter.writePushPop(type, parser.arg1(),parser.arg2());
            }
        }
    }
    private static String asmFilename(String vmFilename){
        return vmFilename.substring(0, vmFilename.lastIndexOf("."))+".asm";
    }
    static  boolean  isVmFile(File file){
        if (file.isDirectory()){
            return false;
        }
        if (!"vm".equals(
                file.getName().substring(file.getName().lastIndexOf(".")+1))){
            return false;
        }
            return true;
    }

}

