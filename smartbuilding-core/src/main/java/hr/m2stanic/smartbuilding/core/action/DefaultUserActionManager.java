package hr.m2stanic.smartbuilding.core.action;


import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


@Service
@Slf4j
public class DefaultUserActionManager implements UserActionManager {

    private BlockingQueue<UserAction> queue = new LinkedBlockingQueue<>();

    @Autowired
    private UserActionRepository repository;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;


    @Override
    public void save(UserAction userAction) {
        queue.add(userAction);
    }

    @Override
    public Page<UserAction> getAllUserActions(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<UserAction> getCompanyUserActions(Long companyId, Pageable pageable) {
        return repository.findByUserCompanyId(companyId, pageable);
    }



}
