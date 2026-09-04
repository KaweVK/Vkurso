package com.kawevk.vkurso.lesson;


import com.kawevk.vkurso.lesson.dtos.LessonResponse;
import com.kawevk.vkurso.module.ModuleMapper;
import com.kawevk.vkurso.shared.storage.VideoStorageService;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LessonMapper {

    @Mapping(source = "module.id", target = "moduleId")
    @Mapping(target = "videoUrl", ignore = true)
    @Mapping(
            target = "durationSeconds",
            expression = "java(lesson.getDurationSeconds() != null ? lesson.getDurationSeconds().getSeconds() : null)"
    )
    LessonResponse toResponseWhitoutUrl (Lesson lesson);

    @Mapping(source = "module.id", target = "moduleId")
    @Mapping(target = "videoUrl", expression = "java(lesson.getVideoKey() == null ? null : storage.presignedGetUrl(lesson.getVideoKey()))")
    @Mapping(
            target = "durationSeconds",
            expression = "java(lesson.getDurationSeconds() != null ? lesson.getDurationSeconds().getSeconds() : null)"
    )
    LessonResponse toResponseWhitUrl (Lesson lesson, @Context VideoStorageService storage);
}
