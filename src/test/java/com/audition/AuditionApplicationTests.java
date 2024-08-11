package com.audition;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource("classpath:application.yml")
class AuditionApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void testMain() {
        AuditionApplication.main(new String[]{});
    }
}
