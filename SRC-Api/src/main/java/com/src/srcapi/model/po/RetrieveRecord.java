package com.src.srcapi.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Date;

@NonNull
@Data
@NoArgsConstructor
public class RetrieveRecord {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String tag;

    private String recordContent;

    private String repoClassification;

    private String retrieveState;

    private Date createTime;

    private Date updateTime;
}
