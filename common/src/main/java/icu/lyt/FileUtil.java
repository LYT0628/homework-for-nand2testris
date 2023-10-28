package icu.lyt;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
    public static List<File> walk(File path){
        if(!path.exists()) {
            throw new IllegalArgumentException("path is not exists");
        }

        List<File> results = new ArrayList<>();
        if(path.isDirectory()){
            for (File subPath : path.listFiles()) {
                results.addAll(walk(subPath));
            }
        }else {
            results.add(path);
        }
        return results;
    }

    static  boolean  isVmFile(File file){
        if (file.isDirectory()){
            return false;
        }
        if (!"vm".equals(
                file.getName().substring(file.getName().lastIndexOf(".")+1))){
            return false;
        }
        return true;
    }

    /**
     * 得到VM文件对应的asm文件名
     * @author : lyt0628
     */
    public static String asmFilename(String vmFilename){
        return vmFilename.substring(0, vmFilename.lastIndexOf("."))+".asm";
    }
}