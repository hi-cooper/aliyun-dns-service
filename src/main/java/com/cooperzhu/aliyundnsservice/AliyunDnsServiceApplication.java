package com.cooperzhu.aliyundnsservice;

import com.cooperzhu.aliyundnsservice.task.DnsCheckTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@SpringBootApplication
@EnableScheduling
public class AliyunDnsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AliyunDnsServiceApplication.class, args);
    }

    @Component
    class CZApplicationRunner implements ApplicationRunner {
        @Autowired
        private DnsCheckTask dnsCheckTask;
        @Override
        public void run(ApplicationArguments args) throws Exception {
            dnsCheckTask.start();
        }
    }
}
