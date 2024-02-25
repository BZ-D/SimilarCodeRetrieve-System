DROP DATABASE IF EXISTS `similar_code_retrieval`;
CREATE DATABASE `similar_code_retrieval` DEFAULT CHARACTER SET utf8;
USE `similar_code_retrieval`;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `retrieve_record`;
CREATE TABLE `retrieve_record`
(
    `id`                  bigint(20)                                                    NOT NULL AUTO_INCREMENT,
    `tag`                 varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `record_content`      longtext                                                      NOT NULL,
    `repo_classification` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL,
    `retrieve_state`      varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL,
    `create_time`         datetime                                                      not null default CURRENT_TIMESTAMP comment '创建时间',
    `update_time`         datetime                                                      not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `similar_code_record`;
CREATE TABLE `similar_code_record`
(
    `id`          bigint(20)                                                    NOT NULL AUTO_INCREMENT,
    `retrieve_id` bigint(20)                                                    NOT NULL,
    `repo_id`     bigint(20)                                                    NOT NULL,
    `code_tag`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `similarity`  float                                                         NOT NULL,
    `create_time` datetime                                                      not null default CURRENT_TIMESTAMP comment '创建时间',
    `update_time` datetime                                                      not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `code_repo_info`;
CREATE TABLE `code_repo_info`
(
    `id`                  bigint(20)                                                    NOT NULL AUTO_INCREMENT,
    `repo_name`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `username`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `latest_commit_id`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `repo_classification` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `create_time`         datetime                                                      not null default CURRENT_TIMESTAMP comment '创建时间',
    `update_time`         datetime                                                      not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `commit_record`;
CREATE TABLE `commit_record`
(
    `id`                 bigint(20)                                                    NOT NULL AUTO_INCREMENT,
    `repo_id`            bigint(20)                                                    NOT NULL,
    `commit_msg`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `affect_files_count` int(11)                                                       NOT NULL,
    `add_lines_count`    int(11)                                                       NOT NULL,
    `minus_lines_count`  int(11)                                                       NOT NULL,
    `create_time`        datetime                                                      not null default CURRENT_TIMESTAMP comment '创建时间',
    `update_time`        datetime                                                      not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `ast_record`;
CREATE TABLE `ast_record`
(
    `id`          bigint(20)                                                    NOT NULL AUTO_INCREMENT,
    `repo_id`     bigint(20)                                                    NOT NULL,
    `code_tag`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
    `create_time` datetime                                                      not null default CURRENT_TIMESTAMP comment '创建时间',
    `update_time` datetime                                                      not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;