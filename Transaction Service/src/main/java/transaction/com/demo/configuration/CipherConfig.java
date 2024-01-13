package transaction.com.demo.configuration;





import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import transaction.com.demo.utils.CipherUtil;

import javax.annotation.PostConstruct;


@Configuration
@ConfigurationProperties(prefix = "cipher")
@Setter
public class CipherConfig {
    private String key;
    private String algorithm;

    @PostConstruct
    void initialize() {
        CipherUtil.initializeCipherUtils(key, algorithm);
    }
}
