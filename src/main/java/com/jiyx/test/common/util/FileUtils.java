package com.jiyx.test.common.util;

import java.io.File;

/**
 * auther: jiyx
 * date: 2018/11/24.
 */
public class FileUtils {

    /**
     * 获取路径下的所有文件
     * @param path
     * @return
     */
    public static File[] getFilsByPath(String path) {
        if (isExist(path)) {
            throw new IllegalArgumentException("输入的" + path + "不存在");
        }

        File file = new File(path);
        if (file.isDirectory()) {
            throw new IllegalArgumentException("输入的" + path + "不是目录");
        }

        return file.listFiles();
    }

    /**
     * 判断文件是否存在
     * @param path
     * @return
     */
    public static boolean isExist(String path) {
        File file = new File(path);
        return file.exists();
    }

}
