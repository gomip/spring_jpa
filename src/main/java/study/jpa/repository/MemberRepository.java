package study.jpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.jpa.dto.MemberDto;
import study.jpa.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    @Query("select new study.jpa.dto.MemberDto(m.id, m.username, t.name) " +
            "from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names);

    Page<Member> findByAge(int age, Pageable pageable);

    @Query(value = "select m from Member m",
    countQuery = "select count(m.username) from Member m")
    Page<Member> findMemberAllCountBy(Pageable pageable);

    // 벌크성 정보를 수정 삭제할때는 Modifying 어노테이션을 사용
    // 벌크 연산은 영속성 컨텍스트를 무시하고 실행된다.
    @Modifying
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);
}

/*
// use count query
Page<Member> findByUsernamePage(String name, Pageable pageable);
// not using count query
// 다음페이지만 확인가능
Slice<Member> findByUsernameSlice(String name, Pageable pageable);
// not using count
List<Member> findByUsernameList(String name, Pageable pageable);
List<Member> findByUsernameSorting(String name, Sort sort);
 */