package com.hppm.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hppm.mapper.NotationFileMapper;
import com.hppm.model.entity.NotationFileDO;
import com.hppm.util.FileUtils;
import com.hppm.mapper.NotationMapper;
import com.hppm.model.dto.NotationDTO;
import com.hppm.model.dto.PageDTO;
import com.hppm.model.entity.NotationDO;
import com.hppm.service.StorageService;
import com.hppm.service.NotationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class NotationServiceImpl implements NotationService {

    @Value("${hppm.baseURL}")
    private String baseURL;

    // 临时文件存储路径
    @Value("${hppm.file.tempFile}")
    private String tempFilePath;

    // 曲谱文件存储路径
    @Value("${hppm.file.notationFile}")
    private String notationFilePath;

    
    @Autowired
    StorageService fileStorageService;
    
    @Autowired
    NotationMapper notationMapper;
    
    @Autowired
    NotationFileMapper notationFileMapper;
    
    @Override
    public void addNotation(NotationDTO notationDTO) throws IOException {
        log.debug("addNotation begin, notationDTO {}", notationDTO);
        
        NotationDO notationDO = new NotationDO();
        BeanUtils.copyProperties(notationDTO, notationDO);
        Date now = new Date();
        notationDO.setCreateDate(now);
        notationDO.setUpdateDate(now);
        notationMapper.insert(notationDO);

        List<String> notationUrls = notationDTO.getNotationUrls();

        // 将上传文件从临时目录移动到曲谱保存目录
        List<String> notationFileList = this.moveTempFilesToNationPath(notationUrls);
        
        for (int i = 0; i < notationFileList.size(); i++) {
            NotationFileDO notationFileDO = new NotationFileDO();
            notationFileDO.setNotationId(notationDO.getId());
            notationFileDO.setSeq(i);
            notationFileDO.setPath(notationFileList.get(i));
            notationFileMapper.insert(notationFileDO);
        }
        
        log.debug("addNotation end.");
    }
    
    private List<String> moveTempFilesToNationPath(List<String> notationUrls) throws IOException {
        List<String> notationFileList = new ArrayList<>();

        if (CollectionUtils.isEmpty(notationUrls)) {
            log.debug("notationUrls is empty.");
            return notationFileList; 
        }
        
        for (String path : notationUrls) {
            // 从文件 URL 解析文件名
            String notationFileName = FileUtils.extractFilename(path);

            // 源文件路径，曲谱临时保存路径
            Path srcPath = Paths.get(tempFilePath).resolve(notationFileName);

            // 目标文件路径，曲谱真实保存路径按歌手分类保存
            Path destPath = Paths.get(notationFilePath);
            // destPath.resolve(notation.getArtist() != null ? notation.getArtist() : DEFAULT_ARTIST);
            // String songName = notationDTO.getSongName() != null ? notationDTO.getSongName() : DEFAULT_SONG_NAME;
            // notationFileName = songName + '-' + notationFileName;

            // 将临时文件挪动到曲谱文件夹中
            log.debug("moving file {} to {}", srcPath.toAbsolutePath(), destPath.toAbsolutePath());
            Files.move(srcPath, destPath.resolve(notationFileName));

            // D:\tmp\hppm\tmp\5e526ab0-9246-4a99-a8bd-3f3a03da5c45_1.jpg
            // D:\tmp\hppm\tmp\5e526ab0-9246-4a99-a8bd-3f3a03da5c45_1.jpg
            String newPath = destPath.resolve(notationFileName).toAbsolutePath().toString();

            notationFileList.add(newPath);
            
        }
        
        return notationFileList;
    }

    @Override
    public void deleteNotation(Long notationId) throws IOException {
        log.debug("deleteNotation begin, notationId [{}]", notationId);

        List<NotationFileDO> notationFileList = 
            notationFileMapper.selectList(new QueryWrapper<NotationFileDO>().eq("notation_id", notationId));
        
        // 删除文件
        for (NotationFileDO notationFile : notationFileList) {
            String pathStr = notationFile.getPath();
            log.debug("Deleting file: " + pathStr);
            Path path = Paths.get(pathStr);
            if (Files.exists(path)) {
                Files.delete(path);
            }
        }

        notationFileMapper.deleteBatchIds(notationFileList.stream().map(NotationFileDO::getId).collect(Collectors.toList()));

        notationMapper.deleteById(notationId);
    }

    @Override
    public void updateNotation(NotationDTO notationDTO) throws IOException {
        log.debug("updateNotation begin, notationDTO [{}]", notationDTO);

        Long notationId = notationDTO.getId();
        Assert.notNull(notationId, "Notation id must not be null!");
        
        // 首先处理 Files
        if (!CollectionUtils.isEmpty(notationDTO.getNotationUrls())) {
            // 这里简单地更新所有文件，因此前台要修改曲谱文件，必须全部删除，再重新上传
            notationFileMapper.delete(new QueryWrapper<NotationFileDO>().eq("notation_id", notationId));

            List<String> notationFiles = moveTempFilesToNationPath(notationDTO.getNotationUrls());

            for (int i = 0; i < notationFiles.size(); i++) {
                NotationFileDO notationFileDO = new NotationFileDO();
                notationFileDO.setNotationId(notationId);
                notationFileDO.setSeq(i);
                notationFileDO.setPath(notationFiles.get(i));
                notationFileMapper.insert(notationFileDO);
            }
        }

        NotationDO notationDO = new NotationDO();
        BeanUtils.copyProperties(notationDTO, notationDO);
        notationDO.setUpdateDate(new Date());
        notationMapper.update(notationDO, new UpdateWrapper<NotationDO>().eq("notation_id", notationId));
    }


    public PageDTO<NotationDTO> queryNotationListPage(String notationCategory, String searchParam, int page, int size) {
        PageHelper.startPage(page, size);

        QueryWrapper<NotationDO> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq(!StringUtils.isEmpty(notationCategory), "notation_category_code", notationCategory);
        
        // searchParam 模糊查询
        queryWrapper.and(!StringUtils.isEmpty(searchParam), 
            wrapper -> wrapper.like("notation_name", searchParam)
            .or().like("song_name", searchParam)
            .or().like("artist", searchParam));

        List<NotationDO> notationList = notationMapper.selectList(queryWrapper);

        PageInfo<NotationDO> pageInfo = new PageInfo<>(notationList);
        
        List<NotationDTO> notations = pageInfo.getList().stream().map(notationDO -> {
            NotationDTO notationDTO = new NotationDTO();
            BeanUtils.copyProperties(notationDO, notationDTO);
            return notationDTO;
        }).collect(Collectors.toList());

        PageDTO<NotationDTO> pageDTO = new PageDTO<>();
        pageDTO.setRows(notations); 
        pageDTO.setPage(pageInfo.getPageNum()); 
        pageDTO.setTotalPages(pageInfo.getPages());
        pageDTO.setTotal(pageInfo.getTotal());
        return pageDTO;
    }


    public NotationDTO getNotationDetail(long id) {
        NotationDTO notationDTO = new NotationDTO();
        
        // 获取曲谱信息
        NotationDO notationDO = notationMapper.selectById(id);
        BeanUtils.copyProperties(notationDO, notationDTO);
        
        // 获取曲谱文件，返回请求 URL
        List<NotationFileDO> notationFileList = notationFileMapper.selectList(new QueryWrapper<NotationFileDO>().eq("notation_id", id));
        List<String> urls = notationFileList.stream().map(notationFileDO -> {
            String filename = FileUtils.extractFilename(notationFileDO.getPath());
            // http://ip:port/file/notation/filename
            return baseURL + "/file/notation/" + filename;
        }).collect(Collectors.toList());
        notationDTO.setNotationUrls(urls);
        
        return notationDTO;
    }
}
