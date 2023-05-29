package com.example.blubb

import java.time.LocalTime

data class Action(
    val name: String,
    val trigger: LocalTime,
    val effect: Unit,
    val active: Boolean
)
//
//fun load ( ) {
//    File("actions.txt").forEachLine {
//        val (trigger, action, active) = it.split(" ")
//        Action(trigger, action, active)
//    }
//}
