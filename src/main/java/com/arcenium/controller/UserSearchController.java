package com.arcenium.controller;

import com.arcenium.service.ContactSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/search")
public class UserSearchController {

    private final ContactSearchService contactSearchService;

    @Autowired
    public UserSearchController(ContactSearchService contactSearchService) {
        this.contactSearchService = contactSearchService;
    }

    @GetMapping
    public List<Integer> searchUsers(@RequestParam String field,
                                     @RequestParam String operation,
                                     @RequestParam(required = false) List<String> values) {

        return contactSearchService.apiResponse(field, operation, values);
    }

}
