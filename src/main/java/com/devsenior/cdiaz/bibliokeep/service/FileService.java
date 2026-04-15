package com.devsenior.cdiaz.bibliokeep.service;

import org.springframework.web.multipart.MultipartFile;

import com.devsenior.cdiaz.bibliokeep.model.dto.file.UploadResponse;

public interface FileService {
    UploadResponse upload(MultipartFile file);
}
