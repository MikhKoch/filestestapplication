package com.example.filestestapp.model;

public class DirectoryDTO extends AbstractDTO {

    public String date;
    public String path;
    public String size;
    public int directoriesCount;
    public int filesCount;
    public boolean isFile;

    public DirectoryDTO(String date, String path, String size, int directoriesCount, int filesCount, boolean isFile) {
        this.date = date;
        this.path = path;
        this.size = size;
        this.directoriesCount = directoriesCount;
        this.filesCount = filesCount;
        this.isFile = isFile;
    }

    public DirectoryDTO() {
    }

    public  String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getDirectoriesCount() {
        return directoriesCount;
    }

    public void setDirectoriesCount(int directoriesCount) {
        this.directoriesCount = directoriesCount;
    }

    public int getFilesCount() {
        return filesCount;
    }

    public void setFilesCount(int filesCount) {
        this.filesCount = filesCount;
    }

    public boolean isFile() {
        return isFile;
    }

    public void setFile(boolean file) {
        isFile = file;
    }
}
