package com.src.srcapi.model.dto.retrieveRecord;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Date;

@NonNull
@Data
@NoArgsConstructor
public class RetrieveRecordStateDTO {
    private Long id;

    private String tag;

    private String recordContent;

    private String repoClassification;

    private String retrieveState;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
