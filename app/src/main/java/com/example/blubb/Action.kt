package com.example.blubb

import java.time.LocalTime

data class Action(
    val trigger: LocalTime,
    val action: Unit,
    val active: Boolean
)
//
//fun load ( ) {
//    File("actions.txt").forEachLine {
//        val (trigger, action, active) = it.split(" ")
//        Action(trigger, action, active)
//    }
//}