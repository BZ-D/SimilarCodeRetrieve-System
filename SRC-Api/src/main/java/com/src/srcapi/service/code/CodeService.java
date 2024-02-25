package com.src.srcapi.service.code;

import com.src.srcapi.model.dto.ResultDTO;
import org.springframework.web.multipart.MultipartFile;

public interface CodeService {
    ResultDTO<String> uploadFile(MultipartFile file);
}
