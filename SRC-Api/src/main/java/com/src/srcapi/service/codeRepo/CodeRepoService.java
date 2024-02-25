package com.src.srcapi.service.codeRepo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.src.srcapi.model.dto.ResultDTO;
import com.src.srcapi.model.dto.codeRepo.CodeRepoInfoDTO;
import com.src.srcapi.model.dto.commitRecord.CommitStatisticDTO;
import com.src.srcapi.model.po.CodeRepoInfo;

import java.util.List;

public interface CodeRepoService {
    ResultDTO<Page<CodeRepoInfoDTO>> listReposByPage(Long pageIndex, Long pageSize, String repoClassification);

    ResultDTO<List<CommitStatisticDTO>> getCodeRepoStatistics(Long repoId);

    ResultDTO<String> getCodeContent(Long repoId, String codeTag);
}
