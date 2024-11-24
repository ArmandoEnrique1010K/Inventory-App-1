package com.inventory.app.services;

import java.nio.file.Path;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    // Crea el contenedor de imagenes
    public void createFolder();

    // Almacena una imagen
    public String storeImage(MultipartFile image);

    // Carga una imagen
    public Path loadImage(String name);

    // Carga el archivo como recurso
    public Resource loadResource(String name);

}
