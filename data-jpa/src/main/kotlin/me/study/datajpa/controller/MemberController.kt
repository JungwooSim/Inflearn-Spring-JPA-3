package me.study.datajpa.controller

import me.study.datajpa.entity.Member
import me.study.datajpa.repository.MemberRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import javax.annotation.PostConstruct

@RestController
class MemberController(@Autowired private val memberRepository: MemberRepository) {

    @GetMapping("/members/{id}")
    fun findMember(@PathVariable("id") member: Member): String {
        return member.username
    }

    @PostConstruct
    fun init() {
        val member = Member(username = "nameA")
        memberRepository.save(member)
    }
}
