package com.hobbyzhub.javabackend.securitymodule.entity;

import com.hobbyzhub.javabackend.securitymodule.types.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@Table(name = "app_user_details", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
@AllArgsConstructor
@NoArgsConstructor
public class AppUser implements Serializable {
    @Serial
    @Transient
    private static final long serialVersionUID = UUID.randomUUID().getLeastSignificantBits();

    @Id
    @Basic
    @Column(name = "user_id")
    private String userId;

    @Basic
    @Column(name = "full_name")
    @Size(min = 1, max = 100)
    private String fullName;

    @Basic
    @Email
    @Column(name = "email")
    private String email;

    @Basic
    @Column(name = "bio")
    private String bio;

    @Column(name = "birthdate")
    private LocalDate birthdate;

    @Column(name = "joined_date")
    private LocalDate joinedDate;

    @Lob
    @Column(name = "profile_image", columnDefinition = "BLOB")
    private String profileImage;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "temporary_otp")
    private Integer temporaryOtp;

    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "new_account")
    private boolean newAccount;

    @Column(name = "category_status")
    private boolean categoryStatus = false;

    @Column(name = "account_active")
    private boolean accountActive;
}
