package com.arysapp.task.ui.screen
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.arysapp.task.ui.components.ArysOutlinedTextField
import com.arysapp.task.R
import com.arysapp.task.domain.model.RepeatType
import com.arysapp.task.ui.components.InfiniteWheelJalaliDatePicker
import com.arysapp.task.ui.components.InfiniteWheelTimePicker
import com.arysapp.task.ui.viewmodel.TaskViewModel
import com.arysapp.task.utils.helper.JalaliDate
import java.util.Calendar


@Composable
fun AddTaskScreen(
    taskViewModel: TaskViewModel = hiltViewModel(),
    navController: NavController
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var repeatType by remember { mutableStateOf(RepeatType.NONE.name) }

    val now = remember { Calendar.getInstance() }
    val currentHour = now.get(Calendar.HOUR_OF_DAY)
    val currentMinute = now.get(Calendar.MINUTE)
    val todayJalali = remember { JalaliDate.today() }

    var selectedDate by remember { mutableStateOf(todayJalali) }
    var selectedHour by remember { mutableStateOf(currentHour) }
    var selectedMinute by remember { mutableStateOf(currentMinute) }

    // این فلگ نشان میدهد که تاریخ به روز بعد رفته است یا خیر
    var dayIncrementFlag by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        ArysOutlinedTextField(
            value = title,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onValueChange = { title = it },
            label = LocalContext.current.getString(R.string.title),
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.title_100px_1),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.height(24.dp)
                )
            },
        )

        Spacer(modifier = Modifier.height(8.dp))

        ArysOutlinedTextField(
            value = description,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onValueChange = { description = it },
            label = LocalContext.current.getString(R.string.description),
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.description_100px_2),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.height(24.dp)
                )
            },
        )

        Spacer(modifier = Modifier.height(12.dp))

        SectionHeader(
            painterResource(R.drawable.time_icon),
            LocalContext.current.getString(R.string.select_Time)
        )

        // وقتی تاریخ انتخاب شده امروز است محدودیت ساعت و دقیقه براساس زمان جاری است
        // در غیر اینصورت ساعت 0 و دقیقه 0 حداقل است (برای روزهای بعد)
        val minHour = if (selectedDate == todayJalali) currentHour else 0
        val minMinute = if (selectedDate == todayJalali && selectedHour == currentHour) currentMinute else 0

        InfiniteWheelTimePicker(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            initialHour = selectedHour,
            initialMinute = selectedMinute,
            minHour = minHour,
            minMinute = minMinute,
            onTimeSelected = { hour, minute, dayIncrement ->
                selectedHour = hour
                selectedMinute = minute

                // اگر زمان انتخابی از زمان جاری در تاریخ امروز کمتر بود، تاریخ را به روز بعد ببرید
                dayIncrementFlag = dayIncrement

                if (dayIncrementFlag) {
                    selectedDate = selectedDate.apply { addDay(1) }
                }

                // اگر تاریخ الان بزرگ‌تر از امروز است، فلگ را صفر کن
                if (selectedDate > todayJalali) {
                    dayIncrementFlag = false
                }
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        SectionHeader(
            painterResource(R.drawable.calendar_icon),
            LocalContext.current.getString(R.string.select_DateTime)
        )

        InfiniteWheelJalaliDatePicker(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            initialDate = selectedDate,
            minDate = todayJalali,
            onDateSelected = { date ->
                // وقتی تاریخ بزرگ‌تر از امروز است اجازه انتخاب هر ساعتی را بده
                // اگر تاریخ انتخابی امروز است دقیقه و ساعت حداقل محدود شود
                selectedDate = date

                // بروزرسانی minHour و minMinute باید در Compose مجدداً اعمال شود
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        SectionHeader(
            painterResource(R.drawable.repeat_100px_1),
            LocalContext.current.getString(R.string.select_repeatType)
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            RepeatType.entries.forEach { type ->
                AssistChip(
                    onClick = { repeatType = type.name },
                    label = {
                        Text(
                            text = type.getLabel(),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = if (repeatType == type.name) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surface,
                        labelColor = if (repeatType == type.name) MaterialTheme.colorScheme.onPrimary
                        else MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                val dateString = "${selectedDate.year}-${selectedDate.month.toString().padStart(2, '0')}-${selectedDate.day.toString().padStart(2, '0')}"
                val timeString = "${selectedHour.toString().padStart(2, '0')}:${selectedMinute.toString().padStart(2, '0')}"

                val (days, weeks, months) = when (repeatType) {
                    RepeatType.DAILY.name -> Triple(1, null, null)
                    RepeatType.WEEKLY.name -> Triple(null, 1, null)
                    RepeatType.MONTHLY.name -> Triple(null, null, 1)
                    else -> Triple(null, null, null)
                }
                taskViewModel.insertTask(
                    title = title,
                    description = description,
                    dateTime = dateString,
                    hourTime = timeString,
                    repeatType = repeatType,
                    repeatIntervalDays = days,
                    repeatIntervalWeeks = weeks,
                    repeatIntervalMonths = months
                )
                navController.popBackStack()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
        ) {
            Text("ذخیره فعالیت")
        }
    }
}


@Composable
fun SectionHeader(icon: Painter, title: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.height(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold
        )
    }
}







