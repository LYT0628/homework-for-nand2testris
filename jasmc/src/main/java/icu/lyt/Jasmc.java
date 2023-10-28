package icu.lyt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.Scanner;

public class Jasmc {
    private final SymbolTable symbolTable = SymbolTable.getInstance();


    private  void assemble(String filename) throws FileNotFoundException {
        Scanner scanner = new Scanner(filename+".asm");
        PrintWriter printWriter = new PrintWriter(filename + ".hack");
        Long lineNumber = 0L;
//        first scan, aiming to build symbolTable
        while (scanner.hasNext()){
            String line = scanner.nextLine();
            line = StrUtil.removeWhiteSpaceAndComment(line);
            if (Objects.equals(line, "") || line.startsWith("//")){
                continue;
            }
            if (line.startsWith("(") && line.endsWith(")")){
                String label = line.substring(1, line.length());
                symbolTable.addLabel(label,String.valueOf(lineNumber));
                continue;
            }
            lineNumber++;
        }

        scanner.reset();
//        second scan, aiming to compile asm to hack
        while(scanner.hasNext()){
            String line = scanner.nextLine();
            line = StrUtil.removeWhiteSpaceAndComment(line);
            if (Objects.equals(line, "") || line.startsWith("//")){
                continue;
            }
            if (line.startsWith("(") && line.endsWith(")")){
                continue;
            }
            String writeLine = "";
            if (line.startsWith("@")){
                // A指令
                int address = 0;
                try{
                    address = Integer.parseInt(line.substring(1));
                }catch (Exception e){
                    address = Integer.parseInt(symbolTable.getVariable(line.substring(1)));
                }finally {
                    writeLine = Coder.int_to_bin_16(address);
                }
            }else {
                String[] expression = Parser.parse(line);
                String d = expression[0];
                String c = expression[1];
                String j = expression[2];
                writeLine = "111"+Coder.comp(c)+Coder.comp(d)+Coder.comp(j);
            }
            printWriter.println(writeLine);
        }

        scanner.close();
        printWriter.close();
    }

    public static void main(String[] args) throws FileNotFoundException {
        if (StrUtil.isBlank(args[0]) || Objects.equals("help",args[0])){
            System.out.println("Hack assembly language file to be converted to binary code");
            return;
        }

        String filename = System.getProperty("user.dir") + File.separator + args[0];
        String basename = FileUtil.basename(filename);
        String extension = FileUtil.extension(filename);
        File file = new File(filename);

        if(!file.exists()){
            throw new FileNotFoundException("path not exists!!!");
        }
        if (Objects.equals(".asm",extension)){
            throw new IllegalArgumentException("extension must be .asm!!!");
        }
        new Jasmc().assemble(basename);
    }
}
