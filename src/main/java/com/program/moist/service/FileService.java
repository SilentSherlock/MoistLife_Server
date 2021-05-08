package com.program.moist.service;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.auth.sts.AssumeRoleRequest;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.program.moist.entity.StsToken;
import com.program.moist.utils.ConstUtil;
import com.program.moist.utils.FTPUtil;
import com.program.moist.utils.MyPropertiesUtil;
import com.program.moist.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * Date: 2021/4/3
 * Author: SilentSherlock
 * Description: describe the class features
 */
@Service
@Slf4j
public class FileService {

    /**
     * 云对象存储
     * 获取StsToken
     * @return
     */
    public StsToken getStsToken() {
        try {
            // 添加endpoint（直接使用STS endpoint，前两个参数留空，无需添加region ID）
            DefaultProfile.addEndpoint("", "", "Sts", MyPropertiesUtil.getProperty("OSS.endpoint"));
            // 构造default profile（参数留空，无需添加region ID）
            IClientProfile profile = DefaultProfile.getProfile("", MyPropertiesUtil.getProperty("OSS.accessKeyId"), MyPropertiesUtil.getProperty("OSS.accessKeySecret"));
            // 用profile构造client
            DefaultAcsClient client = new DefaultAcsClient(profile);
            final AssumeRoleRequest request = new AssumeRoleRequest();
            request.setMethod(MethodType.POST);
            request.setRoleArn(MyPropertiesUtil.getProperty("OSS.roleArn"));
            request.setRoleSessionName(MyPropertiesUtil.getProperty("OSS.roleSessionName"));
//            request.setPolicy(policy); // 若policy为空，则用户将获得该角色下所有权限
            request.setDurationSeconds(1000L); // 设置凭证有效时间
            final AssumeRoleResponse response = client.getAcsResponse(request);
            log.info("Expiration: " + response.getCredentials().getExpiration());
            log.info("Access Key Id: " + response.getCredentials().getAccessKeyId());
            log.info("Access Key Secret: " + response.getCredentials().getAccessKeySecret());
            log.info("Security Token: " + response.getCredentials().getSecurityToken());
            log.info("RequestId: " + response.getRequestId());

            String key = response.getCredentials().getAccessKeyId();
            String secret = response.getCredentials().getAccessKeySecret();
            String token = response.getCredentials().getSecurityToken();

            return new StsToken(key, secret, token);
        } catch (ClientException e) {
            System.out.println("Failed：");
            System.out.println("Error code: " + e.getErrCode());
            System.out.println("Error message: " + e.getErrMsg());
            System.out.println("RequestId: " + e.getRequestId());

            return null;
        }
    }

    /**
     * 单文件上传
     * @param file
     * @param path 路径需要提前根据用户id，上传图片归属进行拼接
     * @return 文件在ftp服务器的绝对路径，eg.ftp://ip/data/xx/xx.jpg
     */
    public String upload(MultipartFile file, String path, String newFileName) {
        File uploadFilePath;
        try {
            String uploadPath = createTargetDir(path);
            String fileName = file.getOriginalFilename();
            if (fileName == null || uploadPath == null) {
                log.error("not get upload file or cant create target dir");
                return null;
            }
            String exName = fileName.substring(fileName.lastIndexOf("."));
            uploadFilePath = new File(uploadPath + newFileName + exName);
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
