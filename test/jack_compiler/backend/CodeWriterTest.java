package test.jack_compiler.backend;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class CodeWriterTest {
    public static void main(String[] args) throws FileNotFoundException {
        PrintWriter printWriter = new PrintWriter("C:\\Users\\lyt06\\Desktop\\test.vm");
        printWriter.println("123456");
        printWriter.close();
        File file = new File("C:\\Users\\lyt06\\Desktop\\test.vm");
        System.out.println(file.getName());
    }
}
