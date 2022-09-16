package fan.company.bankomatspringboot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Bankomat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    private List<PlasticType> plasticTypeList;

    private Double yechiladiganMaxPulMiqdori;

    @ManyToOne
    private Bank ownerBank;

    private Double commissionMiqdoriForOwner;

    private Double commissionMiqdoriForOther;

    private Double forSignalMaxMiqdorPul;

    private Double forSignalMinMiqdorPul;

    @OneToOne(cascade = CascadeType.ALL)
    private Address address;

    @OneToOne(cascade = CascadeType.ALL)
    private Kupyura kupyura;

    @ManyToOne
    private Users masulXodim;




}
