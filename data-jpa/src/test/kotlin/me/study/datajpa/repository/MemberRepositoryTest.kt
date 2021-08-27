package me.study.datajpa.repository

import me.study.datajpa.dto.MemberDto
import me.study.datajpa.dto.UsernameOnlyDto
import me.study.datajpa.entity.Member
import me.study.datajpa.entity.Team
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.annotation.Rollback
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@SpringBootTest
@Transactional
@Rollback(false)
internal class MemberRepositoryTest(
    @Autowired private val memberRepository: MemberRepository,
    @Autowired private val teamRepository: TeamRepository,
    @Autowired private val em: EntityManager) {

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

    @Test
    fun findUsernameList() {
        val m1 = Member(username = "AAA", age = 10)
        val m2 = Member(username = "BBB", age = 20)
        memberRepository.save(m1)
        memberRepository.save(m2)

        val usernameList: MutableList<String> = memberRepository.findUsernameList()

        usernameList.forEach { println(it) }
    }

    @Test
    fun findMemberDto() {
        val team = Team(name = "teamA")
        teamRepository.save(team)

        val m1 = Member(username = "AAA", age = 10, team = team)
        memberRepository.save(m1)

        val memberDto: MutableList<MemberDto> = memberRepository.findMemberDto()

        memberDto.forEach { println(it) }
    }

    @Test
    fun findByNames() {
        val m1 = Member(username = "AAA", age = 10)
        val m2 = Member(username = "BBB", age = 20)
        memberRepository.save(m1)
        memberRepository.save(m2)

        val result: MutableList<Member> = memberRepository.findByNames(mutableListOf("AAA", "BBB"))
        result.forEach { println(it.username) }
    }

    @Test
    fun paging() {
        //given
        memberRepository.save(Member(username = "member1", age = 10))
        memberRepository.save(Member(username = "member2", age = 10))
        memberRepository.save(Member(username = "member3", age = 10))
        memberRepository.save(Member(username = "member4", age = 10))
        memberRepository.save(Member(username = "member5", age = 10))

        val pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"))
        val age = 10

        //when
        val page: Page<Member> = memberRepository.findByAge(age, pageRequest)
        val tmMap: Page<MemberDto> = page.map { member -> MemberDto(member.id!!, member.username, "") } // dto 로 쉽게 변환 가능

        //then
        val content: MutableList<Member> = page.content
        val totalElements = page.totalElements

        assertThat(content.size).isEqualTo(3)
        assertThat(page.totalElements).isEqualTo(5)
        assertThat(page.number).isEqualTo(0)
        assertThat(page.totalPages).isEqualTo(2)
        assertThat(page.isFirst).isTrue()
        assertThat(page.hasNext()).isTrue()
    }

    @Test
    fun bulkUpdate() {
        //given
        memberRepository.save(Member(username = "member1", age = 10))
        memberRepository.save(Member(username = "member2", age = 19))
        memberRepository.save(Member(username = "member3", age = 20))
        memberRepository.save(Member(username = "member4", age = 21))
        memberRepository.save(Member(username = "member5", age = 40))

        //when
        val resultCount = memberRepository.bulkAgePlus(20)

        //then
        assertThat(resultCount).isEqualTo(3)
    }

    @Test
    fun findMemberLazy() {
        //given
        val teamA = Team(name = "teamA")
        val teamB = Team(name = "teamB")
        teamRepository.save(teamA)
        teamRepository.save(teamB)
        memberRepository.save(Member(username = "member1", age = 10, team = teamA))
        memberRepository.save(Member(username = "member2", age = 20, team = teamB))

        em.flush()
        em.clear()

        //when
        // 3가지 모두 가능
//        val members: MutableList<Member> = memberRepository.findAll()
//        val members: MutableList<Member> = memberRepository.findMemberFetchJoin()
        val members: MutableList<Member> = memberRepository.findMemberEntityGraph()

        //then
        members.forEach { println(it.team!!.name) }
    }

    @Test
    fun queryHint() {
        //given
        memberRepository.save(Member(username = "member1", age = 10))
        em.flush()
        em.clear()

        //when
        val member: Member = memberRepository.findReadOnlyByUsername("member1")
        member.username = "member2"

        em.flush() // update query 실행 하지 않음
    }

    @Test
    fun callCustom() {
        memberRepository.findMemberCustom()
    }

    @Test
    fun jpaEventBaseEntity() {
        //given
        val member = Member(username = "member1")
        memberRepository.save(member)

        Thread.sleep(100)
        member.username = "member2"

        em.flush()
        em.clear()

        //when
        val findMember = memberRepository.findById(member.id!!).get()

        //then
        println("findMember.createdDate = " + findMember.createdDate)
        println("findMember.updatedDate = " + findMember.lastModifiedDate)
        println("findMember.createdBy = " + findMember.createBy)
        println("findMember.lastModifiedBy = " + findMember.lastModifiedBy)
    }

    @Test
    fun projections() {
        //given
        memberRepository.save(Member(username = "member1", age = 10))
        memberRepository.save(Member(username = "member2", age = 10))
        em.flush()
        em.clear()

        //when
//        val result: MutableList<UsernameOnly> = memberRepository.findProjectonsByUsername("member1")
//        val result: MutableList<UsernameOnly> = memberRepository.findProjectonsByUsername("member1")

        //then
//        result.forEach { println(it.getUsername()) }
    }

    @Test
    fun projections2() {
        //given
        memberRepository.save(Member(username = "member1", age = 10))
        memberRepository.save(Member(username = "member2", age = 10))
        em.flush()
        em.clear()

        //when
//        val result: MutableList<UsernameOnly> = memberRepository.findProjectonsByUsername("member1")
        val result: MutableList<UsernameOnlyDto> = memberRepository.findProjectonsByUsername("member1")

        //then
        result.forEach { println(it.username) }
    }
}
