package com.arcenium;


import com.arcenium.service.ContactSearchService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@Slf4j
@SpringBootApplication
@AllArgsConstructor
public class ArceniumApplication {


    public static void main(String[] args) {
        SpringApplication.run(ArceniumApplication.class, args);

    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ContactSearchService contactSearchService() {
        return new ContactSearchService();
    }
}



/*public static void main(String[] args) {
    ContactSearchService searchService = new ContactSearchService();

    List<Integer> result1 = searchService.apiResponse(Arrays.asList("username", "Equals", "krishna"));
    System.out.println(result1); // Output: [1]

    List<Integer> result2 = searchService.apiResponse(Arrays.asList("address.city", "IN", "Mumbai,Kolkata"));
    System.out.println(result2); // Output: [1, 2]

    List<Integer> result3 = searchService.apiResponse(Arrays.asList("username", "Equals", "Tom"));
    System.out.println(result3); // Output: [-1]
}
}*/