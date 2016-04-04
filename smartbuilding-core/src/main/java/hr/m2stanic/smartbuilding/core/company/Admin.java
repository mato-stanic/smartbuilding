package hr.m2stanic.smartbuilding.core.company;

import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@NoArgsConstructor
@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends Apartment {

    public Admin(Long id, String name) {
        super(id, name);
    }
}
