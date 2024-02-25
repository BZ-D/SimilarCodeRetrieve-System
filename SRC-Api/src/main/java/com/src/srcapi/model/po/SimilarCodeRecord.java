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
public class SimilarCodeRecord {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long retrieveId;

    private Long repoId;

    private String codeTag;

    private Float similarity;

    private Date createTime;

    private Date updateTime;
}
