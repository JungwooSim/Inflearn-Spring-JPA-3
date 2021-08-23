package me.study.datajpa.repository

import me.study.datajpa.entity.Member
import org.assertj.core.api.Assertions.*
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

        assertThat(findMember?.id).isEqualTo(member.id)
        assertThat(findMember?.username).isEqualTo(member.username)
        assertThat(findMember).isEqualTo(member)
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
        assertThat(findMember1).isEqualTo(member1)
        assertThat(findMember2).isEqualTo(member2)

        //리스트 조회 검증
        val count: Long = memberRepository.count()
        assertThat(count).isEqualTo(2)

        //삭제 검증
        memberRepository.delete(member1)
        memberRepository.delete(member2)
        val deletedCount = memberRepository.count()
        assertThat(deletedCount).isEqualTo(0)
    }

    @Test
    fun findByUsernameAndAgeGreaterThan() {
        val m1 = Member(username = "AAA", age = 10)
        val m2 = Member(username = "AAA", age = 20)
        memberRepository.save(m1)
        memberRepository.save(m2)

        val result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15)
        assertThat(result[0].username).isEqualTo("AAA");
        assertThat(result[0].age).isEqualTo(20);
        assertThat(result.size).isEqualTo(1);
    }

    @Test
    fun testNamedQuery() {
        val m1 = Member(username = "AAA", age = 10)
        val m2 = Member(username = "AAA", age = 20)
        memberRepository.save(m1)
        memberRepository.save(m2)

        val result: MutableList<Member> = memberRepository.findByUsername("AAA")
        val findMember = result[0]
        assertThat(findMember).isEqualTo(m1);
    }

    @Test
    fun testQuery() {
        val m1 = Member(username = "AAA", age = 10)
        val m2 = Member(username = "AAA", age = 20)
        memberRepository.save(m1)
        memberRepository.save(m2)

        val result: MutableList<Member> = memberRepository.findUser("AAA", 10)
        val findMember = result[0]
        assertThat(findMember).isEqualTo(m1);
    }
}
