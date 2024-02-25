package com.src.srcapi.model.dto.retrieveRecord;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NonNull
@Data
@NoArgsConstructor
public class ESCodeDocInfoDTO {
    private Long repoId;

    private String codeTag;
}
