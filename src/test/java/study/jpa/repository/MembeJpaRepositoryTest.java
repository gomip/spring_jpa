package study.jpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.jpa.entity.Member;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class MembeJpaRepositoryTest {
    @Autowired MembeJpaRepository memberJpaRepository;

    @Test
    public void testMember(){
        // given
        Member member = new Member("memberA");
        Member saveMember = memberJpaRepository.save(member);

        // when
        Member findMember = memberJpaRepository.find(saveMember.getId());

        // then
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(saveMember);
    }

    @Test
    public void basicCRUD() {
        //given
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        // when & then
        Member findMember1 = memberJpaRepository.findById(member1.getId()).get();
        Member findMember2 = memberJpaRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        // when & then
        List<Member> all = memberJpaRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        // when & then
        long count = memberJpaRepository.count();
        assertThat(count).isEqualTo(2);

        // when & then
        memberJpaRepository.delete(member1);
        memberJpaRepository.delete(member2);
        long deleteCount = memberJpaRepository.count();
        assertThat(deleteCount).isEqualTo(0);
    }

    @Test
    public void findByUsernameAndAgeGreaterThan(){
        //given
        Member member1 = new Member("member1", 10);
        Member member2 = new Member("member1", 20);
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        // when
        List<Member> result = memberJpaRepository.findByUsernameAndAgeGreaterThan("member1", 15);

        // then
        assertThat(result.get(0).getUsername()).isEqualTo("member1");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void paging() throws Exception {
        // given
        memberJpaRepository.save(new Member("member1", 10));
        memberJpaRepository.save(new Member("member2", 10));
        memberJpaRepository.save(new Member("member3", 10));
        memberJpaRepository.save(new Member("member4", 10));
        memberJpaRepository.save(new Member("member5", 10));
        memberJpaRepository.save(new Member("member6", 10));

        int age = 10;
        int offset = 0;
        int limit = 3;

        // When
        List<Member> members = memberJpaRepository.findByPage(age, offset, limit);
        long total = memberJpaRepository.totalCount(age);
        // formula
        // total page = total / size

        // then
        assertThat(members.size()).isEqualTo(3);
        assertThat(total).isEqualTo(6);
    }

    @Test
    public void bulkUpdate() throws Exception {
        // given
        memberJpaRepository.save(new Member("member1", 10));
        memberJpaRepository.save(new Member("member2", 19));
        memberJpaRepository.save(new Member("member3", 20));
        memberJpaRepository.save(new Member("member4", 21));
        memberJpaRepository.save(new Member("member5", 39));

        // when
        int result = memberJpaRepository.bulkAgePlus(20);

        // then
        assertThat(result).isEqualTo(3);
    }
}