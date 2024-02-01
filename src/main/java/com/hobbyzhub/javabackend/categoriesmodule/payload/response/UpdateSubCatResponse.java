package com.hobbyzhub.javabackend.categoriesmodule.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UpdateSubCatResponse {
	private String subCategoryId;
	private String subCategoryName;
	private String subCategoryImageLink;
}
