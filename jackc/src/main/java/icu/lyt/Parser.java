package icu.lyt;

import java.io.*;
import java.nio.file.Files;
import java.util.Scanner;


class Parser {
    String currentCommand;
    Scanner scanner =null;
    static final String ADD = "add";
    static final String SUB = "sub";
    static final String NEG = "neg";
    static final String EQ = "eq";
    static final String LT = "lt";
    static final String GT = "gt";
    static final String AND = "and";
    static final String OR = "or";
    static final String NOT = "not";
    static final String C_ARITHMETIC = "C_ARITHMETIC";
    static final String C_PUSH = "C_PUSH";
    static final String C_POP = "C_POP";
    static final String C_LABEL = "C_LABEL";
    static final String C_GOTO = "C_GOTO";
    static final String C_IF = "C_IF";
    static final String C_FUNCTION = "C_FUNCTION";
    static final String C_RETURN = "C_RETURN";
    static final String C_CALL = "C_CALL";
    static final String CONSTANT ="constant";
    static final String ARGUMENT ="argument";
    static final String LOCAL = "local";
    static final String THIS = "this";
    static final String THAT ="that";
    static final String STATIC = "static";
    static final String POINTER ="pointer";
    static final String TEMP ="temp";
    static final String SP ="SP";

    Parser(File vmFile) throws IOException {
        scanner = new Scanner(Files.newInputStream(vmFile.toPath()));
    }
    Parser() {}
    /**
     * 检查当前vm文件是否还有更多命令
     * @version :1.0 2023/7/22
     * @author : lyt0628
     */
    public boolean hasMoreCommand() {
        return scanner.hasNext();
    }
    /**
     * 读取下一条语句作为当前命令
     * @version :1.0 2023/7/22
     * @author : lyt0628
     */
    public void advance() {
      currentCommand = remove_WhiteSpace_And_Comment(scanner.nextLine());
    }
    /**
     * 返回当前命令的类型
     * @version :1.0 2023/7/22
     * @author : lyt0628
     */
    public String commandType() {
        if (ADD.equals(currentCommand) ||
                SUB.equals(currentCommand) ||
                NEG.equals(currentCommand) ||
                EQ.equals(currentCommand) ||
                LT.equals(currentCommand) ||
                GT.equals(currentCommand) ||
                AND.equals(currentCommand) ||
                OR.equals(currentCommand) ||
                NOT.equals(currentCommand)) {
            return C_ARITHMETIC;
        }
        if (currentCommand != null && currentCommand.startsWith("push")) {
            return C_PUSH;
        }
        if (currentCommand != null && currentCommand.startsWith("pop")) {
            return C_POP;
        }

        throw new RuntimeException("当前命令: "+currentCommand+"无法识别!!!");
    }
    /**
     * 返回第一个参数
     * @version :1.0 2023/7/22
     * @author : lyt0628
     */
    public String arg1() {
        String type = commandType();
        if (C_ARITHMETIC.equals(type)){
            return currentCommand;
        }
        if (C_RETURN.equals(type)){
            throw new RuntimeException("C_RETURN命令不可以调用 src.jack_compiler.backend.Parser.arg1 !!!");
        }
        return currentCommand.split(" ")[1];
    }
    /**
     * 返回第二个参数
     * @version :1.0 2023/7/22
     * @author : lyt0628
     */
    public String arg2() {
        String type = commandType();
        if (!C_PUSH.equals(type)&&
            !C_POP.equals(type)&&
            !C_FUNCTION.equals(type)&&
            !C_CALL.equals(type)){
            throw new RuntimeException("只有C_PUSH，C_POP，C_FUNCTION和C_CALL命令可以调用src.jack_compiler.backend.Parser.arg2!!!");
        }
        return currentCommand.split(" ")[2];
    }
    private String remove_WhiteSpace_And_Comment(String command) {
        return command.trim().split("//")[0].replaceAll(" ", "");
    }
}
