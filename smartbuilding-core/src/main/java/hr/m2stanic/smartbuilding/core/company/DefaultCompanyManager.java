package hr.m2stanic.smartbuilding.core.company;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static hr.m2stanic.smartbuilding.core.CacheConstants.*;

@Service
public class DefaultCompanyManager implements CompanyManager {



    @Autowired
    private CompanyRepository repository;



    @Override
    @Caching(evict = { @CacheEvict({COMPANIES_ALL_CACHE, COMPANIES_AGENCIES_CACHE, COMPANIES_OPERATOR_GROUPS_CACHE})}, put = {@CachePut(value={COMPANIES_BY_ID_CACHE}, key="#company.id") })
    public Company save(Company company) {
        return repository.save(company);
    }

    @Override
    @Cacheable(COMPANIES_BY_ID_CACHE)
    public Company getCompany(Long id) {
        return repository.findOne(id);
    }

    @Override
    public Company getCompany(String name) {
        return repository.findByName(name);
    }

    @Override
    @Cacheable(COMPANIES_BY_ID_CACHE)
    public Admin getAgency(Long id) {
        Company company = repository.findOne(id);
        return company instanceof Admin ? (Admin) company : null;
    }


    @Override
    @Cacheable(COMPANIES_ALL_CACHE)
    public List<Company> getAllCompanies() {
        Pageable pageable = new PageRequest(0, 100, Sort.Direction.ASC, "name");
        List<Company> companies = repository.findAll(pageable).getContent();
        return companies != null ? companies : new ArrayList<>();
    }



    @Override
    @Cacheable(COMPANIES_AGENCIES_CACHE)
    public List<Admin> getAllAgencies() {
        Pageable pageable = new PageRequest(0, 100, Sort.Direction.ASC, "name");
        List<Admin> agencies = repository.findAllAgencies(pageable).getContent();
        return agencies != null ? agencies : new ArrayList<>();
    }

    @Override
    @Cacheable(COMPANIES_OPERATOR_GROUPS_CACHE)
    public List<UserGroup> getAllOperatorGroups() {
        Pageable pageable = new PageRequest(0, 100, Sort.Direction.ASC, "name");
        List<UserGroup> agencies = repository.findAllOperatorGroups(pageable).getContent();
        return agencies != null ? agencies : new ArrayList<>();
    }

    @Override
    @Caching(evict = { @CacheEvict({COMPANIES_ALL_CACHE, COMPANIES_AGENCIES_CACHE, COMPANIES_OPERATOR_GROUPS_CACHE}), @CacheEvict(value={COMPANIES_BY_ID_CACHE})})
    public void delete(Long operatorId) {
        repository.delete(operatorId);
    }
}
