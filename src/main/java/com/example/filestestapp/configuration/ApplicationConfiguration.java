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
import org.springframework.util.NumberUtils;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import javax.persistence.criteria.CriteriaBuilder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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
                //паттерн выражения для литерала только из символов и строк
                Pattern expSplitPattern = Pattern.compile("[A-Za-z0-9А-Яа-я]+");
                //паттерн выражения для разделения выражения на циферные и буквенные части
                Pattern numberLettersSplitPattern = Pattern.compile("([0-9]+)|([a-zA-zА-Яа-я]+)");
                //выражение для цифры
                String numberExp = "[0-9]+";

                //заполнение списков выражений из символов и строк
                Matcher expMatcher1 = expSplitPattern.matcher(sub1.name);
                Matcher expMatcher2 = expSplitPattern.matcher(sub2.name);

                List<String> expMatches1 = new ArrayList<>();
                List<String> expMatches2 = new ArrayList<>();

                while (expMatcher1.find()) { // find next match
                    expMatches1.add(expMatcher1.group());
                }
                while (expMatcher2.find()) { // find next match
                    expMatches2.add(expMatcher2.group());
                }
                //окончание заполнения

                for (int i = 0; i < Integer.min(expMatches1.size(), expMatches2.size()); i++) {
                    //заполнение списков строками и цифрами
                    Matcher numberLettersMatcher1 = numberLettersSplitPattern.matcher(expMatches1.get(i));
                    Matcher numberLettersMatcher2 = numberLettersSplitPattern.matcher(expMatches2.get(i));

                    List<String> numberLettersMatches1 = new ArrayList<>();
                    List<String> numberLettersMatches2 = new ArrayList<>();

                    while (numberLettersMatcher1.find()) { // find next match
                        numberLettersMatches1.add(numberLettersMatcher1.group());
                    }
                    while (numberLettersMatcher2.find()) { // find next match
                        numberLettersMatches2.add(numberLettersMatcher2.group());
                    }
                    //окончание заполнения
                    for (int j = 0; j < Integer.min(numberLettersMatches1.size(), numberLettersMatches2.size()); j++) {
                        String numberLettersMatch1 = numberLettersMatches1.get(j);
                        String numberLettersMatch2 = numberLettersMatches2.get(j);
                        if (!numberLettersMatch1.matches(numberExp) && numberLettersMatch2.matches(numberExp)){
                            return 1;
                        } else if (numberLettersMatch1.matches(numberExp) && !numberLettersMatch2.matches(numberExp)) {
                            return -1;
                        } else if(numberLettersMatch1.matches(numberExp) && numberLettersMatch2.matches(numberExp)) {
                            int num1 = Integer.parseInt(numberLettersMatch1);
                            int num2 = Integer.parseInt(numberLettersMatch2);
                            if (num1 > num2) {
                                return 1;
                            } else if (num1 < num2) {
                                return -1;
                            } else if (numberLettersMatch1.length() > numberLettersMatch2.length()) {
                                return -1;
                            } else if (numberLettersMatch1.length() < numberLettersMatch2.length()) {
                                return 1;
                            }
                        } else if (numberLettersMatch1.compareToIgnoreCase(numberLettersMatch2) > 0) {
                            return 1;
                        } else if (numberLettersMatch1.compareToIgnoreCase(numberLettersMatch2) < 0) {
                            return -1;
                        }
                    }
                    if (expMatches1.size() > expMatches2.size()) {
                        return -1;
                    } else if (expMatches1.size() < expMatches2.size()) {
                        return 1;
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
