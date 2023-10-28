package icu.lyt;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

class CodeWriter {
    private Map<String, String> segmentMap;
    static int LABEL_COUNT=0;
    String filename;
    PrintWriter writer;
    CodeWriter(String asm) throws FileNotFoundException {
        /* 内存段Map初始化*******************************************************************************************/
        segmentMap =new HashMap<>();
        segmentMap.put("constant","CONSTANT");
        segmentMap.put("local","LCL");
        segmentMap.put("argument","ARG");
        segmentMap.put("this","THIS");
        segmentMap.put("that","THAT");
        segmentMap.put("temp","TEMP");
        segmentMap.put("pointer","POINTER");
        segmentMap.put("temp","5");
        segmentMap.put("pointer","3");


        filename = asm.substring(0,asm.lastIndexOf("."));
        /* 内存的初始化操作*******************************************************************************************/
        writer = new PrintWriter(asm);
        writer.println("@256");
        writer.println("D=A");
        writer.println("@SP");
        writer.println("M=D");
        writer.println("@256");
        writer.println("D=A");
        writer.println("@SP");
        writer.println("M=D");
    }

    CodeWriter() {
    }
    /**
     * 提醒coderWriter准备写下一个文件，更新filename和输出流
     * @version :1.0 2023/7/22
     * @author : lyt0628
     */
    public void setFilename(String asmFilename) throws FileNotFoundException {
        /* 内存的初始化操作*******************************************************************************************/
        filename = asmFilename.substring(asmFilename.lastIndexOf(File.separator)+1,asmFilename.lastIndexOf("."));
        close();
        writer = new PrintWriter(asmFilename);
        writer.println("@256");
        writer.println("D=A");
        writer.println("@SP");
        writer.println("M=D");
        writer.println("@256");
        writer.println("D=A");
        writer.println("@SP");
        writer.println("M=D");
    }
    /**
     * 执行算术操作，command为当前命令
     * @version :1.0 2023/7/22
     * @author : lyt0628
     */
    public void writeArithtic(String command) {
        if (Parser.ADD.equals(command)) {
            arimetic_2operator("D=D+M");
        }
        else if (Parser.SUB.equals(command)) {
            arimetic_2operator("D=M-D");
        }
        else if (Parser.NEG.equals(command)) {
            arimatic_logic_1operator("D=-D");
        }
        else if (Parser.EQ.equals(command)){
            compare_logic_2operator("D;JEQ");
        }
        else if (Parser.LT.equals(command)){
            compare_logic_2operator("D;JLT");
        }
        else if (Parser.GT.equals(command)) {
            compare_logic_2operator("D;JGT");
        }
        else if (Parser.AND.equals(command)) {
            compare_logic_2operator("D=D&M");
        }
        else if (Parser.OR.equals(command)) {
            compare_logic_2operator("D=D|M");
        }
        else if (Parser.NOT.equals(command)){
            arimatic_logic_1operator("D=!D");
        }
    }
    /**
     * 执行Push和Pop操作，Type为：C_PUSH或C_POP，command为当前命令
     * @version :1.0 2023/7/22
     * @author : lyt0628
     */
    public void writePushPop(String Type, String segment,String index) {
        if (Parser.C_PUSH.equals(Type)) {
            if (Parser.CONSTANT.equalsIgnoreCase(segment)){
                getValue2DRegister(index);
                getDRegisterValue2StackTop();
            }


            /* 间接寻址的push*******************************************************************************************/
            else if (Parser.LOCAL.equals(segment)||
                    Parser.ARGUMENT.equals(segment)||
                    Parser.THIS.equals(segment)||
                    Parser.THAT.equals(segment)) {
                push_indirect(segmentMap.get(segment),index);
            }


            /* 直接寻址的push*******************************************************************************************/
            else if (Parser.TEMP.equals(segment)||
                    Parser.POINTER.equals(segment)){
                push_direct(segmentMap.get(segment),index);
            }
            else if (Parser.STATIC.equals(segment)) {
                String value=index;
                writer.println("@"+filename+"."+value);
                writer.println("D=M");
                getDRegisterValue2StackTop();
            }
        }
        else if (Parser.C_POP.equals(Type)) {
            /* 间接寻址的pop*******************************************************************************************/
            if (Parser.LOCAL.equals(segment)||
                    Parser.ARGUMENT.equals(segment)||
                    Parser.THIS.equals(segment)||
                    Parser.THAT.equals(segment)) {
                pop_indirect(segmentMap.get(segment), index);
            }
            /* 直接寻址的pop*******************************************************************************************/
            else if (Parser.POINTER.equals(segment)||
                    Parser.TEMP.equals(segment)){
                pop_direct(segmentMap.get(segment),index);
            }
            }
        }
    /**
     * 关闭输出流
     * @version :1.0 2023/7/22
     * @author : lyt0628
     */
    public void close() {
        if (writer != null){
            writer.close();
        }
    }




