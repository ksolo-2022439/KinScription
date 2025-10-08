package com.ksolorzano.KinScription.web.controller.api;

import com.ksolorzano.KinScription.dominio.dto.SolicitudDto;
import com.ksolorzano.KinScription.dominio.service.AIServiceColegio;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final AIServiceColegio aiService;

    public AiController(AIServiceColegio aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/sugerir")
    public ResponseEntity<String> generarSugerenciaActividad(@RequestBody SolicitudDto solicitudDto) {
        return ResponseEntity.ok(this.aiService.generarActividad(solicitudDto.preferencias()));
    }
}