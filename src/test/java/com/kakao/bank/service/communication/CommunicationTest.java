package com.kakao.bank.service.communication;

import com.kakao.bank.domain.dto.communication.BroughtAccountDto;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class CommunicationTest {
    @Autowired
    private CommunicationService communicationService;

    @Autowired
    private CommunicationServiceImpl c;

//    @Test
//    void getOtherAccounts() {
//        communicationService.getOtherAccounts("01027177581");
//    }

    @Test
    void parseData() {
        try {
            List<BroughtAccountDto> account = c.getTossAccount("01098761234");

            for (BroughtAccountDto broughtAccountDto : account) {
                System.out.println(broughtAccountDto.toString());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getOtherAccounts() {
        try {
            List<BroughtAccountDto> otherAccounts = c.getOtherAccounts("01012341234");
            for (BroughtAccountDto otherAccount : otherAccounts) {
                System.out.println(otherAccount.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
