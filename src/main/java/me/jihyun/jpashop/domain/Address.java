package me.jihyun.jpashop.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Embeddable
@Getter
@AllArgsConstructor
public class Address {

    private String city;

    private String street;

    private String zipcode;

    protected Address() {
    }
}
