package fan.company.bankomatspringboot.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String city;

    @NotNull
    @Column(nullable = false)
    private String street;

    public Address(String city, String street) {
        this.city = city;
        this.street = street;
    }
}
