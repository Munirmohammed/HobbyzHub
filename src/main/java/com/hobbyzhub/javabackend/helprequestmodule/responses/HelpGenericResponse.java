package com.hobbyzhub.javabackend.helprequestmodule.responses;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class HelpGenericResponse<T>{
    private String apiVersion;
    private String organizationName;
    private String message;
    private T data;
}
