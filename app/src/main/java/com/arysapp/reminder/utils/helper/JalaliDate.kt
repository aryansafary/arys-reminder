package com.arysapp.reminder.utils.helper
import java.util.Calendar

data class JalaliDate(
    var year: Int = 0,
    var month: Int = 0,
    var day: Int = 0
) {
    companion object {
        internal val monthNames = arrayOf(
            "فروردین", "اردیبهشت", "خرداد",
            "تیر", "مرداد", "شهریور",
            "مهر", "آبان", "آذر",
            "دی", "بهمن", "اسفند"
        )

//        internal val dayNames = arrayOf(
//            "شنبه", "یکشنبه", "دوشنبه",
//            "سه‌شنبه", "چهارشنبه", "پنجشنبه", "جمعه"
//        )

        fun today(): JalaliDate {
            val calendar = Calendar.getInstance()
            return fromGregorian(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH)
            )
        }
        fun getTodayDateString(isPersian: Boolean): String {
            return if (isPersian) {
                val jDate = today()
                "${jDate.year}-${jDate.month.toString().padStart(2, '0')}-${jDate.day.toString().padStart(2, '0')}"
            } else {
                val cal = Calendar.getInstance()
                "${cal.get(Calendar.YEAR)}-${(cal.get(Calendar.MONTH) + 1).toString().padStart(2, '0')}-${cal.get(Calendar.DAY_OF_MONTH).toString().padStart(2, '0')}"
            }
        }

        fun fromGregorian(gy: Int, gm: Int, gd: Int): JalaliDate {
            val gDM = intArrayOf(0, 31, if (isLeapGregorian(gy)) 29 else 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
            val gy = gy
            val gm = gm
            val gd = gd

            var jy: Int
            var jm: Int
            var jd: Int

            val gy2 = gy - 1600
            val gm2 = gm - 1
            val gd2 = gd - 1

            var gDayNo = 365 * gy2 + (gy2 + 3) / 4 - (gy2 + 99) / 100 + (gy2 + 399) / 400
            for (i in 0 until gm2) gDayNo += gDM[i + 1]
            gDayNo += gd2

            var jDayNo = gDayNo - 79
            val jNp = jDayNo / 12053
            jDayNo %= 12053

            jy = 979 + 33 * jNp + 4 * (jDayNo / 1461)
            jDayNo %= 1461

            if (jDayNo >= 366) {
                jy += (jDayNo - 1) / 365
                jDayNo = (jDayNo - 1) % 365
            }

            val jDaysInMonth = intArrayOf(31, 31, 31, 31, 31, 31, 30, 30, 30, 30, 30, 29)
            var i = 0
            while (i < 12 && jDayNo >= jDaysInMonth[i]) {
                jDayNo -= jDaysInMonth[i]
                i++
            }

            jm = i + 1
            jd = jDayNo + 1

            return JalaliDate(jy, jm, jd)
        }

        private fun isLeapGregorian(year: Int): Boolean {
            return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
        }
    }

    init {
        if (year == 0) {
            val today = today()
            this.year = today.year
            this.month = today.month
            this.day = today.day
        }
    }

//    fun addDay(days: Int) {
//        val calendar = Calendar.getInstance()
//        val gDate = toGregorian()
//        calendar.set(gDate[0], gDate[1] - 1, gDate[2])
//        calendar.add(Calendar.DAY_OF_MONTH, days)
//        val newDate = fromGregorian(
//            calendar.get(Calendar.YEAR),
//            calendar.get(Calendar.MONTH) + 1,
//            calendar.get(Calendar.DAY_OF_MONTH)
//        )
//        this.year = newDate.year
//        this.month = newDate.month
//        this.day = newDate.day
//    }

    fun getDayOfWeek(): Int {
        val calendar = Calendar.getInstance()
        val g = toGregorian()
        calendar.set(g[0], g[1] - 1, g[2])
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        return when (dayOfWeek) {
            Calendar.SATURDAY -> 0
            Calendar.SUNDAY -> 1
            Calendar.MONDAY -> 2
            Calendar.TUESDAY -> 3
            Calendar.WEDNESDAY -> 4
            Calendar.THURSDAY -> 5
            Calendar.FRIDAY -> 6
            else -> 0
        }
    }



    fun toGregorian(): IntArray {
        val jDaysInMonth = intArrayOf(31, 31, 31, 31, 31, 31, 30, 30, 30, 30, 30, 29)
        val jy = year - 979
        val jm = month - 1
        val jd = day - 1

        var jDayNo = 365 * jy + jy / 33 * 8 + (jy % 33 + 3) / 4
        for (i in 0 until jm) jDayNo += jDaysInMonth[i]
        jDayNo += jd

        var gDayNo = jDayNo + 79

        var gy = 1600 + 400 * (gDayNo / 146097)
        gDayNo %= 146097

        var leap = true
        if (gDayNo >= 36525) {
            gDayNo--
            gy += 100 * (gDayNo / 36524)
            gDayNo %= 36524

            if (gDayNo >= 365) gDayNo++ else leap = false
        }

        gy += 4 * (gDayNo / 1461)
        gDayNo %= 1461

        if (gDayNo >= 366) {
            leap = false
            gDayNo--
            gy += gDayNo / 365
            gDayNo %= 365
        }

        val gDaysInMonth = intArrayOf(31, if (leap) 29 else 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
        var gm = 0
        while (gDayNo >= gDaysInMonth[gm]) {
            gDayNo -= gDaysInMonth[gm]
            gm++
        }
        val gd = gDayNo + 1

        return intArrayOf(gy, gm + 1, gd)
    }

    override fun toString(): String = "$year/${month.toString().padStart(2, '0')}/${day.toString().padStart(2, '0')}"




    fun getDaysInJalaliMonth(year: Int, month: Int): Int {
        return when (month) {
            in 1..6 -> 31
            in 7..11 -> 30
            12 -> if (isJalaliLeapYear(year)) 30 else 29
            else -> 0
        }
    }

    fun isJalaliLeapYear(year: Int): Boolean {
        val a = year - (year > 0).compareTo(false) * 474
        val b = a % 2820 + 474
        return ((b * 682) % 2816) < 682
    }



    operator fun compareTo(other: JalaliDate): Int {
        return when {
            year != other.year -> year - other.year
            month != other.month -> month - other.month
            else -> day - other.day
        }
    }









}