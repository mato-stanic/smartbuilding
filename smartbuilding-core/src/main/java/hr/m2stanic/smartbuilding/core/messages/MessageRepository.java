package hr.m2stanic.smartbuilding.core.messages;

import hr.m2stanic.smartbuilding.core.company.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MessageRepository extends JpaRepository<Message, Long> {

    Page<Message> findByRecipientOrderByIdDesc(Company recipient, Pageable pageable);

    Page<Message> findBySenderCompanyIdOrderByIdDesc(Long senderCompanyId, Pageable pageable);

    Long countByRecipientIdAndRead(Long companyId, Boolean read);


}
