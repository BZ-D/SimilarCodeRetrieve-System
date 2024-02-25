package org.nju.se.flink.job.update.entity;

import lombok.Data;

@Data
public class GithubRepository {
    private Long id;
    private String username;
    private String repositoryName;
    private String tag;
    private String lastCommitId;
}
