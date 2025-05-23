package com.ryqg.jiaofu.business.controller;

import com.ryqg.jiaofu.business.common.FileInfo;
import com.ryqg.jiaofu.business.service.FileService;
import com.ryqg.jiaofu.common.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/files")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @PostMapping
    public Result<FileInfo> upload(@RequestParam("file") MultipartFile file) {
        FileInfo fileinfo = fileService.upload(file);
        return Result.success(fileinfo);
    }


}
