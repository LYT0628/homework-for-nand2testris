package icu.lyt;

import java.io.*;


class CodeWriter {
    private final SegmentMap segmentMap = SegmentMap.getIstance();
    private static int LABEL_COUNT=0;
    private String filename;
    private PrintWriter writer;

    public CodeWriter() {}
    
    public void setFilename(String asm) throws FileNotFoundException {
        filename = FileUtil.basename(asm);
        close();
        writer = new PrintWriter(asm);
        bootstrap();
    }
    
    public void writeArithmetic(String command) {
        switch (command){
            case CommandConstant.ADD:
                arithmeticBinaryOperation("D=D+M");
                break;
            case CommandConstant.SUB:
                arithmeticBinaryOperation("D=M-D");
                break;
            case CommandConstant.NEG:
                arithmeticOrLogicUnaryOperation("D=-D");
                break;
            case CommandConstant.EQ:
                arithmeticOrLogicUnaryOperation("D;JEQ");
                break;
            case CommandConstant.LT:
                compareOrLogicBinaryOperation("D;JLT");
                break;
            case CommandConstant.GT:
                compareOrLogicBinaryOperation("D;JGT");
                break;
            case CommandConstant.AND:
                compareOrLogicBinaryOperation("D=D&M");
                break;
            case CommandConstant.OR:
                compareOrLogicBinaryOperation("D=D|M");
                break;
            case CommandConstant.NOT:
                arithmeticOrLogicUnaryOperation("D=!D");
                break;
            default:
                throw new IllegalArgumentException("command is not supported");
        }
    }

    
    public void writePushPop(String Type, String segment,String index) {
        if (CommandConstant.C_PUSH.equals(Type)) {
            if (CommandConstant.CONSTANT.equalsIgnoreCase(segment)){
                value2D(index);
                DValue2SP();
            }
            else if (CommandConstant.STATIC.equals(segment)) {
                String value=index;
                writer.println("@"+filename+"."+value);
                writer.println("D=M");
                DValue2SP();
            }
            /* 间接寻址的push****/
            else if (indirect(segment)) {
                push_indirect(segmentMap.get(segment),index);
            }
            /* 直接寻址的push*******************************************************************************************/
            else if (direct(segment)){
                push_direct(segmentMap.get(segment),index);
            }
        }
        else if (CommandConstant.C_POP.equals(Type)) {
            /* 间接寻址的pop*******************************************************************************************/
            if (indirect(segment)) {
                pop_indirect(segmentMap.get(segment), index);
            }
            /* 直接寻址的pop*******************************************************************************************/
            else if (direct(segment)){
                pop_direct(segmentMap.get(segment),index);
            }
        }// if
        }// func

    private static boolean direct(String segment) {
        return CommandConstant.TEMP.equals(segment) ||
                CommandConstant.POINTER.equals(segment);
    }

