package com.src.srcapi.model.dto.codeRepo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Date;

@NonNull
@Data
@NoArgsConstructor
public class CodeRepoInfoDTO {
    private Long id;

    private String repoName;

    private String repoUrl;

    private String repoClassification;

    private Date createTime;

    private Date updateTime;
}
