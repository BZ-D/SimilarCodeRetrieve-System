package com.src.srcapi.model.dto.retrieveRecord;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Date;

@NonNull
@Data
@NoArgsConstructor
public class RetrieveRecordDTO {
    private Long id;

    private String tag;

    private String recordContent;

    private String repoClassification;
}
