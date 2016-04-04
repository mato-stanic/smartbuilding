package hr.m2stanic.smartbuilding.core.messages;

import hr.m2stanic.smartbuilding.core.company.Company;
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
    public Page<Message> getReceivedMessages(Company recipient, Pageable pageable) {
        return repository.findByRecipientOrderByIdDesc(recipient, pageable);
    }

    @Override
    public Page<Message> getSentMessages(Company sender, Pageable pageable) {
        return repository.findBySenderCompanyIdOrderByIdDesc(sender.getId(), pageable);
    }

    @Override
    public Long getUnreadMessageCount(Long companyId) {
        return repository.countByRecipientIdAndRead(companyId, false);
    }
}
