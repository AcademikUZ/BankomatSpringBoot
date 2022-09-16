package fan.company.bankomatspringboot.service;

import fan.company.bankomatspringboot.entity.*;
import fan.company.bankomatspringboot.payload.dto.ApiResult;
import fan.company.bankomatspringboot.payload.dto.BankomatDto;
import fan.company.bankomatspringboot.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BankomatService {

    @Autowired
    BankomatRepository repository;
    @Autowired
    BankRepository bankRepository;
    @Autowired
    KupyuraRepository kupyuraRepository;
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    UsersRepository usersRepository;
    

    public Page<Bankomat> getAllBankomat(Integer page) {
        Pageable pageable = PageRequest.of(page, 10);
        return repository.findAll(pageable);
    }

    public ApiResult getOne(Long id) {
        Optional<Bankomat> optionalBankomat = repository.findById(id);
        if (optionalBankomat.isEmpty())
            return new ApiResult("Bunday Bankomat mavjud emas!", false);
        return new ApiResult("OK!", true, optionalBankomat.get());
    }

    public ApiResult add(BankomatDto dto) {

        try {

            Optional<Bankomat> optionalBankomat = repository.findByAddress_CityAndAddress_StreetAndOwnerBank_Id(dto.getStreet(), dto.getCity(), dto.getOwnerBankId());
            if (optionalBankomat.isPresent())
                return new ApiResult("Bunday Bankomat mavjud!", false);

            Optional<Users> usersOptional = usersRepository.findById(dto.getMasulXodimId());
            if (usersOptional.isEmpty())
                return new ApiResult("Bunday Xodim mavjud emas!", false);

            Address address = new Address(dto.getStreet(), dto.getCity());
            Kupyura kupyura = new Kupyura(
                    0L, 0L, 0L,
                    0L, 0L, 0L
            );
            Optional<Bank> optionalBank = bankRepository.findById(dto.getOwnerBankId());

            Bankomat bankomat = new Bankomat();

            bankomat.setOwnerBank(optionalBank.get());
            bankomat.setAddress(address);
            bankomat.setKupyura(kupyura);

            bankomat.setCommissionMiqdoriForOther(dto.getCommissionMiqdoriForOther());
            bankomat.setCommissionMiqdoriForOwner(dto.getCommissionMiqdoriForOwner());
            bankomat.setForSignalMaxMiqdorPul(dto.getForSignalMaxMiqdorPul());
            bankomat.setPlasticTypeList(dto.getPlasticTypeList());
            bankomat.setYechiladiganMaxPulMiqdori(dto.getYechiladiganMaxPulMiqdori());
            bankomat.setMasulXodim(usersOptional.get());

            repository.save(bankomat);

            return new ApiResult("Saqlandi!", true);

        } catch (Exception e) {
            return new ApiResult(e.getMessage(), false);
        }

    }

    public ApiResult edit(Long id, BankomatDto dto) {

        try {

            Optional<Bankomat> optionalBankomat = repository.findById(id);
            if (optionalBankomat.isEmpty())
                return new ApiResult("Bunday Bankomat mavjud emas!", false);

            Optional<Users> usersOptional = usersRepository.findById(dto.getMasulXodimId());
            if (usersOptional.isEmpty())
                return new ApiResult("Bunday Xodim mavjud emas!", false);

            Optional<Bank> optionalBank = bankRepository.findById(dto.getOwnerBankId());
            Kupyura kupyura = new Kupyura(
                    dto.getMingSomCount(), dto.getBeshmingSomCount(), dto.getOnmingSomCount(),
                    dto.getEllikmingSomCount(), dto.getYuzmingSomCount(), dto.getIkkimingSomCount()
            );

            Bankomat bankomat = optionalBankomat.get();

            kupyura.setId(bankomat.getKupyura().getId());
            kupyuraRepository.save(kupyura);

            bankomat.getAddress().setStreet(dto.getStreet());
            bankomat.getAddress().setCity(dto.getCity());
            bankomat.setOwnerBank(optionalBank.get());
            bankomat.setCommissionMiqdoriForOther(dto.getCommissionMiqdoriForOther());
            bankomat.setCommissionMiqdoriForOwner(dto.getCommissionMiqdoriForOwner());
            bankomat.setForSignalMaxMiqdorPul(dto.getForSignalMaxMiqdorPul());
            bankomat.setForSignalMinMiqdorPul(dto.getForSignalMinMiqdorPul());
            bankomat.setPlasticTypeList(dto.getPlasticTypeList());
            bankomat.setYechiladiganMaxPulMiqdori(dto.getYechiladiganMaxPulMiqdori());
            bankomat.setMasulXodim(usersOptional.get());

            repository.save(bankomat);

            return new ApiResult("Tahrirlandi!", true);

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
            return new ApiResult("Bunday Bankomat mavjud emas", false);
        } catch (Exception e) {
            return new ApiResult("Xatolik", false);
        }
    }

}
