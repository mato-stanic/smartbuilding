package hr.m2stanic.smartbuilding.core.action;

import lombok.*;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDateTime;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Inheritance
@DiscriminatorColumn(name = "action_type", length = 63)
@Table(name = "action_history")
public abstract class UserAction {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 1)
    @Setter(AccessLevel.PRIVATE)
    private Long id;

    @Column(name = "time")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    private LocalDateTime time = LocalDateTime.now();

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_company_id")
    private Long userCompanyId;

    @Column(name = "user_company_name")
    private String userCompanyName;

    @Column(name = "ref_type")
    @Enumerated(EnumType.STRING)
    private ReferencedEntityType referenceType;

    @Column(name = "ref_id")
    private Long referenceId;

    @Column(name = "ref_name")
    private String referenceName;



    public abstract String getDescription();


}
