package hr.m2stanic.smartbuilding.core.company;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface CompanyRepository extends JpaRepository<Company, Long> {

    @Query("select o from UserGroup o")
    Page<UserGroup> findAllOperatorGroups(Pageable pageable);

    @Query("select a from Admin a")
    Page<Admin> findAllAgencies(Pageable pageable);

    Company findByName(String name);



}
