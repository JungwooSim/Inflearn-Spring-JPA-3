package me.study.datajpa.controller

import me.study.datajpa.dto.MemberDto
import me.study.datajpa.entity.Member
import me.study.datajpa.repository.MemberRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
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

    // Ex. localhost:8080/members?page=0&size=10&sort=id,desc&sort=username,asc
    @GetMapping("/members")
    fun list(pageable: Pageable): Page<Member> {
        return memberRepository.findAll(pageable)
    }

    @GetMapping("/members2")
    fun list2(@PageableDefault(size = 12, sort = ["username"], direction = Sort.Direction.DESC) pageable: Pageable): Page<Member> {
        return memberRepository.findAll(pageable)
    }

    @GetMapping("/members3")
    fun list3(@PageableDefault(size = 12, sort = ["username"], direction = Sort.Direction.DESC) pageable: Pageable): Page<MemberDto> {
        val page = memberRepository.findAll(pageable)
        return page.map { MemberDto(id = it.id!!, username = it.username,teamName = "" ) }
    }

//    @PostConstruct
    fun init() {
        for (i in 1..100) {
            val username = "name$i"
            memberRepository.save(Member(username = username))
        }
    }
}
