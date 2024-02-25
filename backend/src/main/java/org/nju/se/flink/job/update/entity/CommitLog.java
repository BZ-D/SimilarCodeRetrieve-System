package org.nju.se.flink.job.update.entity;

import lombok.Data;

import java.util.List;

@Data
public class CommitLog {
    private GithubRepository repository;
    private boolean isLatest;
    private String commitId;
    private String message;
    private Integer AffectFileCount;
    private Integer addLineCount;
    private Integer deleteLineCount;
    private List<CommitFile> files;
    public String getUsername() { return repository.getUsername(); }
    public Long getRepositoryId() { return repository.getId(); }
    public String getRepositoryName() { return repository.getRepositoryName(); }
}
