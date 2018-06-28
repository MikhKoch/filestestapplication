package com.example.filestestapp.utils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DecimalFormat;
import java.util.Date;

public class FileUtils {

    public FileUtils() {
    }

    public long getFileSize(File file) {
        long length = 0;
        if(!file.isDirectory()) {
            length = file.length();
        } else {
            File[] files = file.listFiles();
            int count = files.length;
            for (int i = 0; i < count; i++) {
                if (files[i].isFile()) {
                    length += files[i].length();
                } else {
                    length += getFileSize(files[i]);
                }
            }
        }
        return length;
    }

    public Date getFileCreationTime(File file) {
        Date date;
        try {
            date = new Date(Files.readAttributes(file.toPath(), BasicFileAttributes.class).lastModifiedTime().toMillis());
        } catch (Exception e) {
            date = null;
        }
        return date;
    }

    public String formatFileLength(long size) {
        if(size <= 0) return "0";
        final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}
