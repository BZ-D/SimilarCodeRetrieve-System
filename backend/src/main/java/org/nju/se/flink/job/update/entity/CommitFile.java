package org.nju.se.flink.job.update.entity;

import lombok.Data;

@Data
public class CommitFile {
    private GithubRepository repository;
    private String status;
    private String path;
    private String content;
    public String getUsername() { return repository.getUsername(); }
    public Long getRepositoryId() { return repository.getId(); }
    public String getRepositoryName() { return repository.getRepositoryName(); }
    public String getTag() { return repository.getTag(); }
}
