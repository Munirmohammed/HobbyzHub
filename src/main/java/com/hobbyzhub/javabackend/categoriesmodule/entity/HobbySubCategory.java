package com.hobbyzhub.javabackend.categoriesmodule.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

import com.hobbyzhub.javabackend.categoriesmodule.type.GeneralCategoryDescription;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "hobby_sub_category", uniqueConstraints = {
	@UniqueConstraint(columnNames = "category_name")
})
public class HobbySubCategory extends GeneralCategoryDescription implements Serializable {
	@Serial
	@Transient
	private static final long serialVersionUID = UUID.randomUUID().getLeastSignificantBits();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_category_id", nullable = false)
	private HobbyCategory hobbyCategory;
}
