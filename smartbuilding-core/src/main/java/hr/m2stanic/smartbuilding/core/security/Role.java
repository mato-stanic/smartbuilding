package hr.m2stanic.smartbuilding.core.security;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_role")
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_seq")
    @SequenceGenerator(name = "role_seq", sequenceName = "role_seq", allocationSize = 1)
    @Setter(AccessLevel.PRIVATE)
    private Long id;


    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "displayName", unique = true)
    private String displayName;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    private RoleScope scope = RoleScope.ADMIN;

    @ElementCollection(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role_perms", joinColumns = @JoinColumn(name = "role_id"))
    @Column(name = "permission", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<Permission> permissions = new HashSet<>();


    public Role(RoleScope scope, String name, String displayName, String description, Set<Permission> permissions) {
        this.scope = scope;
        this.name = name;
        this.displayName = displayName;
        this.description = description;
        if (permissions != null) {
            this.permissions = permissions;
        }
    }

    public boolean hasPermission(Permission permission) {
        return permissions.contains(permission);
    }


}
