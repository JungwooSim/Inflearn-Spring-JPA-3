package me.study.datajpa.repository

import me.study.datajpa.entity.Team
import org.springframework.stereotype.Repository
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
class TeamJpaRepository(@PersistenceContext private val em: EntityManager) {

    fun save(team: Team): Team {
        em.persist(team)
        return team
    }

    fun delete(team: Team) {
        em.remove(team)
    }

    fun findAll(): MutableList<Team> {
        return em.createQuery("select t from Team t", Team::class.java).resultList
    }

    fun findById(id: Long): Optional<Team> {
        val team = em.find(Team::class.java, id)
        return Optional.ofNullable(team)
    }

    fun count(): Long {
        return em.createQuery("select count(t) from Team t", Long::class.javaObjectType).singleResult
    }

    fun find(id: Long): Team {
        return em.find(Team::class.java, id)
    }
}
