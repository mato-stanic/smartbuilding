package hr.m2stanic.smartbuilding.core.messages;


import hr.m2stanic.smartbuilding.core.company.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MessageManager {

    Message save(Message message);

    Message getMessage(Long messageId);

    void delete(Long messageId);

    Page<Message> getReceivedMessages(Company recipient, Pageable pageable);

    Page<Message> getSentMessages(Company sender, Pageable pageable);

    Long getUnreadMessageCount(Long companyId);


}
