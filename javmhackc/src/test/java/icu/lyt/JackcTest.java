package icu.lyt;


import org.junit.Test;

import java.io.IOException;

public class JackcTest {

    String prjPath = "C:\\Users\\lyt\\Desktop\\Jack-computer\\";
    @Test
    public void basic() throws IOException {
        String[] arg = new String[]
                {prjPath + "javmhackc\\src\\test\\resources\\BasicTest.vm"};
        Jackc.main(arg);
    }

    @Test
    public void pointer() throws IOException {
        String[] arg = new String[]
                {prjPath + "javmhackc\\src\\test\\resources\\PointerTest.vm"};
        Jackc.main(arg);
    }
    @Test
    public void simpleAdd() throws IOException {
        String[] arg = new String[]
                {prjPath + "javmhackc\\src\\test\\resources\\SimpleAdd.vm"};
        Jackc.main(arg);
    }

    @Test
    public void stack() throws IOException {
        String[] arg = new String[]
                {prjPath + "javmhackc\\src\\test\\resources\\StackTest.vm"};
        Jackc.main(arg);
    }

    @Test
    public void staticTest() throws IOException {
        String[] arg = new String[]
                {prjPath + "javmhackc\\src\\test\\resources\\StaticTest.vm"};
        Jackc.main(arg);
    }
}