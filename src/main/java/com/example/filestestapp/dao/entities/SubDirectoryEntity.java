package com.example.filestestapp.dao.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "SUBDIRECTORIES")
public class SubDirectoryEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID_SUBDIRECTORY")
    @GeneratedValue
    public Long id;

    @Column(name = "NAME", nullable = false)
    public String name;

    @Column(name = "IS_FILE")
    public boolean isFile;

    @Column(name = "SIZE")
    public long size;

    @ManyToOne
    @JoinColumn(name = "ID_DIRECTORYPATH")
    public DirectoryEntity directoryEntity;

    public SubDirectoryEntity(String name, boolean isFile, long size) {
        this.name = name;
        this.isFile = isFile;
        this.size = size;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public SubDirectoryEntity() {
    }
}
