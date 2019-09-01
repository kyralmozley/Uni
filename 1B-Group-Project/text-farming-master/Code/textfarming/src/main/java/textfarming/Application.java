package textfarming;

import java.nio.charset.Charset;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableScheduling
public class Application {

    //TODO: restructure the packages of our directory, and consider where to put our bean definitions
    // For now, leave all bean definitions in this class...

    //TODO: think about the fact that it is ugly to pass beans from dynamic to static via arguments
    //Try refactoring so that more of our code rests in beans...?
    // For now, just keep passing beans around. including RestTemplate!
    // In other words: refactor our code so that dependency injection is handled in a cleaner way?

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        RestTemplate restTemplate = builder.build();
        restTemplate.getMessageConverters()
        .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        return restTemplate;
    }

    public static void main(String[] args) {
        if (!(System.getProperty("file.encoding").equals("UTF-8") 
                && Charset.defaultCharset().toString().equals("UTF-8")))
            System.err.println("Platform encoding is not UTF-8!");
        else {
            SpringApplication.run(Application.class, args);
        }
    }
}
