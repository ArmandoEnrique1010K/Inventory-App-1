package com.inventory.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventory.app.services.ImageService;

// Controlador REST para manejar solicitudes relacionadas con imágenes.
// RestController devuelve solamente datos como algun contenido en formato JSON o una imagen
@RestController
@RequestMapping("/image")
public class ImageController {

    // Instancia del servicio para gestionar imágenes
    @Autowired
    private ImageService imageService;

    // Endpoint para visualizar la imagen obtenida desde la carpeta en tamaño
    // completo,
    // requiere de un parametro (el nombre del archivo de la imagen)
    @GetMapping("/{name:.+}")
    public ResponseEntity<Resource> previewImageByName(@PathVariable("name") String name) {

        // Carga la imagen como recurso desde el servicio de imágenes
        Resource resource = imageService.loadResource(name);

        // Retorna la imagen en el formato adecuado (JPEG, PNG, GIF)
        return ResponseEntity.ok()
                // Define el tipo de contenido
                .contentType(MediaType.IMAGE_JPEG)
                .contentType(MediaType.IMAGE_PNG)
                .contentType(MediaType.IMAGE_GIF)
                .body(resource);

    }

}