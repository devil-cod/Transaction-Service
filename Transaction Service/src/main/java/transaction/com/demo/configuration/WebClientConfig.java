package transaction.com.demo.configuration;

import io.netty.handler.logging.LogLevel;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

import javax.net.ssl.SSLException;
import java.time.Duration;

@Configuration
public class WebClientConfig {

    @Bean
    @Profile(value = "!local")
    public WebClient webClient(WebClient.Builder webClientBuilder) {

        ConnectionProvider provider = ConnectionProvider
            .builder("webClientConfig")
            .maxConnections(500)
            .maxIdleTime(Duration.ofSeconds(20))
            .maxLifeTime(Duration.ofSeconds(60))
            .pendingAcquireTimeout(Duration.ofSeconds(60))
            .evictInBackground(Duration.ofSeconds(120)).build();

        var connector = new ReactorClientHttpConnector(
            HttpClient.create(provider).wiretap(this.getClass().getCanonicalName(),
                LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL));

        return webClientBuilder
            .clientConnector(connector)
            .build();
    }

    @Bean
    @Profile(value = "local")
    public WebClient webClientForLocal() throws SSLException {

        var sslContext = SslContextBuilder
            .forClient()
            .trustManager(InsecureTrustManagerFactory.INSTANCE)
            .build();

        var connector = new ReactorClientHttpConnector(
            HttpClient.create().secure(t -> t.sslContext(sslContext))
                .wiretap(this.getClass().getCanonicalName(),
                    LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL));

        return WebClient.builder()
            .clientConnector(connector)
            .build();
    }
}