package com.cooperzhu.aliyundnsservice.service;

import com.aliyun.alidns20150109.Client;
import com.aliyun.alidns20150109.models.DescribeDomainRecordsRequest;
import com.aliyun.alidns20150109.models.DescribeDomainRecordsResponse;
import com.aliyun.alidns20150109.models.DescribeDomainRecordsResponseBody;
import com.aliyun.alidns20150109.models.UpdateDomainRecordRequest;
import com.aliyun.teaopenapi.models.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DnsService {
    @Autowired
    private IPService ipService;
    @Value("${userconfig.aliyun.dns.domain}")
    private String domain;
    @Value("${userconfig.aliyun.dns.access-key-id}")
    private String accessKeyId;
    @Value("${userconfig.aliyun.dns.access-key-secret}")
    private String accessKeySecret;

    public void checkAndUpdate() {
        String ip = this.ipService.getPublicIp();

        try {
            Client client = createClient();
            List<DescribeDomainRecordsResponseBody.DescribeDomainRecordsResponseBodyDomainRecordsRecord> records = this.getDescribeDomainRecords(client);
            StringBuffer sb = new StringBuffer();
            for (DescribeDomainRecordsResponseBody.DescribeDomainRecordsResponseBodyDomainRecordsRecord record : records) {
                if (!record.getType().equalsIgnoreCase("A")) {
                    continue;
                }
                if (ip.equalsIgnoreCase(record.getValue())) {
                    continue;
                }

                UpdateDomainRecordRequest request = new UpdateDomainRecordRequest()
                        .setRecordId(record.getRecordId())
                        .setRR(record.getRR())
                        .setType("A")
                        .setValue(ip);
                client.updateDomainRecord(request);
                sb.append(String.format("Updated [RR=%s, value=%s]\n", record.getRR(), ip));
            }

            if (sb.length() > 0) {
                log.info(sb.toString());
            }
        } catch (Exception ex) {
            log.error("Fail", ex);
        }
    }

    //region 获取解析记录列表
    private List<DescribeDomainRecordsResponseBody.DescribeDomainRecordsResponseBodyDomainRecordsRecord> getDescribeDomainRecords(Client client) throws Exception {
        List<DescribeDomainRecordsResponseBody.DescribeDomainRecordsResponseBodyDomainRecordsRecord> list = new ArrayList<>();
        long pageSize = 20;

        List<DescribeDomainRecordsResponseBody.DescribeDomainRecordsResponseBodyDomainRecordsRecord> tmp = new ArrayList<>();
        for (int i = 1, len = 20; i < len; i++) {
            tmp = getDescribeDomainRecords(client, i);
            list.addAll(tmp);
            if (tmp.size() < pageSize) {
                break; // 最后一页
            }
            if (i >= len) { // 达到页数循环限制时，最后一页记录仍大于pageSize，则继续往后面查询
                len += 20;
            }
        }
        return list;
    }

    private List<DescribeDomainRecordsResponseBody.DescribeDomainRecordsResponseBodyDomainRecordsRecord> getDescribeDomainRecords(Client client, long pageNumber) throws Exception {
        long pageSize = 20;
        DescribeDomainRecordsRequest request = new DescribeDomainRecordsRequest()
                .setDomainName(domain)
                .setPageSize(pageSize)
                .setPageNumber(pageNumber);
        DescribeDomainRecordsResponse response = client.describeDomainRecords(request);
        return response.getBody().getDomainRecords().getRecord();
    }
    //endregion

    private Client createClient() throws Exception {
        Config config = new Config()
                .setAccessKeyId(accessKeyId)
                .setAccessKeySecret(accessKeySecret);
        config.endpoint = "alidns.cn-hangzhou.aliyuncs.com";
        return new Client(config);
    }
}
