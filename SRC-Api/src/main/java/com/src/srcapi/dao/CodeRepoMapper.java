package com.src.srcapi.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.src.srcapi.model.po.CodeRepoInfo;
import com.src.srcapi.model.po.RetrieveRecord;
import org.springframework.stereotype.Repository;

@Repository("codeRepoMapper")
public interface CodeRepoMapper extends BaseMapper<CodeRepoInfo> {
}
