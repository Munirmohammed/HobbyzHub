package com.hobbyzhub.javabackend.sharedpayload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GenericResponse<T> {
    private String apiVersion;
    private String organizationName;
    private String message;
    private Boolean success;
    private Integer status;
    private T data;
}
