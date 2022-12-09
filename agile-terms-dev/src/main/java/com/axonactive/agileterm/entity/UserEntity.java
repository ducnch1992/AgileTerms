package com.axonactive.agileterm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name ="users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true , nullable = false)
    @NotNull
    private String username;

    @Column(unique = true , nullable = false)
    @NotNull
    @Email
    private String email;

    @Column
    @JsonIgnore
    private String password;

    @Column
    private Boolean activated = false;

    @OneToMany(mappedBy = "userEntity",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<UserRoleAssignmentEntity> roles;

    @ManyToOne
    @JoinColumn(name = "verification_token_id")
    private VerificationTokenEntity verificationTokenEntity;

}
