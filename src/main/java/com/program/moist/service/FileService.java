package com.program.moist.service;

import com.program.moist.utils.ConstUtil;
import com.program.moist.utils.FTPUtil;
import com.program.moist.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Date: 2021/4/3
 * Author: SilentSherlock
 * Description: describe the class features
 */
@Service
@Slf4j
public class FileService {

    /**
     * 单文件上传
     * @param file
     * @param path 路径需要提前根据用户id，上传图片归属进行拼接
     * @return 文件在ftp服务器的绝对路径，eg.ftp://ip/data/xx/xx.jpg
     */
    public String upload(MultipartFile file, String path) {
        File uploadFilePath;
        try {
            String uploadPath = createTargetDir(path);
            String fileName = file.getOriginalFilename();
            if (fileName == null || uploadPath == null) {
                log.error("not get upload file or cant create target dir");
                return null;
            }
            String exName = fileName.substring(fileName.lastIndexOf("."));
            uploadFilePath = new File(uploadPath + TokenUtil.getUUID() + exName);
            file.transferTo(uploadFilePath);//上传到项目中暂存
            FTPUtil.upload(path, uploadFilePath);//上传到ftp服务器
            uploadFilePath.delete();
        } catch (IOException e) {
            log.error("FileService upload file failed", e);
            return null;
        }
        return ConstUtil.FTP_DIR + path + uploadFilePath.getName();
    }

    /**
     * 多文件上传
     * @param files
     * @param path
     * @return
     */
    public List<String> upload(MultipartFile[] files, String path) {
        String targetPath = createTargetDir(path);
        if (targetPath == null) {
            log.error("cant create target dir");
            return null;
        }

        List<File> fileList = new LinkedList<>();
        List<String> paths = new LinkedList<>();
        try {
            for (MultipartFile f :
                    files) {
                String fileName = f.getOriginalFilename();
                if (fileName == null) {
                    log.error("not get upload file");
                    return null;
                }
                String targetName = targetPath + TokenUtil.getUUID() + fileName.substring(fileName.lastIndexOf("."));
                File targetFile = new File(targetName);
                f.transferTo(targetFile);
                fileList.add(targetFile);
                paths.add(ConstUtil.FTP_DIR + path + targetFile.getName());
            }
            FTPUtil.upload(path, fileList);
            fileList.forEach(File::delete);
        } catch (IOException e) {
            log.error("upload file transfer to local failed");
        }
        return paths;
    }
    /**
     * 创建暂存文件夹
     * @param path
     * @return 暂存路径
     */
    private String createTargetDir(String path) {

        String targetPath = null;
        try {
            targetPath = ResourceUtils.getURL("classpath:").getPath() + "static/" + path;
            File dir = new File(targetPath);
            if (!dir.exists()) {
                dir.setWritable(true);
                dir.mkdirs();
            }
        } catch (IOException e) {
            log.error("create target dir failed", e);
        }
        return targetPath;
    }
}
