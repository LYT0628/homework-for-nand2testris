package icu.lyt;

import org.junit.Test;

import java.io.FileNotFoundException;

import static org.junit.Assert.*;

public class JasmcTest {
public String prjPath = "C:\\Users\\lyt\\Desktop\\Jack-computer\\";

    public static void main(String[] args) throws FileNotFoundException {
        String prjPath = "C:\\Users\\lyt\\Desktop\\Jack-computer\\";
        String[] arg = new String[]
                {prjPath+"jasmc\\src\\test\\resources\\Max.asm"};
        Jasmc.main(arg);
    }
    @Test
    public void add() throws FileNotFoundException {
        String[] args = new String[]
                {prjPath+"jasmc\\src\\test\\resources\\Add.asm"};
        Jasmc.main(args);
    }

    @Test
    public void max() throws FileNotFoundException {
        String[] args = new String[]
                {prjPath+"jasmc\\src\\test\\resources\\Max.asm"};
        Jasmc.main(args);
    }
    @Test
    public void maxl() throws FileNotFoundException {
        String[] args = new String[]
                {prjPath+"jasmc\\src\\test\\resources\\MaxL.asm"};
        Jasmc.main(args);
    }
    @Test
    public void pong() throws FileNotFoundException {
        String[] args = new String[]
                {prjPath+"jasmc\\src\\test\\resources\\Pong.asm"};
        Jasmc.main(args);
    }
    @Test
    public void pongl() throws FileNotFoundException {
        String[] args = new String[]
                {prjPath+"jasmc\\src\\test\\resources\\PongL.asm"};
        Jasmc.main(args);
    }

    @Test
    public void rect() throws FileNotFoundException {
        String[] args = new String[]
                {prjPath+"jasmc\\src\\test\\resources\\Rect.asm"};
        Jasmc.main(args);
    }
    @Test
    public void rectl() throws FileNotFoundException {
        String[] args = new String[]
                {prjPath+"jasmc\\src\\test\\resources\\RectL.asm"};
        Jasmc.main(args);
    }
}