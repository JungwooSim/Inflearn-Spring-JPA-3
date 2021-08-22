package me.study.datajpa.repository

import me.study.datajpa.entity.Member
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Rollback
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
@Rollback(false)
internal class MemberJpaRepositoryTest(@Autowired private val memberJpaRepository: MemberJpaRepository) {

    @Test
    fun testMember() {
        val member = Member(username = "memberA")
        val savedMember = memberJpaRepository.save(member)
        val findMember = memberJpaRepository.find(savedMember.id!!)

        assertThat(findMember.id).isEqualTo(member.id)
        assertThat(findMember.username).isEqualTo(member.username)
        assertThat(findMember).isEqualTo(member)
    }
}
