package com.cooperzhu.aliyundnsservice.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DnsServiceTests {

    @Autowired
    private DnsService dnsService;

    @Test
    public void testCheckAndUpdate() {
        dnsService.checkAndUpdate();
    }
}
