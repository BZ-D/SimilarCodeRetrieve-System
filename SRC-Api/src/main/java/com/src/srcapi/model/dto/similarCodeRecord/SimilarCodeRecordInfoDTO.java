package com.src.srcapi.model.dto.similarCodeRecord;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NonNull
@Data
@NoArgsConstructor
public class SimilarCodeRecordInfoDTO {
    private Long repoId;

    private String repoName;

    private String repoUrl;

    private String codeTag;

    private Float similarity;
}
