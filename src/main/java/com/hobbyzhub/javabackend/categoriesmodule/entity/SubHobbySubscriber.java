package com.hobbyzhub.javabackend.categoriesmodule.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import jakarta.persistence.*;

@Data
@Table(name = "sub_hobby_subscriber")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class SubHobbySubscriber implements Serializable {
    @Serial
    @Transient
    private static final long serialVersionUID = UUID.randomUUID().getLeastSignificantBits();

    @Id
    @Column(name = "subscription_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subscriptionId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "profile_pic_link", columnDefinition = "BLOB")
    private String profilePicLink;

    @Column(name = "sub_category_id")
    private String subCategoryId;
}
