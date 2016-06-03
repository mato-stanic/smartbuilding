package hr.m2stanic.smartbuilding.core.apartment;


import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Inheritance
@Table(name = "apartment_cron")
public class ApartmentCronJob {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "apartment_cron_seq")
    @SequenceGenerator(name = "apartment_cron_seq", sequenceName = "apartment_cron_seq", allocationSize = 1)
    @Setter(AccessLevel.PRIVATE)
    private Long id;

    @OneToOne
    private Apartment apartment;


    @Column(name = "time")
    private String time;

    @Column(name = "action")
    private String action;

    @Column(name = "room")
    private String room;

    @ElementCollection(fetch=FetchType.EAGER)
    @CollectionTable(name="apartment_cron_days", joinColumns=@JoinColumn(name="apartment_cron_id"))
    @Column(name="day")
    List<String> days = new ArrayList<>();


}
