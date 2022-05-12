package com.hppm.controller;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hppm.util.FileUtils;
import com.hppm.service.impl.FileStorageServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Controller
public class FileController {

    // 临时文件存储路径
    @Value("${hppm.file.tempFile}")
    private String tempFilePath;
    
    // 曲谱文件存储路径
    @Value("${hppm.file.notationFile}")
    private String notationFilePath;
    
    @Value("${hppm.baseURL}")
    private String baseUrl;

    @Autowired
    FileStorageServiceImpl storageService;

    @PostMapping("/file/upload")
    public ResponseEntity<Map<String, Object>> uploadFile(@RequestParam("files") MultipartFile[] files) {
        String message = "";
        String uploadingFilename = ""; // 正在上传的文件名
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 初始化文件目录
            storageService.cd(tempFilePath);

            List<String> fileUrls = new ArrayList<>();
            List<String> filenames = new ArrayList<>();

            for (MultipartFile file : files) {
                uploadingFilename = file.getOriginalFilename();
                
                // 文件重命名 1.防止重复上传，文件重名覆盖 2. 防止文件名过长
                String newFileName = FileUtils.renameFileByUUID(file.getOriginalFilename());

                storageService.save(file, newFileName);

                String fileUrl = baseUrl + "/file/" + newFileName; // 同时返回用于请求图片的 url
                fileUrls.add(fileUrl);
                filenames.add(file.getOriginalFilename());
            }
            message = "Uploaded files successfully: " + Arrays.deepToString(filenames.toArray());
            response.put("message", message);
            response.put("fileUrls", fileUrls);
            
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Upload file error", e);
            message = "Could not upload file " + uploadingFilename + "[" + e.getMessage() + "]";
            response.put("message", message);
            return new ResponseEntity<>(response, HttpStatus.EXPECTATION_FAILED);
        }
    }

    /**
     * 获取临时文件
     * @param filename 临时文件名
     * @return 文件内容
     */
    @GetMapping("/file/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        // 初始化文件目录
        storageService.cd(tempFilePath);
        Resource file = storageService.load(filename);
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }


    /**
     * 获取曲谱文件
     * @param filename 曲谱文件名
     * @return 曲谱文件内容
     */
    @GetMapping("/file/notation/{filename:.+}")
    public ResponseEntity<Resource> getNotationFile(@PathVariable String filename) {
        // 初始化文件目录
        storageService.cd(notationFilePath);
        Resource file = storageService.load(filename);
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    // @GetMapping("/files")
    // public ResponseEntity<List<FileInfoDTO>> getListFiles() {
    //     List<FileInfoDTO> FileInfoDTOs = storageService.loadAll().map(path -> {
    //         String filename = path.getFileName().toString();
    //         String url = MvcUriComponentsBuilder
    //                 .fromMethodName(FileController.class, "getFile", path.getFileName().toString()).build().toString();
    //
    //         return new FileInfoDTO(filename, url);
    //     }).collect(Collectors.toList());
    //
    //     return ResponseEntity.status(HttpStatus.OK).body(FileInfoDTOs);
    // }
   
}
