package me.jihyun.jpashop.domain.Item;

import lombok.Data;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
/*
DiscriminatorValue
- 값이 주지 않으면 클래스명이 디폴트 값으로 들어감
 */
@DiscriminatorValue("Book")
@Data
public class Book extends Item{

    private String author;

    private String isbn;
}
