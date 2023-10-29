package com.arcenium.controller;

import com.arcenium.service.ContactSearchService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@Slf4j
@AllArgsConstructor
public class UserSearchController {

    private  ContactSearchService contactSearchService;

    @GetMapping
    public List<Integer> searchUsers(@RequestParam String field,
                                     @RequestParam String operation,
                                     @RequestParam(required = false) List<String> values) {
        final List<Integer> response = contactSearchService.apiResponse(field, operation, values);
        log.info("response: {} ",response);
        return contactSearchService.apiResponse(field, operation, values);
    }

}
