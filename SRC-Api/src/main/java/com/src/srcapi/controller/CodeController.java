package com.src.srcapi.controller;

import com.src.srcapi.model.dto.ResultDTO;
import com.src.srcapi.service.code.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/code")
public class CodeController {
    @Autowired
    CodeService codeService;

    @PostMapping("/uploadFile")
    public ResultDTO<String> uploadFile(@RequestParam("codeFile") MultipartFile file){
        return codeService.uploadFile(file);
    }
}
