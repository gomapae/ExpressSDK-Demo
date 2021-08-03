package com.wendjia.base.utils

import java.util.*

/**
 * @description the date covert to ui resource utils
 * @author dong.jin@g42.ai
 * @createtime 2020/12/23
 */
object DateCovertUtil {

    /**
     * get the current year
     */
    fun getCurYear(): Int {
        val calendar: Calendar = Calendar.getInstance()
        calendar.time = Date(System.currentTimeMillis())
        return calendar.get(Calendar.YEAR)
    }
    fun getYear(date: Date): Int {
        val calendar: Calendar = Calendar.getInstance()
        calendar.time = date
        return calendar.get(Calendar.YEAR)
    }
    /**
     * get the current month
     */
    fun getCurMonth(): Int {
        val calendar: Calendar = Calendar.getInstance()
        calendar.time = Date(System.currentTimeMillis())
        return calendar.get(Calendar.MONTH) + 1
    }
    fun getMonth(date: Date): Int {
        val calendar: Calendar = Calendar.getInstance()
        calendar.time = date
        return calendar.get(Calendar.MONTH) + 1
    }
    /**
     * get the current day
     */
    fun getCurDay(): Int {
        val calendar: Calendar = Calendar.getInstance()
        calendar.time = Date(System.currentTimeMillis())
        return calendar.get(Calendar.DATE)
    }
    fun getDay(date: Date): Int {
        val calendar: Calendar = Calendar.getInstance()
        calendar.time = date
        return calendar.get(Calendar.DATE)
    }

    /**
     * get yesterday date
     */
    fun getYesterday(): Calendar {
        var curMonth = getCurMonth()
        var curYear = getCurYear()
        var curDay = getCurDay()

        var yesterdayYear = curYear
        var yesterdayMonth = curMonth
        var yesterday = 0

        if (curDay == 1) {
            if (curMonth == 1) {
                yesterdayYear = curYear - 1
                yesterdayMonth = 12
            } else {
                yesterdayMonth = curMonth - 1
            }
            yesterday = getMonthLastDay(yesterdayYear, yesterdayMonth)
        } else {
            yesterday = curDay - 1
        }
        return getCalendar(yesterdayYear, yesterdayMonth, yesterday)
    }

    fun getYesterdayAsDate(year:Int ,month:Int ,day:Int): Calendar {
        var yesterdayYear = year
        var yesterdayMonth = month
        var yesterday = 0

        if (day == 1) {
            if (month == 1) {
                yesterdayYear = year - 1
                yesterdayMonth = 12
            } else {
                yesterdayMonth = month - 1
            }
            yesterday = getMonthLastDay(yesterdayYear, yesterdayMonth)
        } else {
            yesterday = day - 1
        }
        return getCalendar(yesterdayYear, yesterdayMonth, yesterday)
    }
    /**
     * get today data
     */
    fun getToday(): Calendar {
        val calendar: Calendar = Calendar.getInstance()
        calendar.time = Date(System.currentTimeMillis())
        return calendar
    }

    /**
     * 比较日期是否为同一天
     */
    fun equals(calendar1: Calendar, calendar2: Calendar): Boolean {
        var year1 = getYear(calendar1)
        var month1 = getMonth(calendar1)
        var day1 = getDay(calendar1)

        var year2 = getYear(calendar2)
        var month2 = getMonth(calendar2)
        var day2 = getDay(calendar2)
        return year1 == year2 && month1 == month2 && day1 == day2
    }

    /**
     * get tomorrow date
     */
    fun getTomorrow(): Calendar {
        var curMonth = getCurMonth()
        var curYear = getCurYear()
        var curDay = getCurDay()
        var monthLastDay = getMonthLastDay(curYear, curMonth)

        var tomorrowYear = curYear
        var tomorrowMonth = curMonth
        var tomorrow = 0

        if (curDay == monthLastDay) {
            if (curMonth == 12) {
                tomorrowYear = curYear + 1
                tomorrowMonth = 1
            } else {
                tomorrowMonth = curMonth + 1
            }
            tomorrow = 1
        } else {
            tomorrow = curDay + 1
        }
        return getCalendar(tomorrowYear, tomorrowMonth, tomorrow)
    }
    /**
     * get tomorrow date
     */
    fun getTomorrowAsDate (year:Int ,month:Int ,day:Int): Calendar {
        var monthLastDay = getMonthLastDay(year, month)

        var tomorrowYear = year
        var tomorrowMonth = month
        var tomorrow = 0

        if (day == monthLastDay) {
            if (month == 12) {
                tomorrowYear = year + 1
                tomorrowMonth = 1
            } else {
                tomorrowMonth = month + 1
            }
            tomorrow = 1
        } else {
            tomorrow = day + 1
        }
        return getCalendar(tomorrowYear, tomorrowMonth, tomorrow)
    }

    /**
     * get the number of days a month
     */
    fun getMonthLastDay(year: Int, month: Int): Int {
        val calendar: Calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month - 1)
        calendar.set(Calendar.DATE, 1)
        calendar.roll(Calendar.DATE, -1)
        return calendar.get(Calendar.DATE)
    }

    /**
     * get the en from month
     */
    fun getEnMon(month: Int): String {
        var en = ""
        when (month) {
            1 -> en = "Jan"
            2 -> en = "Feb"
            3 -> en = "Mar"
            4 -> en = "Apr"
            5 -> en = "May"
            6 -> en = "Jun"
            7 -> en = "Jul"
            8 -> en = "Aug"
            9 -> en = "Sept"
            10 -> en = "Oct"
            11 -> en = "Nov"
            12 -> en = "Dec"
        }
        return en
    }

    /**
     * get the calendar by year/month/day
     */
    fun getCalendar(year: Int, month: Int, day: Int): Calendar {
        val calendar: Calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month - 1)
        calendar.set(Calendar.DATE, day)
        return calendar
    }

    /**
     * get the day from calendar
     */
    fun getDay(calendar: Calendar): Int {
        return calendar?.get(Calendar.DATE)
    }

    /**
     * get the month from calendar
     */
    fun getMonth(calendar: Calendar): Int {
        return calendar?.get(Calendar.MONTH) + 1
    }

    /**
     * get the day from calendar
     */
    fun getYear(calendar: Calendar): Int {
        return calendar.get(Calendar.YEAR)
    }
    fun getHour(time:Long): Int {
        val calendar: Calendar = Calendar.getInstance()
        calendar.time = Date(time)
        return calendar.get(Calendar.HOUR_OF_DAY)
    }
    fun getMinute(time:Long): Int {
        val calendar: Calendar = Calendar.getInstance()
        calendar.time = Date(time)
        return calendar.get(Calendar.MINUTE)
    }
    fun getSecond(time:Long): Int {
        val calendar: Calendar = Calendar.getInstance()
        calendar.time = Date(time)
        return calendar.get(Calendar.SECOND)
    }
    fun getHour(calendar: Calendar): Int {
        return calendar.get(Calendar.HOUR_OF_DAY)
    }
    fun getMinute(calendar: Calendar): Int {
        return calendar.get(Calendar.MINUTE)
    }
    fun getSecond(calendar: Calendar): Int {
        return calendar.get(Calendar.SECOND)
    }
}