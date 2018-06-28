package com.example.filestestapp.controller;

import com.example.filestestapp.dao.DirectoryDAO;
import com.example.filestestapp.dao.entities.DirectoryEntity;
import com.example.filestestapp.dao.entities.SubDirectoryEntity;
import com.example.filestestapp.model.DirectoryDTO;
import com.example.filestestapp.model.ajax.AjaxResponse;
import com.example.filestestapp.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Controller
public class FilesController {

    private DirectoryDAO directoryDAO;
    private FileUtils fileUtils;
    private Helper helper;

    @Autowired
    public FilesController(DirectoryDAO directoryDAO, FileUtils fileUtils, Helper helper) {
        this.directoryDAO = directoryDAO;
        this.fileUtils = fileUtils;
        this.helper = helper;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String showAllFiles(Model model) {
        List<DirectoryDTO> directories = directoryDAO.getAllDirictories()
                .stream().map(helper.getDirectoryMapFunction()).collect(Collectors.toList());
        model.addAttribute("directories", directories);
        return "filelist";
    }

    @RequestMapping(value = "/directory", method = RequestMethod.GET)
    public String getSubdirectory(@RequestParam(name = "path") String directoryPath, Model model) {
        model.addAttribute("subdirectories", directoryDAO.getByPath(directoryPath).subDirectories.stream()
                .map(helper.getSubDirectoryMapFunction())
                .sorted(helper.getSubDirectoryComparator())
                .collect(Collectors.toList())
        );
        model.addAttribute("path", directoryPath);
        return "subdirectories";
    }

    @RequestMapping(value = "/add_directory", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse<DirectoryDTO> addDirectory(@RequestBody Map<String, String> map) {
        AjaxResponse<DirectoryDTO> directoryDTOAjaxResponse = new AjaxResponse<>();
        File file = new File(map.get("path"));
        if (!file.exists()) {
            directoryDTOAjaxResponse.setMsg("Директория или файл не существуют");
        } else {
            DirectoryEntity directoryEntity = new DirectoryEntity();
            List<SubDirectoryEntity> subDirectories = new ArrayList<>();
            directoryEntity.id = file.getPath().replace("\\", "/");
            directoryEntity.date = fileUtils.getFileCreationTime(file);
            if (!file.isDirectory()) {
                directoryEntity.isFile = true;
                directoryEntity.size = fileUtils.getFileSize(file);
            } else {
                directoryEntity.isFile = false;
                File[] subfiles = file.listFiles();
                for (File subfile : subfiles) {
                    boolean isFile = subfile.isFile();
                    long subfileSize = fileUtils.getFileSize(subfile);
                    directoryEntity.size += subfileSize;
                    subDirectories.add(new SubDirectoryEntity(subfile.getName(), isFile, subfileSize));
                    if (isFile) {
                        directoryEntity.filesCount++;
                    } else {
                        directoryEntity.subDirectoriesCount++;
                    }
                }
            }
            directoryDAO.addDirectory(directoryEntity, subDirectories);
            directoryDTOAjaxResponse.setResponse(helper.map(directoryEntity));
        }
        return directoryDTOAjaxResponse;
    }
}

