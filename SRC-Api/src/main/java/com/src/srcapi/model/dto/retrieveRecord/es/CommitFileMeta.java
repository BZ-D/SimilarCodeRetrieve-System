package com.src.srcapi.model.dto.retrieveRecord.es;

import lombok.Data;

import java.util.List;

@Data
public class CommitFileMeta {
    private String tag;
    private Long repositoryId;

    private List<String> dependencyList;
    private List<String> classList;
    private List<String> fieldList;
    private List<String> methodList;
}
