package me.study.datajpa.repository

import org.springframework.beans.factory.annotation.Value

interface UsernameOnly {
//    fun getUsername(): String

    @Value("#{target.username + ' ' +target.age }")
    fun getUsername(): String
}
