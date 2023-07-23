package src.jack_compiler.vm2asm;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class VM2ASM {
    public static void main(String[] args) throws IOException {

        File file;// = new File(args[0]);
        CodeWriter codeWriter;
        file = new File("C:\\Users\\lyt06\\Desktop\\jack.vm");
        file = new File("C:\\Users\\lyt06\\Desktop\\test");
        if (!file.exists()){
            throw new RuntimeException("目标路径不存在文件或文件夹!!!");
        }
        if (file.isFile()){
            String asmFilename = asmFilename(file.getPath());
            File asmFile =new File(asmFilename);
            codeWriter =new CodeWriter(asmFile);
            compilerVMFile(codeWriter,file);
        }
        else {
            codeWriter = new CodeWriter();
            try(Stream<File> fileStream = Files.walk(file.toPath()).map(Path::toFile).filter(VM2ASM::isVmFile)){
                fileStream.forEach(vmFile -> {
                    String asmFilename = asmFilename(vmFile.getPath());
                    try {
                        codeWriter.setFilename(asmFilename);
                        compilerVMFile(codeWriter,vmFile);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }
        codeWriter.close();
    }

    /**
     * 编译一个vm文件，要求coderWriter的输出流已经准备好
     * @version : 1.0 2023/7/23
     * * @author : lyt0628
     * */
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

    /**
     * 得到VM文件对应的asm文件名
     * @version :1.0 2023/7/23
     * @author : lyt0628
     */
    private static String asmFilename(String vmFilename){
        return vmFilename.substring(0, vmFilename.lastIndexOf("."))+".asm";
    }

    /**
     * 判断是否为vm文件
     * @version :1.0 2023/7/23
     * @author : lyt0628
     */
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

