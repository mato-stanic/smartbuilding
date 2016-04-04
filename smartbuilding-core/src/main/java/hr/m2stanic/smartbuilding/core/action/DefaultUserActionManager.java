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

    private Saver saver;


    @Override
    public void save(UserAction userAction) {
        queue.add(userAction);
    }

    class Saver implements Runnable {
        private boolean active = true;

        @Override
        public void run() {
            while (active) {
                try {
                    UserAction userAction = queue.take();
                    if (active) repository.save(userAction);
                } catch (Exception e) {
                    log.error("Failed to save user action!", e);
                }
            }
        }

        public void stop() {
            this.active = false;
            try {
                //add some dummy action to make queue.take() method return
                queue.put(new AppUserAddedAction());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @PostConstruct
    public void init() {
        saver = new Saver();
        taskExecutor.execute(saver);
    }

    @PreDestroy
    public void destroy() {
        if (saver != null) {
            saver.stop();
        }
    }
    
}
