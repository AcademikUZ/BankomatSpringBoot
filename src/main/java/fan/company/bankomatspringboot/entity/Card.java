package fan.company.bankomatspringboot.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min=16, max=16)
    @Column(unique = true, nullable = false)
    private Long maxsusRaqam;

    @NotNull
    @Size(min=3, max=3)
    @Column(nullable = false)
    private Long CVV;

    @NotNull
    @Size(min=4, max=4)
    @Column(nullable = false)
    private Long pincode;

    @NotNull
    private String fullNameOwner;

    @NotNull
    @Column(nullable = false)
    private LocalDate expireDate;

    private Double qoldiqMablag;

    @ManyToOne
    private Bank bank;

    @ManyToOne
    private PlasticType plasticType;

    private boolean active = true;

    @OneToOne(cascade = CascadeType.ALL)
    private Users mijoz;

    private Integer sanoq = 0;

}
