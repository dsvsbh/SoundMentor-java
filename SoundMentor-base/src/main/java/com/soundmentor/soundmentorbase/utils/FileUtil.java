package com.soundmentor.soundmentorbase.utils;

import com.soundmentor.soundmentorbase.enums.ResultCodeEnum;
import com.soundmentor.soundmentorbase.exception.BizException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 文件工具类
 * author: lzc
 * date: 2025/02/05
 */
public class FileUtil {
    /**
     * 创建文件夹
     * @param path 文件夹路径
     * @return 文件夹
     */
    public static void createFolder(String path) {
        File file = new File(path);
        if(file.exists())
        {
            throw new BizException(ResultCodeEnum.FILE_ERROR.getCode(),"该路径已存在文件或文件夹");
        }
        try {
            file.mkdirs();
        } catch (Exception e) {
            throw new BizException(ResultCodeEnum.FILE_ERROR.getCode(),"创建文件夹失败");
        }
    }

    /**
     * zip压缩文件夹
     * @param path
     * @return 压缩后的文件流
     */
    public static InputStream zipFolder(String path) {
        try {
            File folder = new File(path);
            if (!folder.exists() || !folder.isDirectory()) {
                throw new BizException(ResultCodeEnum.FILE_ERROR.getCode(), "文件夹不存在");
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (ZipOutputStream zos = new ZipOutputStream(baos)) {
                zipFile(folder, folder.getName(), zos);
            }

            return new ByteArrayInputStream(baos.toByteArray());
        } catch (Exception e) {
            throw new BizException(ResultCodeEnum.FILE_ERROR.getCode(), "压缩文件夹失败");
        }
    }
    /**
     * 递归压缩文件夹
     * @param file 当前文件或文件夹
     * @param parentName 父文件夹名称
     * @param zos Zip输出流
     * @throws Exception 异常
     */
    private static void zipFile(File file, String parentName, ZipOutputStream zos) throws Exception {
        if (file.isDirectory()) {
            for (File childFile : file.listFiles()) {
                zipFile(childFile, parentName + "/" + childFile.getName(), zos);
            }
        } else {
            try (FileInputStream fis = new FileInputStream(file)) {
                ZipEntry zipEntry = new ZipEntry(parentName);
                zos.putNextEntry(zipEntry);
                byte[] bytes = new byte[1024];
                int length;
                while ((length = fis.read(bytes)) >= 0) {
                    zos.write(bytes, 0, length);
                }
                zos.closeEntry();
            }
        }
    }
}
class Test {
    public static void main(String[] args) {
//        FileUtil.createFolder("temp/test");
        InputStream inputStream = FileUtil.zipFolder("temp/test");
        try {
            Files.copy(inputStream, Paths.get("temp/test.zip"));
        } catch (IOException e) {
            System.out.println("文件复制失败");
        }
    }
}

