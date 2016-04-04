package hr.m2stanic.smartbuilding.core.action;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserActionRepository extends JpaRepository<UserAction, Long>, JpaSpecificationExecutor {

    Page<UserAction> findByUserCompanyId(Long id, Pageable pageable);

    List<UserAction> findByIdIn(List<Long> ids);


}
