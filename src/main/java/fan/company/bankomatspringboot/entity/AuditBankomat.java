package fan.company.bankomatspringboot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class AuditBankomat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private Timestamp createAt;

    @ManyToOne
    private Bankomat bankomat;

    @ManyToOne
    private Users users;

    @ManyToOne
    private Card card;

    private Double pulMiqdori;

    @Enumerated(EnumType.STRING)
    private Amal amal;


}
