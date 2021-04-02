package com.program.moist.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Date: 2021/4/2
 * Author: SilentSherlock
 * Description: observe ftp function
 */
@Slf4j
public class FTPUtil {

    public static boolean upload(String path, List<File> files) throws IOException {
        FTPUtil ftpUtil = new FTPUtil(ConstUtil.FTP_IP, ConstUtil.FTP_PORT, ConstUtil.user_name, ConstUtil.password);
        log.info("start upload file");
        boolean result = ftpUtil.uploadFile(path, files);
        log.info("upload file " + result);
        return result;
    }
    private String ip;
    private int port;
    private String user;
    private String password;
    private FTPClient ftpClient;

    private FTPUtil(String ip, int port, String user, String password) {
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.password = password;
        ftpClient = new FTPClient();
    }

    private boolean uploadFile(String path, List<File> files) throws IOException {
        boolean status = true;
        FileInputStream fileInputStream = null;
        if (connect()) {
            try {
                ftpClient.changeWorkingDirectory(path);
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();

                for (File file :
                        files) {
                    fileInputStream = new FileInputStream(file);
                    ftpClient.storeFile(file.getName(), fileInputStream);
                }
            } catch (IOException e) {
                log.error("FTP服务器上传失败");
                status = false;
            } finally {
                if (fileInputStream != null) fileInputStream.close();
                ftpClient.disconnect();
            }
        }

        return status;
    }

    private boolean connect() {
        boolean isConnect = false;
        try {
            ftpClient.connect(ip);
            isConnect = ftpClient.login(user, password);
        } catch (IOException e) {
            log.error("FTP服务器登录失败");
        }
        return isConnect;
    }
}
