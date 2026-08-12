package com.kawevk.vkurso.course;

import com.kawevk.vkurso.course.dtos.CourseResponse;
import com.kawevk.vkurso.course.dtos.CreateCourseRequest;
import com.kawevk.vkurso.course.dtos.UpdateCourseRequest;
import com.kawevk.vkurso.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/api/courses")
@Tag(
        name = "Courses",
        description = "Endpoints para gerenciamento do curso"
)
public class CourseController {

    private final CourseService service;

    public CourseController(CourseService service) {
        this.service = service;
    }

    @Operation(
            summary = "Listar cursos",
            description = "Retorna uma lista paginada de cursos disponíveis."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Página de cursos retornada com sucesso (vazia ou não)"
            ),
    })
    @GetMapping
    public Page<CourseResponse> list(Pageable pageable) {
        log.info("Listing courses. page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
        return service.list(pageable);
    }

    @Operation(
            summary = "Obter curso por ID",
            description = "Retorna os detalhes de um curso específico com base em seu ID."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Curso retornado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Curso não encontrado"
            ),
    })
    @GetMapping("/id/{id}")
    public CourseResponse get(
            @Parameter(description = "ID do curso", example = "1") @PathVariable Long id) {
        log.info("Getting course by ID: {}", id);
        return service.get(id);
    }

    @Operation(
            summary = "Obter curso por slug",
            description = "Retorna os detalhes de um curso específico com base em seu slug = Título do Curso -> titulo-do-curso."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Curso retornado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Curso não encontrado"
            ),
    })
    @GetMapping("/{slug}")
    public CourseResponse getBySlug(
            @Parameter(description = "Slug do curso", example = "titulo-do-curso") @PathVariable String slug) {
        log.info("Getting course by slug: {}", slug);
        return service.getBySlug(slug);
    }

    @Operation(
            summary = "Listar cursos por instrutor",
            description = "Retorna uma lista paginada dos cursos publicados por um instrutor específico."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Cursos retornados com sucesso"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Instrutor não encontrado"
            ),
    })
    @GetMapping("/by-instructor")
    public Page<CourseResponse> listByInstructor(
            @Parameter(description = "ID do instrutor", example = "1") @RequestParam Long instructorId, Pageable pageable) {
        log.info("Listing courses by instructor. instructorId={}, page={}, size={}", instructorId, pageable.getPageNumber(), pageable.getPageSize());
        return service.listByInstructor(instructorId, pageable);
    }

    @Operation(
            summary = "Criar curso",
            description = "Cria um novo curso com os dados fornecidos."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Curso criado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Curso com slug já existente"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acesso não autorizado"
            ),
    })
    @PostMapping
    public ResponseEntity<CourseResponse> create(@RequestBody @Valid CreateCourseRequest request, UriComponentsBuilder uriBuilder, @AuthenticationPrincipal User user) {
        log.info("Creating course. Request={}", request);
        CourseResponse created = service.create(request, user);
        URI location = uriBuilder.path("/api/courses/{id}")
                .buildAndExpand(created.id())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @Operation(
            summary = "Adicionar categoria ao curso",
            description = "Adiciona uma categoria a um curso específico."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Categoria adicionada com sucesso"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Categoria/curso não existente"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acesso não autorizado"
            ),
    })
    @PostMapping("/{id}/categories/{idCategory}")
    public CourseResponse addCategory(
            @Parameter(description = "ID do curso", example = "1") @PathVariable Long id,
            @Parameter(description = "ID da categoria", example = "1") @PathVariable Long idCategory,
            @AuthenticationPrincipal User user) {
        log.info("Adding category to course. courseId={}, categoryId={}", id, idCategory);
        return service.addCategory(id, idCategory, user);
    }

    @Operation(
            summary = "Remover categoria do curso",
            description = "Remove uma categoria de um curso específico."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Categoria removida com sucesso"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Categoria/curso não existente"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acesso não autorizado"
            ),
    })
    @DeleteMapping("/{id}/categories/{idCategory}")
    public CourseResponse removeCategory(
            @Parameter(description = "ID do curso", example = "1") @PathVariable Long id,
            @Parameter(description = "ID da categoria", example = "1") @PathVariable Long idCategory,
            @AuthenticationPrincipal User user) {
        log.info("Removing category from course. courseId={}, categoryId={}", id, idCategory);
        return service.removeCategory(id, idCategory, user);
    }

    @Operation(
            summary = "Atualizar curso",
            description = "Atualiza os dados de um curso específico."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Curso atualizado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Curso não existente"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acesso não autorizado"
            ),
    })
    @PutMapping("/{id}")
    public CourseResponse update(
            @Parameter(description = "ID do curso", example = "1") @PathVariable Long id,
            @RequestBody @Valid UpdateCourseRequest request,
            @AuthenticationPrincipal User user) {
        log.info("Updating course. courseId={}, request={}", id, request);
        return service.update(id, request, user);
    }

    @Operation(
            summary = "Publicar curso",
            description = "Publica um curso específico."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Curso publicado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Curso não existente"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acesso não autorizado"
            ),
    })
    @PostMapping("/{id}/publish")
    public CourseResponse publish(
            @Parameter(description = "ID do curso", example = "1") @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        log.info("Publishing course. courseId={}", id);
        return service.publish(id, user);
    }

    @Operation(
            summary = "Arquivar curso",
            description = "Arquiva um curso específico."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Curso arquivado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Curso não existente"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acesso não autorizado"
            ),
    })
    @PostMapping("/{id}/archive")
    public CourseResponse archive(
            @Parameter(description = "ID do curso", example = "1") @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        log.info("Archiving course. courseId={}", id);
        return service.archive(id, user);
    }

    @Operation(
            summary = "Deletar curso",
            description = "Remove um curso específico."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Curso deletado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Curso não existente"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acesso não autorizado"
            ),
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @Parameter(description = "ID do curso", example = "1") @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        log.info("Deleting course. courseId={}", id);
        service.delete(id, user);
    }

    @Operation(
            summary = "Mudar ordem dos módulos",
            description = "Modifica a ordem que os módulos são apresentados"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Ordem dos módulos atualizada com sucesso"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Curso/Módulo não existente"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acesso não autorizado"
            ),
    })
    @PutMapping("/{id}/modules/{moduleId}/order")
    public CourseResponse order(
            @Parameter(description = "ID do curso", example = "1") @PathVariable Long id,
            @Parameter(description = "ID do módulo", example = "1") @PathVariable Long moduleId,
            @Parameter(description = "Nova ordem", example = "1") @RequestParam int newOrder,
            @AuthenticationPrincipal User user) {
        log.info("Changing module order. courseId={}, moduleId={}, newOrder={}", id, moduleId, newOrder);
        return service.changeModuleOrder(id, moduleId, newOrder, user);
    }
}