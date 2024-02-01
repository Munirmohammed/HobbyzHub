package com.hobbyzhub.hobbyservice.payload.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private Integer statusCode;
    private String timeInformation;
    private String message;
    private String description;
}
