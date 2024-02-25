package com.src.srcapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.src.srcapi.constants.CommonConstant;
import com.src.srcapi.constants.RetrieveStateConstants;
import com.src.srcapi.dao.CodeRepoMapper;
import com.src.srcapi.dao.RetrieveRecordMapper;
import com.src.srcapi.dao.SimilarCodeRecordMapper;
import com.src.srcapi.model.dto.ResultDTO;
import com.src.srcapi.model.dto.retrieveRecord.ESCodeDocInfoDTO;
import com.src.srcapi.model.dto.retrieveRecord.RetrieveRecordDTO;
import com.src.srcapi.model.dto.retrieveRecord.RetrieveRecordStateDTO;
import com.src.srcapi.model.dto.retrieveRecord.es.CommitFileMeta;
import com.src.srcapi.model.dto.similarCodeRecord.SimilarCodeRecordInfoDTO;
import com.src.srcapi.model.po.CodeRepoInfo;
import com.src.srcapi.model.po.RetrieveRecord;
import com.src.srcapi.model.po.SimilarCodeRecord;
import com.src.srcapi.model.vo.RetrieveRecordStandardVO;
import com.src.srcapi.service.impl.retrieveManager.RetrieveManager;
import com.src.srcapi.service.retrieveRecord.RetrieveRecordService;
import com.src.srcapi.util.es.ESClient;
import com.src.srcapi.util.obs.CustomOBSClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RetrieveRecordServiceImpl implements RetrieveRecordService {
    @Autowired
    RetrieveManager retrieveManager;

    @Autowired
    RetrieveRecordMapper retrieveRecordMapper;

    @Autowired
    SimilarCodeRecordMapper similarCodeRecordMapper;

    @Autowired
    CodeRepoMapper codeRepoMapper;

    @Autowired
    CustomOBSClient customOBSClient;

    @Autowired
    ESClient esClient;

    @Override
    public ResultDTO<Page<RetrieveRecordDTO>> listRecordsByPage(Long pageIndex, Long pageSize) {
        Page<RetrieveRecord> page = new Page<>(pageIndex, pageSize);

        Page<RetrieveRecord> retrieveRecordPage = retrieveRecordMapper.selectPage(page, new QueryWrapper<>());
        Page<RetrieveRecordDTO> retrieveRecordDTOPage = new Page<>();
        retrieveRecordDTOPage.setSize(retrieveRecordPage.getSize());
        retrieveRecordDTOPage.setTotal(retrieveRecordPage.getTotal());
        List<RetrieveRecordDTO> retrieveRecordDTOS = retrieveRecordPage.getRecords().stream().map(item -> {
            RetrieveRecordDTO retrieveRecordDTO = new RetrieveRecordDTO();
            BeanUtils.copyProperties(item, retrieveRecordDTO);
            return retrieveRecordDTO;
        }).collect(Collectors.toList());

        retrieveRecordDTOPage.setRecords(retrieveRecordDTOS);
        return ResultDTO.buildSuccess(retrieveRecordDTOPage);
    }

    @Override
    public ResultDTO retrieve(RetrieveRecordStandardVO retrieveRecordStandardVO) {
        RetrieveRecord retrieveRecord = new RetrieveRecord();
        BeanUtils.copyProperties(retrieveRecordStandardVO, retrieveRecord);
        retrieveRecord.setRetrieveState(RetrieveStateConstants.RETRIEVING);
        retrieveRecordMapper.insert(retrieveRecord);
        if(retrieveRecordStandardVO.getRecordContent().endsWith(".java")){
            retrieveRecordStandardVO.setRecordContent(customOBSClient.fetchObject(
                    retrieveRecordStandardVO.getRecordContent().substring(
                            retrieveRecordStandardVO.getRecordContent().lastIndexOf('/') + 1
                    )
            ).orElse("代码文件不存在！"));
        }
        try {
            retrieveRecordStandardVO.setEsCodeDocInfoDTOS(getDestASTList(retrieveRecordStandardVO.getRecordContent(), retrieveRecordStandardVO.getRepoClassification()));
        }catch (Exception e){
            log.error("Code syntax error, retrieveRecordStandard tag: {}, retrieveRecordStandard content: {}",
                    retrieveRecordStandardVO.getTag(), retrieveRecordStandardVO.getRecordContent());
            retrieveRecord.setRetrieveState(RetrieveStateConstants.RETRIEVE_GRAMMAR_ERROR);
            retrieveRecordMapper.updateById(retrieveRecord);
        }
        retrieveManager.retrieve(retrieveRecordStandardVO, retrieveRecord);
        return ResultDTO.buildSuccess(true);
    }

    @Override
    public ResultDTO<Page<RetrieveRecordStateDTO>> listRetrieveResultsByPage(Long pageIndex, Long pageSize) {
        Page<RetrieveRecord> page = new Page<>(pageIndex, pageSize);

        Page<RetrieveRecord> retrieveRecordPage = retrieveRecordMapper.selectPage(page, new QueryWrapper<>());
        Page<RetrieveRecordStateDTO> retrieveRecordStateDTOPage = new Page<>();
        retrieveRecordStateDTOPage.setSize(retrieveRecordPage.getSize());
        retrieveRecordStateDTOPage.setTotal(retrieveRecordPage.getTotal());
        List<RetrieveRecordStateDTO> retrieveRecordStateDTOS = retrieveRecordPage.getRecords().stream().map(item -> {
            RetrieveRecordStateDTO retrieveRecordStateDTO = new RetrieveRecordStateDTO();
            BeanUtils.copyProperties(item, retrieveRecordStateDTO);
            return retrieveRecordStateDTO;
        }).collect(Collectors.toList());

        retrieveRecordStateDTOPage.setRecords(retrieveRecordStateDTOS);
        return ResultDTO.buildSuccess(retrieveRecordStateDTOPage);
    }

    @Override
    public ResultDTO<Page<SimilarCodeRecordInfoDTO>> listSimilarCodesByPage(Long retrieveId, Long pageIndex, Long pageSize) {
        Page<SimilarCodeRecord> page = new Page<>(pageIndex, pageSize);
        LambdaQueryWrapper<SimilarCodeRecord> similarCodeRecordLambdaQueryWrapper = new LambdaQueryWrapper<>();
        similarCodeRecordLambdaQueryWrapper
                .eq(SimilarCodeRecord::getRetrieveId, retrieveId)
                .orderBy(true, false, SimilarCodeRecord::getSimilarity);

        Page<SimilarCodeRecord> similarCodeRecordPage = similarCodeRecordMapper.selectPage(page, similarCodeRecordLambdaQueryWrapper);
        Page<SimilarCodeRecordInfoDTO> similarCodeRecordInfoDTOPage = new Page<>();
        similarCodeRecordInfoDTOPage.setSize(similarCodeRecordPage.getSize());
        similarCodeRecordInfoDTOPage.setTotal(similarCodeRecordPage.getTotal());
        List<SimilarCodeRecordInfoDTO> similarCodeRecordInfoDTOS = similarCodeRecordPage.getRecords().stream().map(item -> {
            SimilarCodeRecordInfoDTO similarCodeRecordInfoDTO = new SimilarCodeRecordInfoDTO();
            CodeRepoInfo codeRepoInfo = codeRepoMapper.selectById(item.getRepoId());
            BeanUtils.copyProperties(item, similarCodeRecordInfoDTO);
            similarCodeRecordInfoDTO.setRepoName(codeRepoInfo.getRepoName());
            similarCodeRecordInfoDTO.setRepoUrl(CommonConstant.GITHUB_PREFIX + codeRepoInfo.getUsername() + "/" + codeRepoInfo.getRepoName());
            return similarCodeRecordInfoDTO;
        }).collect(Collectors.toList());

        similarCodeRecordInfoDTOPage.setRecords(similarCodeRecordInfoDTOS);
        return ResultDTO.buildSuccess(similarCodeRecordInfoDTOPage);
    }

    private List<ESCodeDocInfoDTO> getDestASTList(String content, String tag){
        CompilationUnit cu = StaticJavaParser.parse(content);
        CommitFileMeta fileMeta = new CommitFileMeta();
        fileMeta.setClassList(cu.findAll(ClassOrInterfaceDeclaration.class).stream().map(n -> n.getName().getIdentifier()).collect(Collectors.toList()));
        fileMeta.setDependencyList(cu.findAll(ImportDeclaration.class).stream().map(n -> n.getName().getIdentifier()).collect(Collectors.toList()));
        fileMeta.setFieldList(cu.findAll(FieldDeclaration.class).stream().flatMap(n ->
                n.getVariables().stream().map(
                        m -> m.getTypeAsString() + ":" + m.getNameAsString()
                )
        ).collect(Collectors.toList()));
        fileMeta.setMethodList(cu.findAll(MethodDeclaration.class).stream().map(
                        n -> {
                            StringBuffer sb = new StringBuffer();
                            sb.append(n.getTypeAsString());
                            sb.append("/");
                            sb.append(n.getNameAsString());
                            sb.append("/");
                            n.getParameters().forEach(p -> {
                                sb.append(p.getTypeAsString() + ":" + p.getNameAsString());
                                sb.append(",");
                            });
                            sb.deleteCharAt(sb.length() - 1);
                            return sb.toString();
                        })
                .collect(Collectors.toList())
        );
        fileMeta.setTag(tag);
        return esClient.retrieveCode(fileMeta);
    }
}
