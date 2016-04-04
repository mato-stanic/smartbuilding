package hr.m2stanic.smartbuilding.core.messages;

import hr.m2stanic.smartbuilding.core.company.Apartment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MessageRepository extends JpaRepository<Message, Long> {

    Page<Message> findByRecipientOrderByIdDesc(Apartment recipient, Pageable pageable);

    Page<Message> findBySenderApartmentIdOrderByIdDesc(Long senderApartmentId, Pageable pageable);

    Long countByRecipientIdAndRead(Long apartmentId, Boolean read);


}
