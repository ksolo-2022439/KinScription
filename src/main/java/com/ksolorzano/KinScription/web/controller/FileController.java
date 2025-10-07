package com.ksolorzano.KinScription.web.controller;

import com.ksolorzano.KinScription.dominio.service.FileSystemStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FileController {

    private final FileSystemStorageService storageService;

    @Autowired
    public FileController(FileSystemStorageService storageService) {
        this.storageService = storageService;
    }

    /**
     * Endpoint para servir los archivos subidos.
     * Lee un archivo del directorio de almacenamiento y lo devuelve al navegador
     * para que pueda ser mostrado.
     * @param filename El nombre del archivo a servir.
     * @return Un ResponseEntity con el contenido del archivo.
     */
    @GetMapping("/uploads/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = storageService.loadAsResource(filename); // Necesitaremos crear este método

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }
}