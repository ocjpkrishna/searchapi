package com.arcenium.repository;

import com.arcenium.domain.User;
import com.arcenium.exception.UserNotFoundException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@Repository
@Slf4j
public class ContactSearchRepo implements CommandLineRunner {
    private static final String USER_DATA_ENDPOINT = "https://raw.githubusercontent.com/arcjsonapi/ApiSampleData/master/api/users";

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private List<User> users;

    @Override
    public void run(String... args) throws Exception {
        this.users = fetchUserData();
    }

    public List<User> getUsers() {
        if (users == null) {
            throw new UserNotFoundException("user list is null");
        }
        return users;
    }

    public List<User> fetchUserData() {
        try {
            URL url = new URL(USER_DATA_ENDPOINT);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String inputLine;
                    StringBuilder content = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        content.append(inputLine);
                    }
                    final List<User> allUsers = objectMapper.readValue(content.toString(), new TypeReference<List<User>>() {
                    });
                    // FIXME:log.debug
                    log.info("all users from Endpoint: {} ", allUsers);
                    return allUsers;
                }
            } else {
                log.error("GET request not worked");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
