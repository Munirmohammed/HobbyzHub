package com.hobbyzhub.javabackend.categoriesmodule.type;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GeneralCategoryDescription {
    @Id
    @Column(name = "category_id")
    private String categoryId;

    @Column(name = "category_name", nullable = false)
    @Size(min = 1, max = 100)
    private String categoryName;

    @Column(name = "last_edited", nullable = false)
    private LocalDate lastEdited;
}
