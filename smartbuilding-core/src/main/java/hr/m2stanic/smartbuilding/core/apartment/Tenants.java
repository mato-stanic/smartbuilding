package hr.m2stanic.smartbuilding.core.apartment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@NoArgsConstructor
@Entity
@DiscriminatorValue("TENANT")
@Getter
@Setter
public class Tenants extends Apartment {

    public Tenants(Long id, String name) {
        super(id, name);
    }
}
