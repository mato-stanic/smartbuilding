package hr.m2stanic.smartbuilding.core.messages;

import hr.m2stanic.smartbuilding.core.appuser.AppUser;
import hr.m2stanic.smartbuilding.core.company.Company;
import lombok.*;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDateTime;

import javax.persistence.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "message_seq")
    @SequenceGenerator(name = "message_seq", sequenceName = "message_seq", allocationSize = 1)
    @Setter(AccessLevel.PRIVATE)
    private Long id;

    @Column(name = "sending_time")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    private LocalDateTime sendingTime = LocalDateTime.now();

    @Column(name = "title", length = 512)
    private String title;

    @Column(name = "body", length = 4096)
    private String body;

    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private Company recipient;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private AppUser sender;

    @Column(name = "read")
    private boolean read;


}
