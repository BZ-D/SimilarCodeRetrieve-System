package com.src.srcapi.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.src.srcapi.model.po.AstRecord;
import org.springframework.stereotype.Repository;

@Repository("astRecordMapper")
public interface AstRecordMapper extends BaseMapper<AstRecord> {
}
