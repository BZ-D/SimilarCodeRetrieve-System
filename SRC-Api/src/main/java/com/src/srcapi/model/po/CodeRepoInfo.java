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
public class CodeRepoInfo {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String repoName;

    private String username;

    private String latestCommitId;

    private String repoClassification;

    private Date createTime;

    private Date updateTime;
}
