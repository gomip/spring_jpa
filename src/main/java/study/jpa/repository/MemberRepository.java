package study.jpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
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

    // use count query
    Page<Member> findByUsernamePage(String name, Pageable pageable);
    // not using count query
    // 다음페이지만 확인가능
    Slice<Member> findByUsernameSlice(String name, Pageable pageable);
    // not using count
    List<Member> findByUsernameList(String name, Pageable pageable);
    List<Member> findByUsernameSorting(String name, Sort sort);


    Page<Member> findByAge(int age, Pageable pageable);

    @Query(value = "select m from Member m",
    countQuery = "select count(m.username) from Member m")
    Page<Member> findMemberAllCountBy(Pageable pageable);
}
