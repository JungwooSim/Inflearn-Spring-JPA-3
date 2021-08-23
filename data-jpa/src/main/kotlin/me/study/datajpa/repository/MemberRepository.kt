package me.study.datajpa.repository

import me.study.datajpa.dto.MemberDto
import me.study.datajpa.entity.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

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
}
