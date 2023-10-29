package icu.lyt;

import icu.lyt.StrUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 *
 */
class Parser {
    private String currentCommand;
    private Scanner scanner =null;

    Parser(String vm) throws FileNotFoundException {
        scanner = new Scanner(new File(vm));
    }
    Parser() {}

    public boolean hasMoreCommand() {
        return scanner.hasNext();
    }
    public void advance() {
      currentCommand = StrUtil.removeWhiteSpaceAndComment(scanner.nextLine());
      if (currentCommand.isEmpty()){
          advance();
      }
    }

    public String command(){
        return currentCommand;
    }

    public String commandType() {
        if (currentCommand != null && Parser.isArithmeticCommand(currentCommand)) {
            return CommandConstant.C_ARITHMETIC;
        }
        if (currentCommand != null && currentCommand.startsWith("push")) {
            return CommandConstant.C_PUSH;
        }
        if (currentCommand != null && currentCommand.startsWith("pop")) {
            return CommandConstant.C_POP;
        }

        throw new RuntimeException("command: "+currentCommand+" is not supported!!!");
    }


    public String arg1() {
        String type = commandType();
        if (CommandConstant.C_ARITHMETIC.equals(type)){
            return currentCommand;
        }
        if (CommandConstant.C_RETURN.equals(type)){
            throw new IllegalArgumentException("C_RETURN type command can not call src.jack_compiler.backend.Parser.arg1 !!!");
        }
        return currentCommand.split(" ")[1];
    }

    public String arg2() {
        String type = commandType();
        if (!CommandConstant.C_PUSH.equals(type)&&
            !CommandConstant.C_POP.equals(type)&&
            !CommandConstant.C_FUNCTION.equals(type)&&
            !CommandConstant.C_CALL.equals(type)){
            throw new RuntimeException("only C_PUSH，C_POP，C_FUNCTION和C_CALL type command can call src.jack_compiler.backend.Parser.arg2!!!");
        }
        return currentCommand.split(" ")[2];
    }

    private static boolean isArithmeticCommand(String command){
        if (CommandConstant.ADD.equals(command) ||
            CommandConstant.SUB.equals(command) ||
            CommandConstant.NEG.equals(command) ||
            CommandConstant.EQ.equals(command) ||
            CommandConstant.LT.equals(command) ||
            CommandConstant.GT.equals(command) ||
            CommandConstant.AND.equals(command) ||
            CommandConstant.OR.equals(command) ||
            CommandConstant.NOT.equals(command)) {
            return true;
        }
        return false;
    }
}
