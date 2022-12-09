package com.axonactive.agileterm.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name ="user_role_assignment")
public class UserRoleAssignmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated (EnumType.STRING)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

}
