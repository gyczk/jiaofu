package com.ryqg.jiaofu.business.service.impl;

import com.ryqg.jiaofu.business.common.FileInfo;
import com.ryqg.jiaofu.business.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final LocalFileService localFileService;
    @Override
    public FileInfo upload(MultipartFile file) {
        return localFileService.uploadFile(file);
    }
}