    private static boolean indirect(String segment) {
        return CommandConstant.LOCAL.equals(segment) ||
                CommandConstant.ARGUMENT.equals(segment) ||
                CommandConstant.THIS.equals(segment) ||
                CommandConstant.THAT.equals(segment);
    }


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
     * @author : lyt0628
     */
    private void compareOrLogicBinaryOperation(String operation){
        SPDown();
        SPValue2D();
        SPDown();
        SPValue2A();
        writer.println("D=M-D");
        //if D==0 then goto TRUE
        writer.println("@TRUE"+LABEL_COUNT);
        writer.println(operation);
        //else goto FALSE
        jump2Label("FALSE"+LABEL_COUNT);
        //TRUE stack.push(-1)
        writer.println("(TRUE"+LABEL_COUNT+")");
        true2D();
        DValue2SP();
        jump2Label("END"+LABEL_COUNT);
        //FALSE stack.push(0)
        writer.println("(FALSE"+LABEL_COUNT+")");
        false2D();
        DValue2SP();
        //END
        writer.println("(END"+LABEL_COUNT+")");
        LABEL_COUNT++;
    }
    /**
     * 执行一些二元算数操作，将结果赋值给工作栈顶
     * @author : lyt0628
     */
    private void arithmeticBinaryOperation(String operation){
        SPDown();
        SPValue2D();
        SPDown();
        SPValue2A();
        writer.println(operation);
        DValue2SP();
    }
    /**
     * 执行一些一元的算术和逻辑操作，如 取负和逻辑非
     * @author : lyt0628
     */
    private void arithmeticOrLogicUnaryOperation(String word){
        SPDown();
        SPValue2D();
        writer.println(word);
        DValue2SP();
    }
    /**
     * 执行间接寻址的push操作，from为原址，from+index为变址
     * @author : lyt0628
     */
    private void push_indirect(String from, String index){
        addrValue2D(from);
        indexD(index);
        addrValue2D("D");
        DValue2SP();
    }
    /**
     * 执行一些直接寻址的push操作，from为原址，from+index为变址
     * @author : lyt0628
     */
    private void push_direct(String from, String index){
        value2D(from);
        indexD(index);
        DValue2SP();
    }
    /**
     * 执行直接寻址的pop操作，from为原址，from+index为变址
     * @author : lyt0628
     */
    private void pop_indirect(String segment, String index){
        writer.println("@"+segment);
        writer.println("D=M");
        writer.println("@"+index);
        writer.println("D=A+D");//目标地址
        SPDown();
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
     * @author : lyt0628
     */
    private void pop_direct(String segment,String index ){
        writer.println("@"+segment);
        writer.println("D=A");
        writer.println("@"+index);
        writer.println("D=A+D");//目标地址
        SPDown();
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
     * @author : lyt0628
     */
    private void SPDown(){
        writer.println("@SP");
        writer.println("M=M-1");
    }
    /**
     * 将工作栈顶指针往下移动一位
     * @author : lyt0628
     */
    private void SPUp(){
        writer.println("@SP");
        writer.println("M=M+1");
    }
    /**
     * 将工作栈顶的值赋值给D寄存器
     * @author : lyt0628
     */
    private void SPValue2D(){
        addrValue2D("SP");
    }
    /**
     * 将地址指向的值赋值给D寄存器
     * @author : lyt0628
     */
    private void addrValue2D(String address){
        writer.println("@"+address);
        writer.println("A=M");
        writer.println("D=M");
    }
    /**
     * 将给定值赋值给D寄存器
     * @author : lyt0628
     */
    private void value2D(String value){
        writer.println("@"+value);
        writer.println("D=A");
    }
    /**
     * 使D寄存器地址增加加index
     * @author : lyt0628
     */
    private void indexD(String index){
        writer.println("@"+index);
        writer.println("D=D+A");
    }
    /**
     * 使A寄存器保存栈顶地址
     * @author : lyt0628
     */
    private void SPValue2A(){
        writer.println("@SP");
        writer.println("A=M");
    }
    /**
     * 将D寄存器保存的值赋值给栈顶
     * @author : lyt0628
     */
    private void DValue2SP(){
        writer.println("@SP");
        writer.println("A=M");
        writer.println("M=D");
        writer.println("@SP");
        writer.println("M=M+1");
    }
    /**
     * 将D寄存器的值设为true（-1）
     * @author : lyt0628
     */
    private void true2D(){
        writer.println("@1");
        writer.println("D=A");
        writer.println("D=-D");
    }
    /**
     * 将D寄存器的值设为False（0）
     * @author : lyt0628
     */
    private void false2D(){
        writer.println("D=0");
        writer.println("@SP");
        writer.println("A=M");
        writer.println("M=D");
    }
    /**
     * 代码调到标记处执行
     * @author : lyt0628
     */
    private void jump2Label(String label){
        writer.println("@"+label);
        writer.println("0;JMP");
    }
    
    private void bootstrap() {
        writer.println("@256");
        writer.println("D=A");
        writer.println("@SP");
        writer.println("M=D");
        writer.println("@256");
        writer.println("D=A");
        writer.println("@SP");
        writer.println("M=D");
    }

}