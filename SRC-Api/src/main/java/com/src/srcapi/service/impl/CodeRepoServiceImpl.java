package com.src.srcapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.src.srcapi.constants.CommonConstant;
import com.src.srcapi.constants.RepoClassificationConstants;
import com.src.srcapi.dao.CodeRepoMapper;
import com.src.srcapi.dao.CommitRecordMapper;
import com.src.srcapi.model.dto.ResultDTO;
import com.src.srcapi.model.dto.codeRepo.CodeRepoInfoDTO;
import com.src.srcapi.model.dto.commitRecord.CommitStatisticDTO;
import com.src.srcapi.model.dto.retrieveRecord.RetrieveRecordStateDTO;
import com.src.srcapi.model.po.CodeRepoInfo;
import com.src.srcapi.service.codeRepo.CodeRepoService;
import com.src.srcapi.util.ceph.CephClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CodeRepoServiceImpl implements CodeRepoService {
    @Autowired
    CodeRepoMapper codeRepoMapper;

    @Autowired
    CommitRecordMapper commitRecordMapper;

    @Autowired
    CephClient cephClient;

    @Override
    public ResultDTO<Page<CodeRepoInfoDTO>> listReposByPage(Long pageIndex, Long pageSize, String repoClassification) {
        Page<CodeRepoInfo> page = new Page<>(pageIndex,pageSize);
        LambdaQueryWrapper<CodeRepoInfo> codeRepoInfoQueryWrapperByClassification = new LambdaQueryWrapper<>();
        if(!repoClassification.equals(RepoClassificationConstants.FULL))
            codeRepoInfoQueryWrapperByClassification.eq(CodeRepoInfo::getRepoClassification, repoClassification);

        Page<CodeRepoInfo> codeRepoInfoPage = codeRepoMapper.selectPage(page, codeRepoInfoQueryWrapperByClassification);
        Page<CodeRepoInfoDTO> codeRepoInfoDTOPage = new Page<>();
        codeRepoInfoDTOPage.setSize(codeRepoInfoPage.getSize());
        codeRepoInfoDTOPage.setTotal(codeRepoInfoPage.getTotal());
        List<CodeRepoInfoDTO> codeRepoInfoDTOS = codeRepoInfoPage.getRecords().stream().map(item -> {
            CodeRepoInfoDTO codeRepoInfoDTO = new CodeRepoInfoDTO();
            BeanUtils.copyProperties(item, codeRepoInfoDTO);
            codeRepoInfoDTO.setRepoUrl(CommonConstant.GITHUB_PREFIX + item.getUsername() + "/" + item.getRepoName());
            return codeRepoInfoDTO;
        }).collect(Collectors.toList());
        codeRepoInfoDTOPage.setRecords(codeRepoInfoDTOS);
        return ResultDTO.buildSuccess(codeRepoInfoDTOPage);
    }

    @Override
    public ResultDTO<List<CommitStatisticDTO>> getCodeRepoStatistics(Long repoId) {
        return ResultDTO.buildSuccess(commitRecordMapper.getCodeRepoStatistics(repoId));
    }

    @Override
    public ResultDTO<String> getCodeContent(Long repoId, String codeTag) {
        CodeRepoInfo codeRepoInfo = codeRepoMapper.selectById(repoId);
        ResultDTO<String> resultDTO = ResultDTO.buildSuccess();
        try {
            resultDTO.setContent(cephClient.getFileFromS3(codeRepoInfo.getRepoClassification(), codeTag).orElse("文件丢失！"));
        } catch (Exception e){
            log.error("Get Ceph code file error, uri: {}", codeTag);
        }
        return resultDTO;
    }
}
