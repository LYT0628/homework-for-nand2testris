package test.asm_compiler;

import java.text.DecimalFormat;

public class CoderTest {
    public static void main(String[] args) {

        String replace = String.format("%16s", Integer.toBinaryString(3)).replace(" ", "0");
        System.out.println(replace);
    }
}
