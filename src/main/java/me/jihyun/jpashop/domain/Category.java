package me.jihyun.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.jihyun.jpashop.domain.Item.Item;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id @GeneratedValue
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable(name = "category_item",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    /*
    JoinTable
    ManyToMany 관계는 JoinTable이 필요함
    - Why?
        엔티티는 각자의 collection을 속성으로 가지면 되지만 테이블은 collection을 갖는 것이 불가능하기 때문에
        중간에 매핑 테이블을 가져야 함

    *** 매핑 테이블 사용은 실전에서 지양해야함!!!! ***
    -Why?
        1. 필드를 추가할 수 없어서 실무에서 거의 못씀
            ex) 실무에서는 단순히 매핑 관계 이외에 등록한 날짜 같은 부가 정보도 입력해주어야하는데
                매핑 테이블을 사용할 경우 다른 필드를 추가할 수 없기 때문
        2. 중간 테이블이 숨겨져 있기 때문에 예상하지 못하는 쿼리들이 나감

     p.s 그럼 실무에서는 다 대 다 관계 못쓰나...? 왜 안알려줘...?
        - OK 알려줌
            - 매핑 테이블을 Entity로 꺼내면 된다. 즉, JPA에 의해 가려진 매핑 테이블을 정식 Entity로 승격시키는 것이다.
              중간에 매핑 테이블 Entity를 만들어 다 대 일, 일 대 다 로 묶어주고, 필요한 필드를 추가하면 된다.
            - 이렇게 해주면 Entity가 존재하기 때문에 Repository도 생성할 수 있게 되어 관리가 편해지는 장점이 있다.
            p.s PK는 당연히 독립적으로 생성되도록 한다.

     일단은 예제를 따라해보려고 한다.
     */
    private List<Item> items = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();


    public static Category of(Category parent, String name) {
        Category category = new Category();
        category.setName(name);

        category.setParent(parent);
        parent.addChildCategory(category);
        return category;
    }

    private void setParent(Category parent) {
        this.parent = parent;
    }

    private void setName(String name) {
        this.name = name;
    }

    private void addChildCategory(Category child) {
        this.child.add(child);
        child.setParent(this);
    }
}
