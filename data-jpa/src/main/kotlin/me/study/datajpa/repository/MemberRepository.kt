package me.study.datajpa.repository

import me.study.datajpa.dto.MemberDto
import me.study.datajpa.entity.Member
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.*
import org.springframework.data.repository.query.Param
import javax.persistence.LockModeType
import javax.persistence.QueryHint

interface MemberRepository : JpaRepository<Member, Long> {

    fun findByUsernameAndAgeGreaterThan(userName: String, age: Int): MutableList<Member>

    @Query(name = "Member.findByUsername")
    fun findByUsername(@Param("username") userName: String): MutableList<Member>

    @Query("select m from Member m where m.username = :username and m.age = :age")
    fun findUser(@Param("username") username: String, @Param("age") age: Int ): MutableList<Member>

    @Query("select m.username from Member m")
    fun findUsernameList(): MutableList<String>

    @Query("select new me.study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    fun findMemberDto(): MutableList<MemberDto>

    @Query("select m from Member m where m.username in :names")
    fun findByNames(@Param("names") names: MutableList<String>): MutableList<Member>

    // count 쿼리 사용
//    fun findByUsername(name: String, pageable: Pageable): Page<Member>

    // count 쿼리 사용 안함
//    fun findByUsername(name: String, pageable: Pageable): Slice<Member>

    // count 쿼리 사용
    fun findByUsername(name: String, pageable: Pageable): MutableList<Member>

    fun findByUsername(name: String, sort: Sort): MutableList<Member>

    // count query 분리 가능
    @Query(value = "select m from Member m", countQuery = "select count(m.username) from Member m")
    fun findByAge(age: Int, pageable: Pageable): Page<Member>

    @Modifying(clearAutomatically = true) // jpql 실행 후 자동으로 clean 해준다.
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    fun bulkAgePlus(@Param("age") age: Int): Int

    @Query("select m from Member m left join fetch m.team")
    fun findMemberFetchJoin(): MutableList<Member>

    @EntityGraph(attributePaths = ["team"])
    override fun findAll(): MutableList<Member>

    @EntityGraph(attributePaths = ["team"])
    @Query("select m from Member m")
    fun findMemberEntityGraph(): MutableList<Member>

    @QueryHints(value = [QueryHint(name = "org.hibernate.readOnly", value = "true")])
    fun findReadOnlyByUsername(username: String): Member

//    @Lock(LockModeType.PESSIMISTIC_WRITE)
//    fun findByUsername(name: String): MutableList<Member>
}
