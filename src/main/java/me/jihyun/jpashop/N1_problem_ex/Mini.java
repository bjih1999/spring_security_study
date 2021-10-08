package me.jihyun.jpashop.N1_problem_ex;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Mini {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String albumTitle;

    @Column
    private String locales;

//    @OneToMany(mappedBy = "mini", cascade = CascadeType.ALL, fetch = FetchType.EAGER) // 2번 상황
    @OneToMany(mappedBy = "mini", cascade = CascadeType.ALL, fetch = FetchType.LAZY) // 1번 상황
    private List<Song> songs = new ArrayList<>();
}