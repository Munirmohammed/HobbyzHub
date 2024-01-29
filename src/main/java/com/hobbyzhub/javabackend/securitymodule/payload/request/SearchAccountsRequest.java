package com.hobbyzhub.javabackend.securitymodule.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchAccountsRequest {
    private String searchSlug;
    private Integer page;
    private Integer size;
}
