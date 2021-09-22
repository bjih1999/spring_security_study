package me.jihyun.jpashop.repository;

import lombok.RequiredArgsConstructor;
import me.jihyun.jpashop.domain.Item.Item;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public Long save(Item item) {
        if (item.getId() == null) {
            em.persist(item);
        } else {
            em.merge(item);
        }
        /*
        여기서 merge는 굳이 필요가 없어 보임 왜냐면 어차피 변경감지를 사용할거니까
         */

        return item.getId();
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }
}
