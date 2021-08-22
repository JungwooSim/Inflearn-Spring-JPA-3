package me.study.datajpa.repository

import me.study.datajpa.entity.Member
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.annotation.Rollback
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
@Rollback(false)
internal class MemberRepositoryTest(@Autowired private val memberRepository: MemberRepository) {

    @Test
    fun testMember() {
        val member = Member(username = "memberA")
        val savedMember = memberRepository.save(member)
        val findMember = memberRepository.findByIdOrNull(savedMember.id)

        Assertions.assertThat(findMember?.id).isEqualTo(member.id)
        Assertions.assertThat(findMember?.username).isEqualTo(member.username)
        Assertions.assertThat(findMember).isEqualTo(member)
    }
}
