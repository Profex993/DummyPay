package com.egersoft.dummypay.utils;

import com.egersoft.dummypay.repository.PaymentSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@RequiredArgsConstructor
public class IdGenerator {
    private final PaymentSessionRepository sessionRepository;
    private final int NUMBER_OF_TRIES = 5;
    private final int ID_LENGTH = 100000;


    public long generateId() throws Exception {
        int tries = 0;
        Random random = new Random();

        while (tries < NUMBER_OF_TRIES) {
            long id = random.nextInt(ID_LENGTH);

            if (!sessionRepository.existsById(id)) return id;

            tries++;
        }
        throw new Exception("failed to create unique ID");
    }
}
