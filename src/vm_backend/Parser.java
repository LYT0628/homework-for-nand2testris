package src.vm_backend;

import java.io.*;

class Parser {
    String current_Line;

    BufferedReader br;

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

    Parser(File VM_File) {
        try {
            br = new BufferedReader(new FileReader(VM_File));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    Parser() {
    }
    public boolean hasMoreCommand() {
        try {
            br.mark(1024);
            String next_Line;
            if ((next_Line = br.readLine()) != null&&!"".equals(next_Line)) {
                br.reset();
                return true;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    private String remove_WhiteSpace_And_Comment(String command) {
        return command.trim().split("//")[0].replaceAll("  ", " ");
    }

    public void advance() {
        String next_Line;
        try {
            if ((next_Line = br.readLine())!= null){
                current_Line=remove_WhiteSpace_And_Comment(next_Line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public String commandType() {
        if (current_Line != null &&
                ADD.equals(current_Line) ||
                SUB.equals(current_Line) ||
                NEG.equals(current_Line) ||
                EQ.equals(current_Line) ||
                LT.equals(current_Line) ||
                GT.equals(current_Line) ||
                AND.equals(current_Line) ||
                OR.equals(current_Line) ||
                NOT.equals(current_Line)) {
            return C_ARITHMETIC;
        }
        if (current_Line != null && current_Line.startsWith("push")) {
            return C_PUSH;
        }
        if (current_Line != null && current_Line.startsWith("pop")) {
            return C_POP;
        }
        return "";
    }

    public String arg1() {
        String[] command = current_Line.split(" ");
        return command.length >= 2 ? command[1] : command[0];
    }

    public int arg2() {
        return Integer.parseInt(current_Line.split(" ")[2]);
    }

    public String getCommand() {
        return current_Line;
    }

    public static void main(String[] args) {
        Parser parser = new Parser();
        parser.current_Line = "add";
        System.out.println(parser.commandType());
        System.out.println(parser.arg1());
        System.out.println("Hello world!");
    }
}
