package com.cooperzhu.aliyundnsservice.service;

import com.cooperzhu.aliyundnsservice.util.IPAddressUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

@Service
public class IPService {

    public static void main(String[] args) {
        IPService ipService = new IPService();
        String ip = ipService.getPublicIp();
        System.out.println(ip);
    }

    public String getPublicIp() {
        String ip = this.getPublicIpBySohu();
        ip = ip != null ? ip : this.getPublicIpByIpify();
        return IPAddressUtil.isValid(ip) ? ip: null;
    }

    //region private
    private String getPublicIpBySohu() {
        String url = "http://txt.go.sohu.com/ip/soip";

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String body = response.body().string();

            String text = "window.sohu_user_ip=\"";
            int index = body.indexOf(text);
            body = body.substring(index + text.length());

            index = body.indexOf("\";");
            body = body.substring(0, index);
            return body;
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * see https://www.ipify.org/
     *
     * @return
     */
    private String getPublicIpByIpify() {
        String url = "https://api.ipify.org";

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (Exception ex) {
            return null;
        }
    }
    //endregion
}
