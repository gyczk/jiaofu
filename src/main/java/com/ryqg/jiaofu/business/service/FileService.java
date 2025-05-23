package com.ryqg.jiaofu.business.service;

import com.ryqg.jiaofu.business.common.FileInfo;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    FileInfo upload(MultipartFile file);
}
