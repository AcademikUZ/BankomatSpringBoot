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
public class Kupyura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long mingSomCount;

    private Long beshmingSomCount;

    private Long onmingSomCount;

    private Long ellikmingSomCount;

    private Long yuzmingSomCount;

    private Long ikkimingSomCount;

    public Kupyura(Long mingSomCount, Long beshmingSomCount, Long onmingSomCount, Long ellikmingSomCount, Long yuzmingSomCount, Long ikkimingSomCount) {
        this.mingSomCount = mingSomCount;
        this.beshmingSomCount = beshmingSomCount;
        this.onmingSomCount = onmingSomCount;
        this.ellikmingSomCount = ellikmingSomCount;
        this.yuzmingSomCount = yuzmingSomCount;
        this.ikkimingSomCount = ikkimingSomCount;
    }
}
