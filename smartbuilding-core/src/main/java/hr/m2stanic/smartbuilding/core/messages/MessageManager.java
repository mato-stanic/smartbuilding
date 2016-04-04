package hr.m2stanic.smartbuilding.core.messages;


import hr.m2stanic.smartbuilding.core.company.Apartment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MessageManager {

    Message save(Message message);

    Message getMessage(Long messageId);

    void delete(Long messageId);

    Page<Message> getReceivedMessages(Apartment recipient, Pageable pageable);

    Page<Message> getSentMessages(Apartment sender, Pageable pageable);

    Long getUnreadMessageCount(Long apartmentId);


}
