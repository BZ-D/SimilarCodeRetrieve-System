package com.src.srcapi.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.src.srcapi.model.dto.ResultDTO;
import com.src.srcapi.model.dto.retrieveRecord.RetrieveRecordDTO;
import com.src.srcapi.model.dto.retrieveRecord.RetrieveRecordStateDTO;
import com.src.srcapi.model.dto.similarCodeRecord.SimilarCodeRecordInfoDTO;
import com.src.srcapi.model.po.RetrieveRecord;
import com.src.srcapi.model.vo.RetrieveRecordStandardVO;
import com.src.srcapi.service.code.CodeService;
import com.src.srcapi.service.retrieveRecord.RetrieveRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/retrieveRecord")
public class RetrieveRecordController {
    @Autowired
    RetrieveRecordService retrieveRecordService;

    @GetMapping("/listRecordsByPage")
    ResultDTO<Page<RetrieveRecordDTO>> listRecordsByPage(@RequestParam Long pageIndex, @RequestParam Long pageSize){
        return retrieveRecordService.listRecordsByPage(pageIndex, pageSize);
    }


    @PostMapping("/retrieve")
    ResultDTO retrieve(@RequestBody RetrieveRecordStandardVO retrieveRecord){
        return retrieveRecordService.retrieve(retrieveRecord);
    }

    @GetMapping("/listRetrieveResultsByPage")
    ResultDTO<Page<RetrieveRecordStateDTO>> listRetrieveResultsByPage(@RequestParam Long pageIndex, @RequestParam Long pageSize){
        return retrieveRecordService.listRetrieveResultsByPage(pageIndex, pageSize);
    }

    @GetMapping("/listSimilarCodesByPage")
    ResultDTO<Page<SimilarCodeRecordInfoDTO>> listSimilarCodesByPage(@RequestParam Long retrieveId, @RequestParam Long pageIndex, @RequestParam Long pageSize){
        return retrieveRecordService.listSimilarCodesByPage(retrieveId, pageIndex, pageSize);
    }
}
