package me.study.datajpa.repository

import me.study.datajpa.entity.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface MemberRepository : JpaRepository<Member, Long> {

    fun findByUsernameAndAgeGreaterThan(userName: String, age: Int): MutableList<Member>

    @Query(name = "Member.findByUsername")
    fun findByUsername(@Param("username") userName: String): MutableList<Member>

}
