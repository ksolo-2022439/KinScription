package com.ksolorzano.KinScription.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja la excepción que se lanza cuando un archivo subido excede el tamaño máximo configurado.
     * Redirige al usuario a la página desde la que vino con un mensaje de error claro.
     * @param redirectAttributes Para añadir el mensaje flash de error.
     * @param request Para obtener la URL de la página anterior (el referer).
     * @return Una cadena de redirección a la página del formulario.
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxSizeException(RedirectAttributes redirectAttributes, HttpServletRequest request) {

        redirectAttributes.addFlashAttribute("errorMessage", "Error: El archivo supera el tamaño máximo permitido (5MB).");

        // Obtenemos la URL de la página anterior para redirigir al usuario de vuelta a ella.
        String referer = request.getHeader("Referer");

        return "redirect:" + (referer != null ? referer : "/");
    }
}