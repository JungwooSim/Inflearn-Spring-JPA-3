package me.study.datajpa.entity

import javax.persistence.*

@Entity
@NamedQuery(
    name="Member.findByUsername",
    query="select m from Member m where m.username = :username"
)
class Member (
    @Id @GeneratedValue
    val id: Long? = null,
    var username: String = "",
    val age: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    var team: Team? = null
) {
    fun changeTeam(team: Team) {
        this.team = team
        team.members.add(this)
    }
}
