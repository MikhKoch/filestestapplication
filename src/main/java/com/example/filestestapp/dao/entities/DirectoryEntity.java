package com.example.filestestapp.dao.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "DIRECTORY")
public class DirectoryEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID_DIRECTORYPATH")
    public String id;

    @Column(name = "DATE")
    public Date date;

    @Column(name = "IS_FILE")
    public boolean isFile;

    @Column(name = "SIZE")
    public long size;

    @Column(name = "SUBDIRECTORIES_COUNT")
    public int subDirectoriesCount;

    @Column(name = "FILES_COUNT")
    public int filesCount;

    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    public List<SubDirectoryEntity> subDirectories;

    public DirectoryEntity() {
    }
}
