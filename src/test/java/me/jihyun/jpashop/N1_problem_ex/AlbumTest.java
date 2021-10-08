package me.jihyun.jpashop.N1_problem_ex;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@SpringBootTest
class AlbumTest {

    @Autowired
    MiniRepository miniRepository;

    @Autowired
    SongRepository songRepository;

    @Autowired
    EntityManager em;

    @Test
    @Transactional()
    public void N1_쿼리테스트_EAGER() throws Exception{

        for (int i = 0 ; i < 100 ; i++) {
            Mini mini = new Mini();
            mini.setAlbumTitle("test" + i);
            miniRepository.save(mini);
        }


        em.flush();
        em.clear();
//        song1.setMini(mini);
//        song2.setMini(mini);
//        song3.setMini(mini);

        List<Mini> minis = miniRepository.findAll();
        System.out.println(minis.size());


        for (Song song : minis.get(0).getSongs()) {
            System.out.println(song.getTitle());
        }
    }

    @Test
    @Transactional
    public void N1_쿼리테스트_2_LAZY() throws Exception{

        for (int i = 0 ; i < 100 ; i++) {
            Mini mini = new Mini();
            mini.setAlbumTitle("test" + i);
            miniRepository.save(mini);
        }

//        for (int i = 0 ; i < 100 ; i++) {
//            Mini mini = new Mini();
//            mini.setAlbumTitle("test" + i);
//            miniRepository.save(mini);
//        }


        em.flush();
        em.clear();
//        song1.setMini(mini);
//        song2.setMini(mini);
//        song3.setMini(mini);

        List<Mini> minis = miniRepository.findAll();
        System.out.println(minis.size());


        for (Mini mini : minis) {
            System.out.println(mini.getSongs().size());
        }
    }

}