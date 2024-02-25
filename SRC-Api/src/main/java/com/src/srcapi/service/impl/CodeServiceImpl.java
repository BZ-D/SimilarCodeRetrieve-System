package com.src.srcapi.service.impl;

import com.src.srcapi.model.dto.ResultDTO;
import com.src.srcapi.service.code.CodeService;
import com.src.srcapi.util.obs.CustomOBSClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class CodeServiceImpl implements CodeService {
    @Autowired
    CustomOBSClient customOBSClient;

    @Override
    public ResultDTO<String> uploadFile(MultipartFile file) {
        ResultDTO<String> resultDTO = ResultDTO.buildSuccess();
        resultDTO.setContent(customOBSClient.uploadObject(file).orElse("无法获取文件uri，请联系管理员！"));
        return resultDTO;
    }
}
