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
public class CommitRecord {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long repoId;

    private String commitMsg;

    private Integer affectFilesCount;

    private Integer addLinesCount;

    private Integer minusLinesCount;

    private Date createTime;

    private Date updateTime;
}
