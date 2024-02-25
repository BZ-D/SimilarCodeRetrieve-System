package com.src.srcapi.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.src.srcapi.model.dto.ResultDTO;
import com.src.srcapi.model.dto.codeRepo.CodeRepoInfoDTO;
import com.src.srcapi.model.dto.commitRecord.CommitStatisticDTO;
import com.src.srcapi.service.codeRepo.CodeRepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/codeRepo")
public class CodeRepoController {
    @Autowired
    CodeRepoService codeRepoService;

    @GetMapping("/listReposByPage")
    ResultDTO<Page<CodeRepoInfoDTO>> listReposByPage(@RequestParam Long pageIndex, @RequestParam Long pageSize, @RequestParam String repoClassification){
        return codeRepoService.listReposByPage(pageIndex, pageSize, repoClassification);
    }

    @GetMapping("/getCodeRepoStatistics")
    ResultDTO<List<CommitStatisticDTO>> getCodeRepoStatistics(@RequestParam Long repoId){
        return codeRepoService.getCodeRepoStatistics(repoId);
    }

    @GetMapping("/getCodeContent")
    ResultDTO<String> getCodeContent(@RequestParam Long repoId, @RequestParam String codeTag){
        return codeRepoService.getCodeContent(repoId, codeTag);
    }
}
