package hr.m2stanic.smartbuilding.core.messages;

import hr.m2stanic.smartbuilding.core.apartment.Apartment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class DefaultMessageManager implements MessageManager {

    @Autowired
    private MessageRepository repository;

    @Override
    public Message save(Message message) {
        return repository.save(message);
    }


    @Override
    public Message getMessage(Long messageId) {
        return repository.findOne(messageId);
    }

    @Override
    public void delete(Long messageId) {
        repository.delete(messageId);
    }

    @Override
    public Page<Message> getReceivedMessages(Apartment recipient, Pageable pageable) {
        return repository.findByRecipientOrderByIdDesc(recipient, pageable);
    }

    @Override
    public Page<Message> getSentMessages(Apartment sender, Pageable pageable) {
        return repository.findBySenderApartmentIdOrderByIdDesc(sender.getId(), pageable);
    }

    @Override
    public Long getUnreadMessageCount(Long apartmentId) {
        return repository.countByRecipientIdAndRead(apartmentId, false);
    }
}
