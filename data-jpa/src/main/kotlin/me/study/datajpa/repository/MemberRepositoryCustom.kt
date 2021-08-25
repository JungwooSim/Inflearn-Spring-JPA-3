package me.study.datajpa.repository

import me.study.datajpa.entity.Member

interface MemberRepositoryCustom {

    fun findMemberCustom(): MutableList<Member>
}
