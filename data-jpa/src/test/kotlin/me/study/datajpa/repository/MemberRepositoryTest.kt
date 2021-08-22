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
        val member = Member(username = "memberB")
        val savedMember = memberRepository.save(member)
        val findMember = memberRepository.findByIdOrNull(savedMember.id)

        Assertions.assertThat(findMember?.id).isEqualTo(member.id)
        Assertions.assertThat(findMember?.username).isEqualTo(member.username)
        Assertions.assertThat(findMember).isEqualTo(member)
    }

    @Test
    fun basicCRUD() {
        val member1 = Member(username = "member1")
        val member2 = Member(username = "member2")
        memberRepository.save(member1)
        memberRepository.save(member2)

        //단건 조회 검증
        val findMember1 = memberRepository.findById(member1.id!!).get()
        val findMember2 = memberRepository.findById(member2.id!!).get()
        Assertions.assertThat(findMember1).isEqualTo(member1)
        Assertions.assertThat(findMember2).isEqualTo(member2)

        //리스트 조회 검증
        val count: Long = memberRepository.count()
        Assertions.assertThat(count).isEqualTo(2)

        //삭제 검증
        memberRepository.delete(member1)
        memberRepository.delete(member2)
        val deletedCount = memberRepository.count()
        Assertions.assertThat(deletedCount).isEqualTo(0)
    }
}
