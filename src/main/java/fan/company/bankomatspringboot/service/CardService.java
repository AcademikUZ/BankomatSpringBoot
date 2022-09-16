package fan.company.bankomatspringboot.service;

import fan.company.bankomatspringboot.entity.Bank;
import fan.company.bankomatspringboot.entity.Card;
import fan.company.bankomatspringboot.entity.PlasticType;
import fan.company.bankomatspringboot.entity.Users;
import fan.company.bankomatspringboot.entity.enums.RoleName;
import fan.company.bankomatspringboot.payload.dto.ApiResult;
import fan.company.bankomatspringboot.payload.dto.CardDto;
import fan.company.bankomatspringboot.repository.BankRepository;
import fan.company.bankomatspringboot.repository.CardRepository;
import fan.company.bankomatspringboot.repository.PlasticTypeRepository;
import fan.company.bankomatspringboot.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CardService {

    @Autowired
    CardRepository repository;
    @Autowired
    PlasticTypeRepository plasticTypeRepository;
    @Autowired
    BankRepository bankRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RoleRepository roleRepository;

    

    public Page<Card> getAllCard(Integer page) {
        Pageable pageable = PageRequest.of(page, 10);
        return repository.findAll(pageable);
    }

    public ApiResult getOne(Long id) {
        Optional<Card> optionalCard = repository.findById(id);
        if (optionalCard.isEmpty())
            return new ApiResult("Bunday Card mavjud emas!", false);
        return new ApiResult("OK!", true, optionalCard.get());
    }

    public ApiResult add(CardDto dto) {

        try {

            Optional<Card> optionalCard = repository.findByMaxsusRaqam(dto.getMaxsusRaqam());
            if (optionalCard.isPresent())
                return new ApiResult("Bunday Card mavjud!", false);

            Optional<Bank> optionalBank = bankRepository.findById(dto.getBankId());
            if(optionalBank.isEmpty())
                return new ApiResult("Bunday Bank mavjud emas!", false);

            Optional<PlasticType> optionalPlasticType = plasticTypeRepository.findById(dto.getPlasticTypeId());
            if(optionalPlasticType.isEmpty())
                return new ApiResult("Bunday Plastic turi mavjud emas!", false);



            Card card = new Card();

            card.setActive(dto.isActive());
            card.setPincode(dto.getPincode());
            card.setBank(optionalBank.get());
            card.setPlasticType(optionalPlasticType.get());
            card.setCVV(dto.getCVV());
            card.setMaxsusRaqam(dto.getMaxsusRaqam());
            card.setFullNameOwner(dto.getFullNameOwner());
            card.setExpireDate(dto.getExpireDate());


            Users users = new Users();
            users.setFullName(dto.getFullName());
            users.setPassword(passwordEncoder.encode(String.valueOf(card.getPincode())));
            users.setEmail(String.valueOf(card.getMaxsusRaqam()));
            users.setRoles(roleRepository.findByRoleName(RoleName.ROLE_MIJOZ).get());

            card.setMijoz(users);

            repository.save(card);

            return new ApiResult("Saqlandi!", true);

        } catch (Exception e) {
            return new ApiResult(e.getMessage(), false);
        }

    }

    public ApiResult edit(Long id, CardDto dto) {

        try {

            Optional<Card> optionalCard = repository.findById(id);
            if (optionalCard.isEmpty())
                return new ApiResult("Bunday Card mavjud emas!", false);

            Optional<Bank> optionalBank = bankRepository.findById(dto.getBankId());
            if(optionalBank.isEmpty())
                return new ApiResult("Bunday Bank mavjud emas!", false);

            Optional<PlasticType> optionalPlasticType = plasticTypeRepository.findById(dto.getPlasticTypeId());
            if(optionalPlasticType.isEmpty())
                return new ApiResult("Bunday Plastic turi mavjud emas!", false);

            Card card = optionalCard.get();

            card.setActive(dto.isActive());
            card.setPincode(dto.getPincode());
            card.setBank(optionalBank.get());
            card.setPlasticType(optionalPlasticType.get());
            card.setCVV(dto.getCVV());
            card.setMaxsusRaqam(dto.getMaxsusRaqam());
            card.setFullNameOwner(dto.getFullNameOwner());
            card.setExpireDate(dto.getExpireDate());

            Users users = card.getMijoz();
            users.setFullName(dto.getFullName());
            users.setPassword(passwordEncoder.encode(String.valueOf(card.getPincode())));
            users.setEmail(String.valueOf(card.getMaxsusRaqam()));
            users.setRoles(roleRepository.findByRoleName(RoleName.ROLE_MIJOZ).get());

            card.setMijoz(users);

            repository.save(card);

            return new ApiResult("Taxrirlandi!", true);

        } catch (Exception e) {
            return new ApiResult(e.getMessage(), false);
        }
    }


    public ApiResult delete(Long id) {
        try {
            boolean exists = repository.existsById(id);
            if (exists) {
                repository.deleteById(id);
                return new ApiResult("O'chirildi", true);
            }
            return new ApiResult("Bunday Card mavjud emas", false);
        } catch (Exception e) {
            return new ApiResult("Xatolik", false);
        }
    }

}
