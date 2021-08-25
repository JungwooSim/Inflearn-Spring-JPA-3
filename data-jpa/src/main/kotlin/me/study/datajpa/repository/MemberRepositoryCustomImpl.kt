package me.study.datajpa.repository

import me.study.datajpa.entity.Member
import org.springframework.beans.factory.annotation.Autowired
import javax.persistence.EntityManager

class MemberRepositoryCustomImpl(@Autowired private val em: EntityManager) : MemberRepositoryCustom {

    override fun findMemberCustom(): MutableList<Member> {
        return em.createQuery("select m from Member m").resultList as MutableList<Member>
    }
}