    /* *************************************************************************************************/
    /* 下面的工具方法封装的一些比较固定的asm语句*******************************************************************************************/
    /* *************************************************************************************************/
    /**
     * 执行一些二元的比较和逻辑操作，将真值赋值给工作栈顶，参数：核心操作，如 小于：D;JLT ，逻辑与：D=D&M
     * @version :1.0 2023/7/22
     * @author : lyt0628
     */
    private void compare_logic_2operator(String operation){
        stackPointerDown();
        getStackTopValue2DRegister();
        stackPointerDown();
        setARegister2StackTop();
        writer.println("D=M-D");
        //if D==0 then goto TRUE
        writer.println("@TRUE"+LABEL_COUNT);
        writer.println(operation);
        //else goto FALSE
        jump2Label("FALSE"+LABEL_COUNT);
        //TRUE stack.push(-1)
        writer.println("(TRUE"+LABEL_COUNT+")");
        setDRegisterTrue();
        getDRegisterValue2StackTop();
        jump2Label("END"+LABEL_COUNT);
        //FALSE stack.push(0)
        writer.println("(FALSE"+LABEL_COUNT+")");
        setDRegisterFalse();
        getDRegisterValue2StackTop();
        //END
        writer.println("(END"+LABEL_COUNT+")");
        LABEL_COUNT++;
    }
    /**
     * 执行一些二元算数操作，将结果赋值给工作栈顶
     * @version : 2023/7/22
     * @author : lyt0628
     */
    private void arimetic_2operator(String operation){
        stackPointerDown();
        getStackTopValue2DRegister();
        stackPointerDown();
        setARegister2StackTop();
        writer.println(operation);
        getDRegisterValue2StackTop();
    }
    /**
     * 执行一些一元的算术和逻辑操作，如 取负和逻辑非
     * @version :1.0 2023/7/22
     * @author : lyt0628
     */
    private void arimatic_logic_1operator(String word){
        stackPointerDown();
        getStackTopValue2DRegister();
        writer.println(word);
        getDRegisterValue2StackTop();
    }
    /**
     * 执行间接寻址的push操作，from为原址，from+index为变址
     * @version :1.0 2023/7/22
     * @author : lyt0628
     */
    private void push_indirect(String from, String index){
        getAddressValue2DRegister(from);
        indexDRegister(index);
        getAddressValue2DRegister("D");
        getDRegisterValue2StackTop();
    }
    /**
     * 执行一些直接寻址的push操作，from为原址，from+index为变址
     * @version :1.0 2023/7/22
     * @author : lyt0628
     */
    private void push_direct(String from, String index){
        getValue2DRegister(from);
        indexDRegister(index);
        getDRegisterValue2StackTop();
    }
    /**
     * 执行直接寻址的pop操作，from为原址，from+index为变址
     * @version :1.0 2023/7/22
     * @author : lyt0628
     */
    private void pop_indirect(String segment, String index){
        writer.println("@"+segment);
        writer.println("D=M");
        writer.println("@"+index);
        writer.println("D=A+D");//目标地址
        stackPointerDown();
        writer.println("@SP");
        writer.println("A=M");
        writer.println("A=M");//值
        writer.println("D=A+D");
        writer.println("A=D-A");
        writer.println("D=D-A");
        writer.println("M=D");
    }
    /**
     * 执行款间接寻址的pop操作，from为原址，from+index为变址
     * @version :1.0 2023/7/22
     * @author : lyt0628
     */
    private void pop_direct(String segment,String index ){
        writer.println("@"+segment);
        writer.println("D=A");
        writer.println("@"+index);
        writer.println("D=A+D");//目标地址
        stackPointerDown();
        writer.println("@SP");
        writer.println("A=M");
        writer.println("A=M");//值
        writer.println("D=A+D");
        writer.println("A=D-A");
        writer.println("D=D-A");
        writer.println("M=D");
    }



