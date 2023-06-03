package com.cooperzhu.aliyundnsservice.task;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DnsCheckTaskTests {

    @Autowired
    private DnsCheckTask dnsCheckTask;

    @Test
    void testStart() {
        dnsCheckTask.start();
    }
}
