package com.ksolorzano.KinScription.web.controller.api;

import com.ksolorzano.KinScription.dominio.dto.CursoApiDto; // <-- Importar el nuevo DTO
import com.ksolorzano.KinScription.dominio.service.GradoCursoService;
import com.ksolorzano.KinScription.persistence.entity.GradoCurso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/gradocursos")
public class GradoCursoApiController {

    private final GradoCursoService gradoCursoService;

    @Autowired
    public GradoCursoApiController(GradoCursoService gradoCursoService) {
        this.gradoCursoService = gradoCursoService;
    }

    /**
     * Devuelve una lista de DTOs de Cursos asociados a un Grado específico.
     * @param idGrado El ID del Grado.
     * @return Una lista de DTOs de Curso (CursoApiDto).
     */
    @GetMapping("/grado/{idGrado}/cursos")
    public List<CursoApiDto> getCursosPorGrado(@PathVariable Integer idGrado) {
        return gradoCursoService.buscarPorFiltro(idGrado)
                .stream()
                .map(GradoCurso::getCurso)
                .map(CursoApiDto::new)
                .collect(Collectors.toList());
    }
}