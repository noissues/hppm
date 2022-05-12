package com.hppm.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("notation_file")
public class NotationFileDO {

    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long notationId;
    
    private Integer seq;
    
    private String path;
}
