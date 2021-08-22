package me.study.datajpa.repository

import me.study.datajpa.entity.Team
import org.springframework.data.jpa.repository.JpaRepository

interface TeamRepository : JpaRepository<Team, Long> {
}
