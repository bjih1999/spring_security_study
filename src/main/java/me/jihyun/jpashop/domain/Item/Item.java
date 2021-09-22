package me.jihyun.jpashop.domain.Item;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import me.jihyun.jpashop.domain.Category;
import me.jihyun.jpashop.exception.NotEnoughStockException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
public abstract class Item {

    @Id @GeneratedValue
    private Long id;

    private String name;

    private int price;

    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    //== 비즈니스 로직==//
    /*
    - Setter를 닫고 비즈니스 로직을 추가함으로써 외부에서 값을 조정하는 것이 아니라 비즈니스 로직을 통해 값을 조정함 -> 캡슐화
     */

    /*
    나의 의문
        Q: 왜 entity에 대한 로직을 service에서 처리하지 않고 entity에서 처리를 하는가?
        A: 데이터를 가지고 있는쪽에 비즈니스 메소드가 있는 것이 응집력이 좋음(service에서 처리 하면 일일이 데이터를 가져와야하는 번거로움이 생기기 때문인 듯 하다)
     */
    /**
     * stock 증가
     */
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    /**
     * stock 감소
     */
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }
}
