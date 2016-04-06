package hr.m2stanic.smartbuilding.core.apartment;

import java.util.List;

public interface ApartmentManager {


    Apartment save(Apartment agency);

    Apartment getApartment(Long id);

    Apartment getApartment(String name);

    List<Admin> getAllApartments();

    List<UserGroup> getAllUserGroups();

    void delete(Long operatorId);

}
