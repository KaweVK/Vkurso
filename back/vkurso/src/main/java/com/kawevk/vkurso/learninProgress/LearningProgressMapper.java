package com.kawevk.vkurso.learninProgress;

import com.kawevk.vkurso.learninProgress.dtos.LearningProgressResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LearningProgressMapper {

    LearningProgressResponse toResponse(LearningProgress progress);
}
