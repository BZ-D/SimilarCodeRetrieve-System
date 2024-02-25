package com.src.srcapi.service.retrieveRecord;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.src.srcapi.model.dto.ResultDTO;
import com.src.srcapi.model.dto.retrieveRecord.RetrieveRecordDTO;
import com.src.srcapi.model.dto.retrieveRecord.RetrieveRecordStateDTO;
import com.src.srcapi.model.dto.similarCodeRecord.SimilarCodeRecordInfoDTO;
import com.src.srcapi.model.vo.RetrieveRecordStandardVO;

public interface RetrieveRecordService {
    ResultDTO<Page<RetrieveRecordDTO>> listRecordsByPage(Long pageIndex, Long pageSize);

    ResultDTO retrieve(RetrieveRecordStandardVO retrieveRecordStandardVO);

    ResultDTO<Page<RetrieveRecordStateDTO>> listRetrieveResultsByPage(Long pageIndex, Long pageSize);

    ResultDTO<Page<SimilarCodeRecordInfoDTO>> listSimilarCodesByPage(Long retrieveId, Long pageIndex, Long pageSize);
}
