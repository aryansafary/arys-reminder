package com.arysapp.task.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.arysapp.task.R
import androidx.compose.ui.text.font.Font

val fontBold = FontFamily(
    Font(R.font.vazirmatn_bold)
)
val fontMedium = FontFamily(
    Font(R.font.vazirmatn_medium)
)
val fontRegular = FontFamily(
    Font(R.font.vazirmatn_regular)
)
fun String.toPersianDigits(): String {
    val englishDigits = arrayOf('0','1','2','3','4','5','6','7','8','9')
    val persianDigits = arrayOf('۰','۱','۲','۳','۴','۵','۶','۷','۸','۹')
    var result = this
    for (i in englishDigits.indices) {
        result = result.replace(englishDigits[i], persianDigits[i])
    }
    return result
}

fun String.toEnglishDigits(): String {
    val englishDigits = arrayOf('0','1','2','3','4','5','6','7','8','9')
    val persianDigits = arrayOf('۰','۱','۲','۳','۴','۵','۶','۷','۸','۹')
    var result = this
    for (i in persianDigits.indices) {
        result = result.replace(persianDigits[i], englishDigits[i])
    }
    return result
}

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = fontBold,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = fontMedium,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodySmall = TextStyle(
        fontFamily = fontRegular,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = fontRegular,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    labelMedium = TextStyle(
        fontFamily = fontMedium,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    labelLarge = TextStyle(
        fontFamily = fontBold,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = fontBold,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = fontMedium,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = fontRegular,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = fontBold,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleMedium = TextStyle(
        fontFamily = fontMedium,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleSmall = TextStyle(
        fontFamily = fontRegular,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),







)