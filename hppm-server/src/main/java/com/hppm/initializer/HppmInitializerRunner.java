package com.hppm.initializer;

import com.hppm.util.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class HppmInitializerRunner implements ApplicationRunner {
    @Value("${hppm.appHome}")
    private String hppmHome;
    
    // 临时文件存储路径
    @Value("${hppm.file.tempFile}")
    private String tempFilePath;

    // 曲谱文件存储路径
    @Value("${hppm.file.notationFile}")
    private String notationFilePath;
    
    @Override
    public void run(ApplicationArguments args) throws Exception {
        this.initializeDirectories();
    }

    private void initializeDirectories() {
        FileUtils.createDirectoriesIfNotExist(hppmHome);
        FileUtils.createDirectoriesIfNotExist(tempFilePath);
        FileUtils.createDirectoriesIfNotExist(notationFilePath);
    }
}
