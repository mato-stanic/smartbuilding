package hr.m2stanic.smartbuilding.core.apartment;


import com.google.common.base.Objects;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Inheritance
@Table(name = "apartment_layout")
public class ApartmentLayout {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "apartment_layout_seq")
    @SequenceGenerator(name = "apartment_layout_seq", sequenceName = "apartment_layout_seq", allocationSize = 1)
    @Setter(AccessLevel.PRIVATE)
    private Long id;

    @OneToOne
    private Apartment apartment;

    @Column(name = "living_room")
    private boolean livingRoom;

    @Column(name = "hallway")
    private boolean hallway;

    @Column(name = "kitchen")
    private boolean kitchen;

    @Column(name = "bathroom")
    private boolean bathroom;

    @Column(name = "bedroom")
    private boolean bedroom;
}
