package hr.m2stanic.smartbuilding.core.company;

import java.util.List;

public interface CompanyManager {


    Apartment save(Apartment agency);

    Apartment getApartment(Long id);

    Apartment getApartment(String name);

    List<Admin> getAllAgencies();

    List<UserGroup> getAllOperatorGroups();

    void delete(Long operatorId);

}
