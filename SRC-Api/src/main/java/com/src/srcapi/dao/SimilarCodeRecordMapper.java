package com.src.srcapi.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.src.srcapi.model.po.CodeRepoInfo;
import com.src.srcapi.model.po.SimilarCodeRecord;
import org.springframework.stereotype.Repository;

@Repository("similarCodeRecordMapper")
public interface SimilarCodeRecordMapper extends BaseMapper<SimilarCodeRecord> {
}
