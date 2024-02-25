package com.src.srcapi.service.impl.retrieveManager;

import com.github.javaparser.ParseProblemException;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.src.srcapi.constants.CommonConstant;
import com.src.srcapi.constants.RetrieveStateConstants;
import com.src.srcapi.dao.CodeRepoMapper;
import com.src.srcapi.dao.RetrieveRecordMapper;
import com.src.srcapi.dao.SimilarCodeRecordMapper;
import com.src.srcapi.model.dto.retrieveRecord.ESCodeDocInfoDTO;
import com.src.srcapi.model.po.CodeRepoInfo;
import com.src.srcapi.model.po.RetrieveRecord;
import com.src.srcapi.model.po.SimilarCodeRecord;
import com.src.srcapi.model.vo.RetrieveRecordStandardVO;
import com.src.srcapi.service.impl.retrieveManager.algorithm.LevenshteinDistanceSimilarityCalAlgorithm;
import com.src.srcapi.service.impl.retrieveManager.algorithm.SimilarityCalAlgorithm;
import com.src.srcapi.service.impl.retrieveManager.algorithm.SmithWatermanSimilarityCalAlgorithm;
import com.src.srcapi.service.impl.retrieveManager.serializer.SerializerVisitor;
import com.src.srcapi.util.ceph.CephClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RetrieveManager {
    @Value("${code-retrieve.algorithm}")
    private final String similarityAlgorithm = "levenshtein-distance";

    @Autowired
    SimilarCodeRecordMapper similarCodeRecordMapper;

    @Autowired
    CephClient cephClient;

    @Autowired
    CodeRepoMapper codeRepoMapper;

    @Autowired
    RetrieveRecordMapper retrieveRecordMapper;


    private SimilarityCalAlgorithm similarityCalAlgorithm;

    public RetrieveManager(){
        switch (similarityAlgorithm){
            case "levenshtein-distance":
                this.similarityCalAlgorithm = new LevenshteinDistanceSimilarityCalAlgorithm();
                break;
            case "smith-waterman":
                this.similarityCalAlgorithm = new SmithWatermanSimilarityCalAlgorithm();
            default:
                break;
        }
    }

    @Async
    public void retrieve(RetrieveRecordStandardVO retrieveRecordStandardVO, RetrieveRecord retrieveRecord){
        try {
            CompilationUnit cu = StaticJavaParser.parse(retrieveRecordStandardVO.getRecordContent());
            SerializerVisitor serializer = new SerializerVisitor();
            String srcAST = StringUtils.join(cu.accept(serializer, null).toArray(), "");
            for(ESCodeDocInfoDTO esCodeDocInfoDTO : retrieveRecordStandardVO.getEsCodeDocInfoDTOS()){
                CodeRepoInfo codeRepoInfo = codeRepoMapper.selectById(esCodeDocInfoDTO.getRepoId());
                String destAST = cephClient.getFileFromS3(codeRepoInfo.getRepoClassification(), esCodeDocInfoDTO.getCodeTag() + CommonConstant.AST_SUFFIX).orElse("语法树不存在！");
                Float similarity = similarityCalAlgorithm.calculateSimilarity(srcAST, destAST);
                SimilarCodeRecord similarCodeRecord = new SimilarCodeRecord();
                similarCodeRecord.setCodeTag(esCodeDocInfoDTO.getCodeTag() + CommonConstant.JAVA_SUFFIX);
                similarCodeRecord.setRetrieveId(retrieveRecord.getId());
                similarCodeRecord.setRepoId(codeRepoInfo.getId());
                similarCodeRecord.setSimilarity(similarity);
                similarCodeRecordMapper.insert(similarCodeRecord);
                log.info("src tag: {}, dest file: {}, similarity: {}", retrieveRecordStandardVO.getTag(), esCodeDocInfoDTO.getCodeTag(), similarity);
            }
            retrieveRecord.setRetrieveState(RetrieveStateConstants.RETRIEVE_SUCCESS);

        }catch (ParseProblemException parseProblemException){
            log.error("Code syntax error, retrieveRecordStandard tag: {}, retrieveRecordStandard content: {}",
                    retrieveRecordStandardVO.getTag(), retrieveRecordStandardVO.getRecordContent());
            retrieveRecord.setRetrieveState(RetrieveStateConstants.RETRIEVE_GRAMMAR_ERROR);
        }
        catch (Exception e){
            e.printStackTrace();
            log.error("Code similarity retrieve failed, retrieveRecordStandard tag: {}, retrieveRecordStandard content: {}",
                    retrieveRecordStandardVO.getTag(), retrieveRecordStandardVO.getRecordContent());
            retrieveRecord.setRetrieveState(RetrieveStateConstants.RETRIEVE_FAILED);
        } finally {
            retrieveRecordMapper.updateById(retrieveRecord);
        }
    }
}
