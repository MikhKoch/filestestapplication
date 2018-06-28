package com.example.filestestapp.model;

public class SubDirectoryDTO extends AbstractDTO {

    public String name;
    public String size;
    boolean isFile;

    public SubDirectoryDTO(String name, String size, boolean isFile) {
        this.name = name;
        this.size = size;
        this.isFile = isFile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public boolean isFile() {
        return isFile;
    }

    public void setFile(boolean file) {
        isFile = file;
    }
}
