package com.example.filestestapp.utils;

import com.example.filestestapp.model.DirectoryDTO;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tags {
    public static List<DirectoryDTO> sort(List<DirectoryDTO> directories) {
        Collections.sort(directories, (d1,d2) -> {
                    if (!d1.isFile() && d2.isFile()) {
                        return -1;
                    } else if (d1.isFile() && !d2.isFile()) {
                        return 1;
                    } else {
                        Pattern pattern = Pattern.compile("[A-Za-z0-9А-Яа-я]+");
                        Matcher m1 = pattern.matcher(d1.path);
                        Matcher m2 = pattern.matcher(d2.path);
                        while (m1.find() && m2.find()) {
                            int compareVal = m1.group().compareToIgnoreCase(m2.group());
                            if(compareVal != 0) {
                                return compareVal;
                            }
                        }
                        return 0;
                    }
                });
        return directories;
    }
}
