package com.src.srcapi.model.dto.commitRecord;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NonNull
@Data
@NoArgsConstructor
public class CommitStatisticDTO {
    private String date;

    private Integer commitsCount;

    private Integer affectFilesCount;

    private Integer addLinesCount;

    private Integer minusLinesCount;
}
