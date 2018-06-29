package com.example.filestestapp.configuration;

import com.example.filestestapp.controller.Helper;
import com.example.filestestapp.dao.entities.DirectoryEntity;
import com.example.filestestapp.dao.entities.SubDirectoryEntity;
import com.example.filestestapp.model.DirectoryDTO;
import com.example.filestestapp.model.SubDirectoryDTO;
import com.example.filestestapp.utils.FileUtils;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Configuration
public class ApplicationConfiguration {

    @Bean
    SessionFactory getSessionFactory() {
        org.hibernate.cfg.Configuration cfg = new org.hibernate.cfg.Configuration().configure();
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                .applySettings(cfg.getProperties());
        return cfg.buildSessionFactory(builder.build());
    }

    @Bean
    public ViewResolver getViewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

    @Bean
    public FileUtils getFileUtils() {
        return new FileUtils();
    }

    @Bean
    public Helper getMapper(FileUtils fileUtils,
                            Function<DirectoryEntity, DirectoryDTO> directoryMapFunction,
                            Function<SubDirectoryEntity, SubDirectoryDTO> subDirectoryMapFunction,
                            Comparator<SubDirectoryDTO> subDirectoryComparator,
                            SimpleDateFormat simpleDateFormat) {
        return new Helper(fileUtils, directoryMapFunction, subDirectoryMapFunction, subDirectoryComparator, simpleDateFormat);
    }

    @Bean
    public Function<DirectoryEntity, DirectoryDTO> getDirectoryMapFunction(FileUtils fileUtils, SimpleDateFormat simpleDateFormat) {
        return (directoryEntity -> new DirectoryDTO(simpleDateFormat.format(directoryEntity.date),
                directoryEntity.id,
                fileUtils.formatFileLength(directoryEntity.size),
                directoryEntity.subDirectoriesCount,
                directoryEntity.filesCount,
                directoryEntity.isFile));
    }

    @Bean
    public Function<SubDirectoryEntity, SubDirectoryDTO> getSubDirectoryMapFunction(FileUtils fileUtils) {
        return (subdirectoryEntity -> new SubDirectoryDTO(subdirectoryEntity.name,
                fileUtils.formatFileLength(subdirectoryEntity.size),
                subdirectoryEntity.isFile));
    }

    @Bean
    Comparator<SubDirectoryDTO> getSubDirectoryComparator() {
        return ((sub1, sub2) -> {
            if (!sub1.isFile() && sub2.isFile()) {
                return -1;
            } else if (sub1.isFile() && !sub2.isFile()) {
                return 1;
            } else {
                Pattern pattern = Pattern.compile("[A-Za-z0-9А-Яа-я]+");
                Matcher m1 = pattern.matcher(sub1.name);
                Matcher m2 = pattern.matcher(sub2.name);
                while (m1.find() && m2.find()) {
                    int compareVal = m1.group().compareToIgnoreCase(m2.group());
                    if(compareVal != 0) {
                        return compareVal;
                    }
                }
                return 0;
            }
        });
    }

    @Bean
    public SimpleDateFormat getSimpleDateFormat() {
        return new SimpleDateFormat("dd.MM.yyyy hh:mm");
    }
}
