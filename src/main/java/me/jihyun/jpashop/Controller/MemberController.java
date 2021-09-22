package me.jihyun.jpashop.Controller;

import lombok.RequiredArgsConstructor;
import me.jihyun.jpashop.domain.Address;
import me.jihyun.jpashop.domain.Member;
import me.jihyun.jpashop.service.MemberService;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

//    @GetMapping("members/new")
//    public String creatForm(Model model) {
//        model.addAttribute("memberForm", new MemberForm());
//        return "members/createMemberForm";
//    }

    @PostMapping("members/new")
    public Long create(@Valid MemberCreateRequestDto requestDto, BindingResult result) {

        if (result.hasErrors()) {
            result.getAllErrors().forEach(objectError->{
                System.err.println("code : " + objectError.getCode());
                System.err.println("defaultMessage : " + objectError.getDefaultMessage());
                System.err.println("objectName : " + objectError.getObjectName()); });
        }

        Address address = new Address(requestDto.getCity(), requestDto.getStreet(), requestDto.getZipcode());

        Member member = Member.of(requestDto.getName(), address);

        return memberService.join(member);
    }

    /*
    화면을 출력할 때도 Dto를 사용하는 게 좋음
    API의 경우는 엔티티를 반환하면 절대 안됨
        - API는 스펙이 변함
    */
    @GetMapping("/members")
    public List<Member> list(Model model) {
        return memberService.findMembers();
    }
}
