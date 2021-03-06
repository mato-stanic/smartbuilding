package hr.m2stanic.smartbuilding.core.apartment;

import java.util.List;

public interface ApartmentManager {


    Apartment save(Apartment agency);

    Apartment getApartment(Long id);

    Apartment getApartment(String name);

    List<Admin> getAllApartments();

    List<Tenants> getAllTenantApartments();

    ApartmentLayout getApartmentRoomStates(Apartment apartment);

    ApartmentLayout save(ApartmentLayout apartmentLayout);

    ApartmentCronJob save(ApartmentCronJob apartmentCronJob);

    List<ApartmentCronJob> getApartmentCronJobsForRoom(Apartment apartment, String room);

    List<ApartmentCronJob> getApartmentCronJobs(Apartment apartment);

    ApartmentCronJob getAparatmentCronJob(Long apartmentCronJobId);

    List<ApartmentCronJob> getAllCronJobs();

    void deleteCronJob(Long apartmentCronJobId);

    void delete(Long operatorId);

}
