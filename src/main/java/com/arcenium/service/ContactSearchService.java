package com.arcenium.service;

import com.arcenium.domain.User;
import com.arcenium.exception.UserExceedException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
public class ContactSearchService {

    private static final String USER_DATA_ENDPOINT = "https://raw.githubusercontent.com/arcjsonapi/ApiSampleData/master/api/users";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final List<User> users;

    public ContactSearchService() {
        this.users = fetchUserData();
    }

    public ContactSearchService(List<User> users) {
        this.users = users;
    }

    public List<Integer> apiResponse(String field, String operation, List<String> values) {
        if (values.size() > 3) {
            throw new UserExceedException("more than three User not allowed");
        }
        List<Integer> result = new ArrayList<>();
        for (String value : values) {
            Optional<Integer> userId = users.stream()
                    .filter(user -> matchesCriteria(user, field, operation, value))
                    .findFirst()
                    .map(User::getId);
            result.add(userId.orElse(-1));
        }
        return result;
    }

    private boolean matchesCriteria(User user, String fieldName, String operationType, String value) {
        try {
            String[] fieldParts = fieldName.split("\\.");

            Object currentObject = user;
            for (String part : fieldParts) {
                Field field = currentObject.getClass().getDeclaredField(part);
                field.setAccessible(true);
                currentObject = field.get(currentObject);
            }

            if (currentObject == null) {
                return false;
            }

            String stringValue = currentObject.toString();

            switch (operationType) {
                case "Equals":
                    return stringValue.equals(value);
                case "IN":
                    List<String> values = Arrays.asList(value.split(","));
                    return values.contains(stringValue);
                default:
                    throw new IllegalArgumentException("Unsupported operation type: " + operationType);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return false;
        }
    }

    private List<User> fetchUserData() {
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
                    final List<User> users1 = objectMapper.readValue(content.toString(), new TypeReference<List<User>>() {
                    });
                    log.info("all users from Endpoint: {} ", users1);
                    return users1;
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
