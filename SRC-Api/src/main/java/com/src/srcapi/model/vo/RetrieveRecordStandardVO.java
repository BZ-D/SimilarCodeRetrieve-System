package com.src.srcapi.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.src.srcapi.model.dto.retrieveRecord.ESCodeDocInfoDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Date;
import java.util.List;

@NonNull
@Data
@NoArgsConstructor
public class RetrieveRecordStandardVO {
    private String tag;

    private String recordContent;

    private String repoClassification;

    private String retrieveState;

    private List<ESCodeDocInfoDTO> esCodeDocInfoDTOS;
}
