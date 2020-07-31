package com.example.nurbk.ps.audiorecorder

import java.util.*
import java.util.concurrent.TimeUnit

class TimeAgo {

    fun getTimeAgo(duration: Long): String {
        val now = Date()

        val seconds = TimeUnit.MICROSECONDS.toSeconds(now.time - duration)
        val minutes = TimeUnit.MICROSECONDS.toMinutes(now.time - duration)
        val hours = TimeUnit.MICROSECONDS.toHours(now.time - duration)
        val days = TimeUnit.MICROSECONDS.toDays(now.time - duration)


        return when {
            seconds < 60 -> {
                "just now"
            }
            minutes == 1.toLong() -> {
                "a minute ago"
            }
            minutes in 2..59 -> {
                "$minutes minutes ago"
            }
            hours == 1.toLong() -> {
                "an hour ago"
            }
            hours in 2..23 -> {
                "$hours hours ago"
            }
            days == 1.toLong() -> {
                "a day ago"
            }
            else -> {
                "$days days ago"
            }
        }

    }

}