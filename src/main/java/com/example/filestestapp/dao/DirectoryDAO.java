package com.example.filestestapp.dao;

import com.example.filestestapp.dao.entities.DirectoryEntity;
import com.example.filestestapp.dao.entities.SubDirectoryEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DirectoryDAO {

    private SessionFactory sessionFactory;

    @Autowired
    DirectoryDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public DirectoryEntity getByPath(String path) {
        Session session = sessionFactory.openSession();
        DirectoryEntity directoryEntity = (DirectoryEntity) session.get(DirectoryEntity.class, path);
        session.close();
        return directoryEntity;
    }

    public List<DirectoryEntity> getAllDirictories() {
        Session session = sessionFactory.openSession();
        List<DirectoryEntity> list =  session.createQuery("from " + DirectoryEntity.class.getName()).list();
        session.close();
        return list;
    }

    public void addDirectory(DirectoryEntity directoryEntity, List<SubDirectoryEntity> subDirectories) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        directoryEntity.subDirectories = subDirectories;
        session.save(directoryEntity);
        for (SubDirectoryEntity sub : subDirectories) {
            sub.directoryEntity = directoryEntity;
            session.save(sub);
        }
        session.getTransaction().commit();
        session.close();
    }
}
