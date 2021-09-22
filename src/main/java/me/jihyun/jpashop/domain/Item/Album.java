package me.jihyun.jpashop.domain.Item;

import lombok.Data;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Album")
@Data
public class Album extends Item{

    private String artist;

    private String ext;
}
