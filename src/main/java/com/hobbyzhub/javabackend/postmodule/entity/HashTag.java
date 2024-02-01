package com.hobbyzhub.javabackend.postmodule.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HashTag implements Serializable {
    @Id
    private String hashTagId;
    private String tagName;
    @JsonManagedReference
    @ManyToMany(mappedBy = "hashTags",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private Set<Post> posts = new HashSet<>();
}
