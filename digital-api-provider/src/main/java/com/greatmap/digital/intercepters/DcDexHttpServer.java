package com.greatmap.digital.intercepters;

import com.greatmap.dex.service.encrypt.DexEncryptSm2;
import com.greatmap.dex.service.httpserver.DexHttpServer;
import com.greatmap.dex.validate.DexHttpParamsValidate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class DcDexHttpServer {

    @Bean
    public DexHttpServer dexHttpServer(@Value("${dex.dcPublicKey}") String dcPublicKey,
                                       @Value("${dex.dcPrivateKey}") String dcPrivateKey,
                                       @Value("${dex.fetchPublicKeyUrl}") String fetchPublicKeyUrl,
                                       @Value("${dex.refreshPublicKey}") boolean refreshPublicKey
                                       /*@Value("${dex.mapParams}") Map<String,String> mapParams*/){
        DexEncryptSm2 dexEncryptSm2 = new DexEncryptSm2(dcPublicKey,dcPrivateKey,refreshPublicKey,null,fetchPublicKeyUrl);
        DexHttpServer dexHttpServer = new DexHttpServer();
        dexHttpServer.setDexEncrypt(dexEncryptSm2);
        return dexHttpServer;
    }

    @Bean
    public DexHttpParamsValidate paramsValidate(){
        return new DexHttpParamsValidate();
    }
}
