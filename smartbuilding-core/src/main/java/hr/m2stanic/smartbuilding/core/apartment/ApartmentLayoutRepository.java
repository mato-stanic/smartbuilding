package hr.m2stanic.smartbuilding.core.apartment;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ApartmentLayoutRepository extends JpaRepository<ApartmentLayout, Long> {

    ApartmentLayout findByApartment(Apartment apartment);

    ApartmentLayout save(ApartmentLayout apartmentLayout);
}
