package icu.lyt;

public interface CommandConstant {
//    Arithtic
   static String ADD = "add";
    static String SUB = "sub";
    static String NEG = "neg";
    static String EQ = "eq";
    static String LT = "lt";
    static String GT = "gt";
    static  String AND = "and";
    static String OR = "or";
    static String NOT = "not";

//    command type
static  String C_ARITHMETIC = "C_ARITHMETIC";
    static String C_PUSH = "C_PUSH";
    static  String C_POP = "C_POP";
    static  String C_LABEL = "C_LABEL";
    static  String C_GOTO = "C_GOTO";
    static  String C_IF = "C_IF";
    static  String C_FUNCTION = "C_FUNCTION";
    static String C_RETURN = "C_RETURN";
    static  String C_CALL = "C_CALL";

//    segment
static String CONSTANT ="constant";
    static   String SP ="SP";
    static  String LOCAL = "local";
    static String ARGUMENT ="argument";
    static String THIS = "this";
    static String THAT ="that";
    static String POINTER ="pointer";
    static  String TEMP ="temp";
    static   String STATIC = "static";

}
