package com.hppm.model.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("notation")
public class NotationDO {
    
    // 主键自动递增
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String notationName;
    
    private String author;
    
    private String songName;
    
    private String artist;
    
    private String notationCategoryCode;  // 吉他、尤克里里

    private String playCategoryCode; // 指弹、弹唱
    
    private Date createDate;
    
    private Date updateDate;
}
