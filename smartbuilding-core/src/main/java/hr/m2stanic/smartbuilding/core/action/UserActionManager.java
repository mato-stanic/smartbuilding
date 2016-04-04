package hr.m2stanic.smartbuilding.core.action;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface UserActionManager {

    void save(UserAction userAction);

    Page<UserAction> getAllUserActions(Pageable pageable);

    Page<UserAction> getCompanyUserActions(Long companyId, Pageable pageable);



}
