package fan.company.bankomatspringboot.service;

import fan.company.bankomatspringboot.entity.*;
import fan.company.bankomatspringboot.payload.dto.ApiResult;
import fan.company.bankomatspringboot.payload.dto.PayDto;
import fan.company.bankomatspringboot.repository.AuditBankomatRepository;
import fan.company.bankomatspringboot.repository.BankomatRepository;
import fan.company.bankomatspringboot.repository.CardRepository;
import fan.company.bankomatspringboot.repository.KupyuraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PayService {

    @Autowired
    BankomatRepository bankomatRepository;
    @Autowired
    CardRepository cardRepository;
    @Autowired
    KupyuraRepository kupyuraRepository;
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    AuditBankomatService auditBankomatService;
    @Autowired
    AuditBankomatRepository auditBankomatRepository;


    public ApiResult carddanCardgaPulTransaksiyasi(PayDto dto) {


        Optional<Bankomat> optionalBankomat = bankomatRepository.findById(dto.getBankomatId());
        if (optionalBankomat.isEmpty())
            return new ApiResult("Bankomat topilmadi!", false);

        Optional<Card> optionalCardFrom = cardRepository.findById(dto.getFromCardId());
        if (optionalCardFrom.isEmpty())
            return new ApiResult("Card topilmadi!", false);

        Optional<Card> optionalCardTo = cardRepository.findById(dto.getToCardId());
        if (optionalCardTo.isEmpty())
            return new ApiResult("Qabul qiluvchi Card topilmadi!", false);

        Card card = optionalCardFrom.get();


        if (card.getPincode() == dto.getPinCode()) {
            sanoqHatoPinCode(card);
            return new ApiResult("Pin code hato!", false);
        }

        if (!card.isActive())
            return new ApiResult("Cardni aktiv emas. Bankga murojat qiling!", false);

        Bankomat bankomat = optionalBankomat.get();

        double miqdor = 0;


        boolean owner = false;

        if (card.getBank().getId() == bankomat.getOwnerBank().getId()) {
            owner = true;
            miqdor = dto.getTransaksiyaPulMiqdori() + (dto.getTransaksiyaPulMiqdori() / 100 * bankomat.getCommissionMiqdoriForOwner());
        } else {
            miqdor = dto.getTransaksiyaPulMiqdori() + (dto.getTransaksiyaPulMiqdori() / 100 * bankomat.getCommissionMiqdoriForOther());
        }

        if (card.getQoldiqMablag() < miqdor)
            return new ApiResult("Sizda yetarlicha mablag' mavjud emas! Joriy qoldiq maglag' " + card.getQoldiqMablag(), false);

        if (owner) {
            miqdor = dto.getTransaksiyaPulMiqdori() - (dto.getTransaksiyaPulMiqdori() / 100 * bankomat.getCommissionMiqdoriForOwner());
        } else {
            miqdor = dto.getTransaksiyaPulMiqdori() - (dto.getTransaksiyaPulMiqdori() / 100 * bankomat.getCommissionMiqdoriForOther());
        }

        miqdor = card.getQoldiqMablag() - miqdor;

        card.setQoldiqMablag(miqdor);
        card.setSanoq(0);

        cardRepository.save(card);

        Card cardTo = optionalCardTo.get();
        cardTo.setQoldiqMablag(cardTo.getQoldiqMablag() + miqdor);

        cardRepository.save(cardTo);


        auditBankomatService.audit(bankomat, card.getMijoz(), card, Amal.CARDDAN_PUL_YECHISH, miqdor);

        return new ApiResult("Transaksiya amalga oshirildi! Joriy qoldiq ' " + card.getQoldiqMablag(), true);
    }

    public ApiResult cardgaPulTashashTransaksiyasi(PayDto dto) {

        Optional<Bankomat> optionalBankomat = bankomatRepository.findById(dto.getBankomatId());
        if (optionalBankomat.isEmpty())
            return new ApiResult("Bankomat topilmadi!", false);

        Optional<Card> optionalCardTo = cardRepository.findById(dto.getToCardId());
        if (optionalCardTo.isEmpty())
            return new ApiResult("Card topilmadi!", false);

        Card card = optionalCardTo.get();

        if (card.getPincode() == dto.getPinCode()) {
            sanoqHatoPinCode(card);
            return new ApiResult("Pin code hato!", false);
        }

        if (!card.isActive())
            return new ApiResult("Cardni aktiv emas. Bankga murojat qiling!", false);


        Bankomat bankomat = optionalBankomat.get();

        double miqdor = 0;

        if (card.getBank().getId() == bankomat.getOwnerBank().getId()) {
            miqdor = dto.getTransaksiyaPulMiqdori() - (dto.getTransaksiyaPulMiqdori() / 100 * bankomat.getCommissionMiqdoriForOwner());
        } else {
            miqdor = dto.getTransaksiyaPulMiqdori() - (dto.getTransaksiyaPulMiqdori() / 100 * bankomat.getCommissionMiqdoriForOther());
        }
        card.setQoldiqMablag(card.getQoldiqMablag() + miqdor);
        card.setSanoq(0);

        cardRepository.save(card);

        //Kupyurani sanash

        Kupyura savedKupyura = kupyuraRepository.findById(bankomat.getKupyura().getId()).get();

        Kupyura kupyura = new Kupyura(
                savedKupyura.getMingSomCount() + dto.getMingSomCount(), dto.getBeshmingSomCount() + savedKupyura.getBeshmingSomCount(), savedKupyura.getOnmingSomCount() + dto.getOnmingSomCount(),
                savedKupyura.getEllikmingSomCount() + dto.getEllikmingSomCount(), savedKupyura.getYuzmingSomCount() + dto.getYuzmingSomCount(), savedKupyura.getIkkimingSomCount() + dto.getIkkimingSomCount()
        );

        kupyura.setId(savedKupyura.getId());

        Kupyura save = kupyuraRepository.save(kupyura);

        Double maxMiqdor = Double.valueOf(save.getMingSomCount() * 1000 + save.getIkkimingSomCount() * 2000 + save.getBeshmingSomCount() * 5000 + save.getOnmingSomCount() * 10000 + save.getEllikmingSomCount() * 50000 + save.getYuzmingSomCount() * 100000);

        //max pul miqdori bo'lganda emailga habar jo'natish
        if (bankomat.getForSignalMaxMiqdorPul() <= maxMiqdor)
            sendMail(bankomat.getMasulXodim().getEmail(), "Bankomatda max pul miqdori! Address " + bankomat.getAddress() + ", Bankomat id - " + bankomat.getId());

        auditBankomatService.audit(bankomat, card.getMijoz(), card, Amal.CARDGA_PUL_UTKAZISH, miqdor);

        return new ApiResult("Transaksiya amalga oshirildi! Joriy qoldiq' " + card.getQoldiqMablag(), true);
    }

    public ApiResult carddanPulYechishTransaksiyasi(PayDto dto) {

        Optional<Bankomat> optionalBankomat = bankomatRepository.findById(dto.getBankomatId());
        if (optionalBankomat.isEmpty())
            return new ApiResult("Bankomat topilmadi!", false);

        Optional<Card> optionalCardTo = cardRepository.findById(dto.getToCardId());
        if (optionalCardTo.isEmpty())
            return new ApiResult("Card topilmadi!", false);

        Card card = optionalCardTo.get();
        if (card.getPincode() == dto.getPinCode()) {
            sanoqHatoPinCode(card);
            return new ApiResult("Pin code hato!", false);
        }

        if (!card.isActive())
            return new ApiResult("Cardni aktiv emas. Bankga murojat qiling!", false);



        Bankomat bankomat = optionalBankomat.get();

        if(bankomat.getForSignalMaxMiqdorPul()<dto.getTransaksiyaPulMiqdori())
            return new ApiResult("Bankomatda yetarlicha mablag' mavjud emas!", false);

        double miqdor = 0;

        boolean owner = false;

        if (card.getBank().getId() == bankomat.getOwnerBank().getId()) {
            owner = true;
            miqdor = dto.getTransaksiyaPulMiqdori() + (dto.getTransaksiyaPulMiqdori() / 100 * bankomat.getCommissionMiqdoriForOwner());
        } else {
            miqdor = dto.getTransaksiyaPulMiqdori() + (dto.getTransaksiyaPulMiqdori() / 100 * bankomat.getCommissionMiqdoriForOther());
        }

        if (card.getQoldiqMablag() < miqdor)
            return new ApiResult("Sizda yetarlicha mablag' mavjud emas! Joriy qoldiq maglag' " + card.getQoldiqMablag(), false);

        if (owner) {
            miqdor = dto.getTransaksiyaPulMiqdori() - (dto.getTransaksiyaPulMiqdori() / 100 * bankomat.getCommissionMiqdoriForOwner());
        } else {
            miqdor = dto.getTransaksiyaPulMiqdori() - (dto.getTransaksiyaPulMiqdori() / 100 * bankomat.getCommissionMiqdoriForOther());
        }

        miqdor = card.getQoldiqMablag() - miqdor;

        card.setQoldiqMablag(miqdor);
        card.setSanoq(0);
        cardRepository.save(card);

        //Kupyurani sanash
        Kupyura savedKupyura = kupyuraRepository.findById(bankomat.getKupyura().getId()).get();

        Kupyura kupyura = new Kupyura(
                savedKupyura.getMingSomCount() - dto.getMingSomCount(), savedKupyura.getBeshmingSomCount() - dto.getBeshmingSomCount(), savedKupyura.getOnmingSomCount() - dto.getOnmingSomCount(),
                savedKupyura.getEllikmingSomCount() - dto.getEllikmingSomCount(), savedKupyura.getYuzmingSomCount() - dto.getYuzmingSomCount(), savedKupyura.getIkkimingSomCount() - dto.getIkkimingSomCount()
        );

        kupyura.setId(savedKupyura.getId());

        Kupyura save = kupyuraRepository.save(kupyura);

        //max pul miqdori bo'lganda emailga habar jo'natish
        Double maxMiqdor = Double.valueOf(save.getMingSomCount() * 1000 + save.getIkkimingSomCount() * 2000 + save.getBeshmingSomCount() * 5000 + save.getOnmingSomCount() * 10000 + save.getEllikmingSomCount() * 50000 + save.getYuzmingSomCount() * 100000);

        if (bankomat.getForSignalMaxMiqdorPul() <= maxMiqdor)
            sendMail(bankomat.getMasulXodim().getEmail(), "Bankomatda max pul miqdori! Address " + bankomat.getAddress() + ", Bankomat id - " + bankomat.getId());

        if (bankomat.getForSignalMaxMiqdorPul() <= bankomat.getForSignalMinMiqdorPul())
            sendMail(bankomat.getMasulXodim().getEmail(), "Bankomatda max pul miqdori kam! Address " + bankomat.getAddress() + ", Bankomat id - " + bankomat.getId() + ".  Qoldiq - " + bankomat.getForSignalMaxMiqdorPul());

        auditBankomatService.audit(bankomat, card.getMijoz(), card, Amal.CARDDAN_PUL_YECHISH, miqdor);


        return new ApiResult("Transaksiya amalga oshirildi! Joriy qoldiq' " + card.getQoldiqMablag(), true);
    }

    public ApiResult bankomatniToldirish(PayDto dto) {

        Optional<Bankomat> optionalBankomat = bankomatRepository.findById(dto.getBankomatId());
        if (optionalBankomat.isEmpty())
            return new ApiResult("Bankomat topilmadi!", false);

        Bankomat bankomat = optionalBankomat.get();

        double miqdor = 0;

        boolean owner = false;


        //Kupyurani sanash
        Kupyura savedKupyura = kupyuraRepository.findById(bankomat.getKupyura().getId()).get();

        Kupyura kupyura = new Kupyura(
                savedKupyura.getMingSomCount() + dto.getMingSomCount(), savedKupyura.getBeshmingSomCount() + dto.getBeshmingSomCount(), savedKupyura.getOnmingSomCount() + dto.getOnmingSomCount(),
                savedKupyura.getEllikmingSomCount() + dto.getEllikmingSomCount(), savedKupyura.getYuzmingSomCount() + dto.getYuzmingSomCount(), savedKupyura.getIkkimingSomCount() + dto.getIkkimingSomCount()
        );

        kupyura.setId(savedKupyura.getId());

        Kupyura save = kupyuraRepository.save(kupyura);

        //max pul miqdori bo'lganda emailga habar jo'natish
        Double maxMiqdor = Double.valueOf(save.getMingSomCount() * 1000 + save.getIkkimingSomCount() * 2000 + save.getBeshmingSomCount() * 5000 + save.getOnmingSomCount() * 10000 + save.getEllikmingSomCount() * 50000 + save.getYuzmingSomCount() * 100000);

        sendMail(bankomat.getMasulXodim().getEmail(), "Transaksiya amalga oshirildi! Joriy naqd pul miqdori' " + bankomat.getForSignalMaxMiqdorPul() + "! Address " + bankomat.getAddress() + ", Bankomat id - " + bankomat.getId());

        auditBankomatService.audit(bankomat, bankomat.getMasulXodim(), null, Amal.BANKOMATNI_TULDIRISH, miqdor);

        return new ApiResult("Transaksiya amalga oshirildi! Joriy naqd pul miqdori' " + bankomat.getForSignalMaxMiqdorPul(), true);
    }

    public Boolean sendMail(String sendingEmail, String message) {

        try {

            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom("company@fan.uz");
            simpleMailMessage.setTo(sendingEmail);
            simpleMailMessage.setSubject("company@fan.uz tizimida accaountni tasdiqlash");
            simpleMailMessage.setText(message);
            System.out.println(message);
            javaMailSender.send(simpleMailMessage);
            return true;

        } catch (MailException e) {
            e.printStackTrace();
            return false;
        }

    }

    private void sanoqHatoPinCode(Card card) {
        card.setSanoq(card.getSanoq() + 1);
        if (card.getSanoq() >= 2)
            card.setActive(false);

        cardRepository.save(card);
    }


}
