package com.ryqg.jiaofu.business.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.extra.ftp.Ftp;
import cn.hutool.extra.ftp.FtpMode;
import com.ryqg.jiaofu.business.common.FileInfo;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * 本地存储服务类
 *
 * @author Theo
 * @since 2024-12-09 17:11
 */
@Data
@Slf4j
@Component
//@ConditionalOnProperty(value = "oss.type", havingValue = "local")
//@ConfigurationProperties(prefix = "oss.local")
@RequiredArgsConstructor
public class LocalFileService  {//implements FileService

//    @Value("${oss.local.storage-path}")
//    private String storagePath = "g://123//";

    /**
     * 上传文件方法
     *
     * @param file 表单文件对象
     * @return 文件信息
     */
    public FileInfo uploadFile(MultipartFile file) {
        // 获取文件名
        String originalFilename = file.getOriginalFilename();
        // 获取文件后缀
        String suffix = FileUtil.getSuffix(originalFilename);
        // 生成uuid
        String fileName = IdUtil.simpleUUID()+ "." + suffix;
        // 生成文件名(日期文件夹)
        String folder = DateUtil.format(LocalDateTime.now(), DatePattern.PURE_DATE_PATTERN);
        try (Ftp ftp = new Ftp("117.72.56.61", 21, "ftpuser", "ftpuser")) {
            ftp.setMode(FtpMode.Passive);
            ftp.cd("images");
            ftp.mkdir(folder);
            ftp.upload(folder,fileName,file.getInputStream());
        } catch (IOException e) {
            log.error("文件上传失败", e);
            throw new RuntimeException("文件上传失败");
        }

        // 获取文件访问路径，因为这里是本地存储，所以直接返回文件的相对路径，需要前端自行处理访问前缀
        String fileUrl = "http://117.72.56.61/" + folder + "/" + fileName;
        FileInfo fileInfo = new FileInfo();
        fileInfo.setName(originalFilename);
        fileInfo.setUrl(fileUrl);
        return fileInfo;
    }


//    /**
//     * 删除文件
//     * @param filePath 文件完整URL
//     * @return 是否删除成功
//     */
//    public boolean deleteFile(String filePath) {
//        //判断文件是否为空
//        if (filePath == null || filePath.isEmpty()) {
//            return false;
//        }
//        // 判断filepath是否为文件夹
//        if (FileUtil.isDirectory(storagePath + filePath)) {
//            // 禁止删除文件夹
//            return false;
//        }
//        // 删除文件
//        return FileUtil.del(storagePath + filePath);
//    }
}
