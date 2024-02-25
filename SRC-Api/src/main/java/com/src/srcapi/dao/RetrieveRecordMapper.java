package com.src.srcapi.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.src.srcapi.model.po.RetrieveRecord;
import org.springframework.stereotype.Repository;

@Repository("retrieveRecordMapper")
public interface RetrieveRecordMapper extends BaseMapper<RetrieveRecord> {
}
