package com.src.srcapi.model.dto;

import com.src.srcapi.constants.CommonConstant;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.validation.ObjectError;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public class ResultDTO<T> {
    private Integer code;

    private String message;

    private T content;

    public ResultDTO(Integer code, String msg){
        this.code = code;
        this.message = msg;
    }

    public ResultDTO(Integer code, String msg, @NonNull T data) {
        this.code = code;
        this.message = msg;
        this.content = data;
    }

    public  static <T> ResultDTO<T> buildSuccess(String msg, @NonNull T data) {
        return new ResultDTO(CommonConstant.SUCCESS_CODE,msg,data);
    }

    public  static <T> ResultDTO<T> buildSuccess(@NonNull T data) {
        return new ResultDTO(CommonConstant.SUCCESS_CODE,"",data);
    }
    public static <T> ResultDTO<T> buildSuccess(String msg) {
        return new ResultDTO(CommonConstant.SUCCESS_CODE,msg);
    }
    public static <T> ResultDTO<T> buildSuccess() {
        return new ResultDTO(CommonConstant.SUCCESS_CODE,"请求成功");
    }
    public static ResultDTO buildFailure(String msg) {
        return new ResultDTO(CommonConstant.ERROR_CODE,msg);
    }
    public static ResultDTO buildError(List<ObjectError> constraintViolations) {
        Set<String> setMessage = new HashSet<>();
        for (ObjectError message : constraintViolations) {
            setMessage.add(message.getDefaultMessage());
        }
        return buildFailure(setMessage.toString());
    }
    public boolean isFailure(){
        return code==CommonConstant.ERROR_CODE;
    }
    public boolean isSuccess(){
        return code==CommonConstant.SUCCESS_CODE;
    }
}
