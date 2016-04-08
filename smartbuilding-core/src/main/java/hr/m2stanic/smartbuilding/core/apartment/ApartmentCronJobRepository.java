package hr.m2stanic.smartbuilding.core.apartment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ApartmentCronJobRepository extends JpaRepository<ApartmentCronJob, Long> {

    List<ApartmentCronJob> findByApartment(Apartment apartment);

    ApartmentCronJob save(ApartmentCronJob apartmentCronJob);

    List<ApartmentCronJob> findByApartmentAndRoom(Apartment apartment, String room);
}
