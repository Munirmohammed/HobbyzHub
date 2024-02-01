package com.hobbyzhub.javabackend.postmodule.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class PostGenericResponse<T>{
    private String apiVersion;
    private String organizationName;
    private String message;
    private T data;
}
