package me.jihyun.jpashop.service;

import lombok.RequiredArgsConstructor;
import me.jihyun.jpashop.domain.Item.Book;
import me.jihyun.jpashop.domain.Item.Item;
import me.jihyun.jpashop.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public Long saveItem(Item item) {
        return itemRepository.save(item);
    }

    /*
    준영속 엔티티 : 영속성 컨텍스트가 관리하지 않는 엔티티
        - Book book = new Book; book.setId(item.getId());와 같은 객체는 우리가 임의로 만든 객체이기 때문에
            영속성 컨텍스트가 감지하지 못한다.
        - 따라서 아무리 setter로 값을 바꿔도 자동감지(dirty check)이 일어나지 않아 영속성 컨텍스트에 저장되지 않는다.
        - 그러면 어캐하느냐는 밑에 ㄱㄱ

    준영속 엔티티를 저장하는 방법
        - 변경 감지 기능 사용 (best practice)
        - 병합(merge) 기능 사용

        - 아래의 예시가 변경 감지 기능을 사용하는 것이다. DTO를 입력받아 id로 영속성 컨텍스트에서 엔티티를 불러온 후,
            불러온 엔티티에 값을 setter로 변경한다.
        - 이 때, 불러온 엔티티는 영속성 엔티티(영속성 컨텍스트를 거쳐 불러왔기 때문)이기 때문에
            setter로 값만 변경해주면 알아서 자동감지하여 값이 수정된다.

    변경 감지 기능을 추천하는 이유
        1. 변경 감지 기능을 사용하면 원하는 속성만 선택하여 변경할 수 있지만, 병합은 모든 속성이 변경됨
        2. 모든 속성이 변경되는 과정에서 값이 없는 경우 null이 들어갈 위험이 있음(ㄹㅇ ㅋㅋ 걍 쓰지 말자)
     */
    @Transactional
    public void updateItem(Long itemId, Book param) {
        Item item = itemRepository.findOne(itemId);
        item.setName(param.getName());
        item.setStockQuantity(param.getStockQuantity());
        item.setPrice(param.getPrice());
        /*
        사실 여기서도 set으로 값을 세팅하기 보다 비즈니스로직(ex. addStock) 등의 메소드로 접근하는 것이 바람직함
         */
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }


}
