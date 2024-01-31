package com.hobbyzhub.javabackend.helprequestmodule.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Help {
    @Id
    private String helpId;
    @NotNull
    private String userId;
    @NotNull
    @NotEmpty
    private String email;
    @Lob
    @Column(name = "profile_image", columnDefinition = "BLOB")
    private String profilePic;
    @NotNull
    @NotEmpty
    @Column(columnDefinition = "TEXT")
    private String message;
    @NotNull
    @NotEmpty
    private String fullName;
}
