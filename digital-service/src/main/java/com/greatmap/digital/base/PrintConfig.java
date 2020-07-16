package com.greatmap.digital.base;

import com.greatmap.print.model.PrintParamModel;
import com.greatmap.print.service.PrintService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author gaorui
 * @create 2019-12-10 15:37
 */
@Configuration
public class PrintConfig {

    @Bean
    public PrintService printService(@Value("${spring.datasource.url}") String url,
                                     @Value("${spring.datasource.username}") String username,
                                     @Value("${spring.datasource.password}") String password,
                                     @Value("${wcf.wcfServer}") String wcfServer,
                                     @Value("${wcf.estatePrintUrl}") String printUrl,
                                     @Value("${wcf.tempDirectory}") String tempDirectory,
                                     @Value("${wcf.relativePath}") String relativePath,
                                     @Value("${ftp.host}") String ftpHost,
                                     @Value("${ftp.user}") String ftpUser,
                                     @Value("${ftp.psw}") String ftpPsw,
                                     @Value("${ftp.http}") String ftpHttp) {
        PrintParamModel model = new PrintParamModel();
        model.setFtpHost(ftpHost);
        model.setWcfServer(wcfServer);
        model.setPrintUrl(printUrl);
        model.setTempDirecotry(tempDirectory);
        model.setRelativePath(relativePath);
        model.setJdbcUrl(url);
        model.setJdbcUserName(username);
        model.setJdbcPassword(password);
        model.setFtpPwd(ftpPsw);
        model.setFtpUser(ftpUser);
        model.setFtpHost(ftpHost);
        model.setHttpServer(ftpHttp);
        PrintService printService = new PrintService();
        printService.setPrintParamModel(model);
        return printService;
    }
}
