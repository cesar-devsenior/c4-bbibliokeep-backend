package com.devsenior.cdiaz.bibliokeep.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.devsenior.cdiaz.bibliokeep.exception.BadRequestException;
import com.devsenior.cdiaz.bibliokeep.model.dto.file.UploadResponse;
import com.devsenior.cdiaz.bibliokeep.service.FileService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FileServiceImpl implements FileService {

    @Value("${app.file.directory}")
    private String directoryName;

    @Value("${app.file.publish-path}")
    private String publishPathName;

    @Override
    public UploadResponse upload(MultipartFile file) {
        try {
            // Verificar que el archivo tenga contenido
            if (file.isEmpty()) {
                log.warn("El archivo está vacío");
                throw new BadRequestException("El archivo está vacío");
            }

            // Verificar la existencia de la carpeta donde se cargaran los archivos, si no,
            // se crea la carpeta
            var directory = Paths.get(directoryName);
            if (!Files.exists(directory)) {
                log.warn("El directorio no existe. Se crea el directorio.");
                Files.createDirectories(directory);
            }

            // Copiar el contenido del archivo a un archivo a un nuevo archivo en la carpeta
            var originalFileName = file.getOriginalFilename();
            var extension = originalFileName.substring(originalFileName.lastIndexOf('.') + 1);
            if(extension.equals("blob")) {
                extension = "webp";
            }
            var filename = String.format("%s.%s", UUID.randomUUID().toString(), extension);
            Files.copy(file.getInputStream(), directory.resolve(filename));

            // Generar una URL publica para consumir el archivo
            var url = "%s/%s".formatted(publishPathName, filename);

            return new UploadResponse(filename, url, file.getSize());

        } catch (IOException ex) {
            log.error("Error al guardar el archivo en el disco", ex);
            throw new BadRequestException("Error al guardar el archivo en el disco");
        }
    }
}
