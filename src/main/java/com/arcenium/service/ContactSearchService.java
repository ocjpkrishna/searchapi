package com.arcenium.service;

import com.arcenium.controller.repository.ContactSearchRepo;
import com.arcenium.domain.User;
import com.arcenium.exception.UserExceedException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class ContactSearchService {
    @Autowired
    private ContactSearchRepo contactSearchRepo;

    public List<Integer> apiResponse(String field, String operation, List<String> values) {
        List<User> users = contactSearchRepo.fetchUserData();

        if (values.size() > 3) {
            throw new UserExceedException("more than three users are not allowed");
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
}
