package com.hobbyzhub.javabackend.helprequestmodule.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@Entity
@Getter
@Builder
public final class Help implements Serializable {
    /*
    * final because it shouldn't be extended whatsoever
    * */
    private static final long serialVersionUID = 1L;
    @Id
    private final String helpId;
    @NotNull
    private final String userId;
    @NotNull
    @NotEmpty
    private final String email;
    @Lob
    @Column(name = "profile_image", columnDefinition = "BLOB")
    private final String profilePic;
    @NotNull
    @NotEmpty
    @Column(columnDefinition = "TEXT")
    private final String message;
    @NotNull
    @NotEmpty
    private final String fullName;
    /*
    * initialize mutable fields only once
    *  in the constructor
    * the fields are thread-safe as well
    * */

    public Help(String helpId, String userId, String email, String profilePic, String message, String fullName) {
        this.helpId = helpId;
        this.userId = userId;
        this.email = email;
        this.profilePic = profilePic;
        this.message = message;
        this.fullName = fullName;
    }
}
