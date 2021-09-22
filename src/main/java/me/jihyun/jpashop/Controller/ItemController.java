package me.jihyun.jpashop.Controller;

import lombok.RequiredArgsConstructor;
import me.jihyun.jpashop.domain.Item.Book;
import me.jihyun.jpashop.domain.Item.Item;
import me.jihyun.jpashop.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

//    @GetMapping("/items/new")
//    public String createForm(Model model) {
//        model.addAttribute("form", new BookForm());
//        return "items/createItemForm";
//    }

    @PostMapping("/items/new")
    public Long create(@Valid BookRequestDto requestDto) {
        Book book = new Book();
        /*
        setter를 제거하고 Book.createBook 메소드를 만들어 리턴 받는 것이 더 좋은 설계
         */
        book.setName(requestDto.getName());
        book.setPrice(requestDto.getPrice());
        book.setStockQuantity(requestDto.getStockQuantity());
        book.setAuthor(requestDto.getAuthor());
        book.setIsbn(requestDto.getIsbn());

        return itemService.saveItem(book);
    }

    @GetMapping("/items")
    public List<Item> list() {
        return itemService.findItems();

    }

//    @GetMapping("/items/{itemId}/edit")
//    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model) {
//        Book item = (Book) itemService.findOne(itemId);
//
//        BookForm form = new BookForm();
//        form = new BookForm();
//
//        form.setId(item.getId());
//        form.setName(item.getName());
//        form.setAuthor(item.getAuthor());
//        form.setPrice(item.getPrice());
//        form.setStockQuantity(item.getStockQuantity());
//        form.setIsbn(item.getIsbn());
//
//        model.addAttribute("form", form);
//        return "items/updateItemForm";
//    }

    @PostMapping("/items/{itemId}/edit")
    public String updateItem(@PathVariable("itemId") Long itemId, @ModelAttribute("form") BookRequestDto requestDto) {
        Book book = new Book();
        book.setId(requestDto.getId());
        book.setName(requestDto.getName());
        book.setPrice(requestDto.getPrice());
        book.setStockQuantity(requestDto.getStockQuantity());
        book.setAuthor(requestDto.getAuthor());
        book.setIsbn(requestDto.getIsbn());

        itemService.updateItem(book.getId(), book);
        return "redirect:/items";
    }

}
