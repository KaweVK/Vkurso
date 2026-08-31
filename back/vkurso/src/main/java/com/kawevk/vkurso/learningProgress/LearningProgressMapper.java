package com.kawevk.vkurso.learningProgress;

import com.kawevk.vkurso.learningProgress.dtos.LearningProgressResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LearningProgressMapper {

    LearningProgressResponse toResponse(LearningProgress progress);
}
