package com.hobbyzhub.javabackend.categoriesmodule.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.hobbyzhub.javabackend.categoriesmodule.type.GeneralCategoryDescription;
import jakarta.persistence.*;
import lombok.*;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "hobby_category", uniqueConstraints = {
        @UniqueConstraint(columnNames = "category_name")
})
@EqualsAndHashCode(callSuper = true)
public class HobbyCategory extends GeneralCategoryDescription implements Serializable {
    @Serial
    @Transient
    private static final long serialVersionUID = UUID.randomUUID().getLeastSignificantBits();

    @Column(name = "icon_link", columnDefinition = "BLOB")
    private String iconLink;

    @OneToMany(
		cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
        },
		mappedBy = "hobbyCategory",
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    private List<HobbySubCategory> subCategory = new ArrayList<>();
}
