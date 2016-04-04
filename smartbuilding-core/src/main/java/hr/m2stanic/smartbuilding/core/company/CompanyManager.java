package hr.m2stanic.smartbuilding.core.company;

import java.util.List;

public interface CompanyManager {


    Company save(Company agency);

    Company getCompany(Long id);

    Company getCompany(String name);

    Admin getAgency(Long id);

    List<Company> getAllCompanies();

    List<Admin> getAllAgencies();

    List<UserGroup> getAllOperatorGroups();

    void delete(Long operatorId);

}
