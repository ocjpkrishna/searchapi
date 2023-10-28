package com.arcenium.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class User {
    private int id;
    private String name;
    private String username;
    private String email;
    private Address address;
    private String website;
    @JsonProperty("company")
    private CompanyInfo companyInfo;

}