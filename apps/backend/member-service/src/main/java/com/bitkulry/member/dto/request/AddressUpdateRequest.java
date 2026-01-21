package com.bitkulry.member.dto.request;

import lombok.Getter;

@Getter
public class AddressUpdateRequest {
    private String zipcode;
    private String address;
    private String addressDetail;
}