    /* *************************************************************************************************/
    /* 下面的工具方法是更低层次的封装*******************************************************************************************/
    /* *************************************************************************************************/

    /**
     * 将工作栈顶指针往上移动一位
     * @version :1.0 2023/7/22
     * @author : lyt0628
     */
    private void stackPointerDown(){
        writer.println("@SP");
        writer.println("M=M-1");
    }
    /**
     * 将工作栈顶指针往下移动一位
     * @version :1.0 2023/7/22
     * @author : lyt0628
     */
    private void stackPointerUp(){
        writer.println("@SP");
        writer.println("M=M+1");
    }
    /**
     * 将工作栈顶的值赋值给D寄存器
     * @version :1.0 2023/7/22
     * @author : lyt0628
     */
    private void getStackTopValue2DRegister(){
        getAddressValue2DRegister("SP");
    }
    /**
     * 将地址指向的值赋值给D寄存器
     * @version :1.0 2023/7/22
     * @author : lyt0628
     */
    private void getAddressValue2DRegister(String address){
        writer.println("@"+address);
        writer.println("A=M");
        writer.println("D=M");
    }
    /**
     * 将给定值赋值给D寄存器
     * @version :1.0 2023/7/22
     * @author : lyt0628
     */
    private void getValue2DRegister(String value){
        writer.println("@"+value);
        writer.println("D=A");
    }
    /**
     * 使D寄存器地址增加加index
     * @version :1.0 2023/7/22
     * @author : lyt0628
     */
    private void indexDRegister(String index){
        writer.println("@"+index);
        writer.println("D=D+A");
    }
    /**
     * 使A寄存器保存栈顶地址
     * @version :1.0 2023/7/22
     * @author : lyt0628
     */
    private void setARegister2StackTop(){
        writer.println("@SP");
        writer.println("A=M");
    }
    /**
     * 将D寄存器保存的值赋值给栈顶
     * @version :1.0 2023/7/22
     * @author : lyt0628
     */
    private void getDRegisterValue2StackTop(){
        writer.println("@SP");
        writer.println("A=M");
        writer.println("M=D");
        writer.println("@SP");
        writer.println("M=M+1");
    }
    /**
     * 将D寄存器的值设为true（-1）
     * @version :1.0 2023/7/22
     * @author : lyt0628
     */
    private void setDRegisterTrue(){
        writer.println("@1");
        writer.println("D=A");
        writer.println("D=-D");
    }
    /**
     * 将D寄存器的值设为False（0）
     * @version : 2023/7/22
     * @author : lyt0628
     */
    private void setDRegisterFalse(){
        writer.println("D=0");
        writer.println("@SP");
        writer.println("A=M");
        writer.println("M=D");
    }
    /**
     * 代码调到标记处执行
     * @version :1.0 2023/7/22
     * @author : lyt0628
     */
    private void jump2Label(String label){
        writer.println("@"+label);
        writer.println("0;JMP");
    }

}