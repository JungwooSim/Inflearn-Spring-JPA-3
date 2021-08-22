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

    @Test
    fun basicCRUD() {
        val member1 = Member(username = "member1")
        val member2 = Member(username = "member2")
        memberJpaRepository.save(member1)
        memberJpaRepository.save(member2)

        //단건 조회 검증
        val findMember1 = memberJpaRepository.findById(member1.id!!).get()
        val findMember2 = memberJpaRepository.findById(member2.id!!).get()
        assertThat(findMember1).isEqualTo(member1)
        assertThat(findMember2).isEqualTo(member2)

        //리스트 조회 검증
        val count: Long = memberJpaRepository.count()
        assertThat(count).isEqualTo(2)

        //삭제 검증
        memberJpaRepository.delete(member1)
        memberJpaRepository.delete(member2)
        val deletedCount = memberJpaRepository.count()
        assertThat(deletedCount).isEqualTo(0)
    }
}
