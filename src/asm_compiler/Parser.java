package src.asm_compiler;

public class Parser {
    /**
     * c_command结构 : dest=comp;jump
     * 可能的结构:
     * 结构完整: 如，？？？
     * dest域为空: 0;jMP  D;JEQ
     * jump域为空: D=D+A，A=A-1
     * @version :1.0 2023/7/22
     * @author : lyt0628
     */
    public String[] parse(String c_command){
        int eq_index = c_command.lastIndexOf("=");
        int sc_index = c_command.lastIndexOf(";");
        String dest = eq_index > 0 ? c_command.substring(0,eq_index): "";
        String comp = sc_index > 0 ? c_command.substring(eq_index+1,sc_index) : c_command.substring(+1);
        String jump = sc_index > 0 ? c_command.substring(sc_index+1) : "";
        return new String[]{dest,comp,jump};
    }

    /**
     * 移除空格和注释
     * @version :1.0 2023/7/23
     * @author : lyt0628
     */
    public String  strip_whitespace_and_comments(String string){
        return string.strip().split("//")[0].replace(" ","");
    }
}
