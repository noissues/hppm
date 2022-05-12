package com.hppm.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NotationDTO {

    private Long id;
    
    private String notationName; // 曲谱名
    
    private String author; // 曲谱作者
    
    private String songName; // 歌曲名

    private String artist; // 艺术家

    private String notationCategoryCode;  // 吉他、尤克里里

    private String playCategoryCode; // 指弹、弹唱

    private List<String> notationUrls; // 曲谱链接

}
