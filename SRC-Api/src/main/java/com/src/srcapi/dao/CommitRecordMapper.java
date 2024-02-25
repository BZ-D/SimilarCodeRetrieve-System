package com.src.srcapi.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.src.srcapi.model.dto.commitRecord.CommitStatisticDTO;
import com.src.srcapi.model.po.CodeRepoInfo;
import com.src.srcapi.model.po.CommitRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("commitRecordMapper")
public interface CommitRecordMapper extends BaseMapper<CommitRecord> {
    List<CommitStatisticDTO> getCodeRepoStatistics(@Param("repoId") Long repoId);
}
