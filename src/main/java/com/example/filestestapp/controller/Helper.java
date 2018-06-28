package com.example.filestestapp.controller;

import com.example.filestestapp.dao.entities.DirectoryEntity;
import com.example.filestestapp.dao.entities.SubDirectoryEntity;
import com.example.filestestapp.model.DirectoryDTO;
import com.example.filestestapp.model.SubDirectoryDTO;
import com.example.filestestapp.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.function.Function;


public class Helper {

    private FileUtils fileUtils;
    private Function<DirectoryEntity, DirectoryDTO> directoryMapFunction;
    private Function<SubDirectoryEntity, SubDirectoryDTO> subDirectoryMapFunction;
    private Comparator<SubDirectoryDTO> subDirectoryComparator;
    private SimpleDateFormat simpleDateFormat;

    @Autowired
    public Helper(FileUtils fileUtils,
                  Function<DirectoryEntity, DirectoryDTO> directoryMapFunction,
                  Function<SubDirectoryEntity, SubDirectoryDTO> subDirectoryMapFunction,
                  Comparator<SubDirectoryDTO> subDirectoryComparator,
                  SimpleDateFormat simpleDateFormat) {
        this.fileUtils = fileUtils;
        this.directoryMapFunction = directoryMapFunction;
        this.subDirectoryMapFunction = subDirectoryMapFunction;
        this.subDirectoryComparator = subDirectoryComparator;
        this.simpleDateFormat = simpleDateFormat;
    }

    public Comparator<SubDirectoryDTO> getSubDirectoryComparator() {
        return subDirectoryComparator;
    }

    public Function<DirectoryEntity, DirectoryDTO> getDirectoryMapFunction() {
        return directoryMapFunction;
    }

    public Function<SubDirectoryEntity, SubDirectoryDTO> getSubDirectoryMapFunction() {
        return subDirectoryMapFunction;
    }

    public DirectoryDTO map(DirectoryEntity directory) {
        return new DirectoryDTO(simpleDateFormat.format(directory.date),
                directory.id,
                fileUtils.formatFileLength(directory.size),
                directory.subDirectoriesCount,
                directory.filesCount,
                directory.isFile);
    }

}
