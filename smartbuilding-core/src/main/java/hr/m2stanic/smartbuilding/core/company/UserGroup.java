package hr.m2stanic.smartbuilding.core.company;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@NoArgsConstructor
@Entity
@DiscriminatorValue("OPERATOR")
@Getter
@Setter
public class UserGroup extends Company {

    public UserGroup(Long id, String name) {
        super(id, name);
    }
}
