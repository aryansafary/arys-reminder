package com.arysapp.reminder.ui.screen
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.arysapp.reminder.ui.components.MyOutlinedTextField
import com.arysapp.reminder.R
import com.arysapp.reminder.domain.model.RepeatType
import com.arysapp.reminder.domain.model.ReminderModel
import com.arysapp.reminder.ui.components.MyDatePicker
import com.arysapp.reminder.ui.components.MyTimePicker
import com.arysapp.reminder.ui.theme.parseToGregorianDate
import com.arysapp.reminder.ui.viewmodel.ReminderViewModel
import com.arysapp.reminder.utils.Constants.PERSIAN_LANGUAGE
import com.arysapp.reminder.utils.Constants.USER_LANGUAGE
import com.arysapp.reminder.utils.helper.JalaliDate
import java.util.Calendar



@Composable
fun AddReminderScreen(
    reminderViewModel: ReminderViewModel = hiltViewModel(),
    navController: NavController,
    reminderModel: ReminderModel? = null
) {
var title by remember { mutableStateOf(reminderModel?.title?:"") }
var description by remember { mutableStateOf(reminderModel?.description?:"") }
var repeatType by remember { mutableStateOf(reminderModel?.repeatType?:RepeatType.NONE.name) }
var time by remember { mutableStateOf("") }
var date by remember {
        mutableStateOf(
            if (USER_LANGUAGE == PERSIAN_LANGUAGE)
                "${JalaliDate.today().year}-" +
                "${JalaliDate.today().month.toString().padStart(2, '0')}-" +
                "${JalaliDate.today().day.toString().padStart(2, '0')} "
             else
                "${Calendar.getInstance().get(Calendar.YEAR)}-" +
                "${(Calendar.getInstance().get(Calendar.MONTH) + 1).toString().padStart(2, '0')}-" +
                "${Calendar.getInstance().get(Calendar.DAY_OF_MONTH).toString().padStart(2, '0')} "
        )
    }
val isEditMode = reminderModel != null
    Column (
    modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState()),
    ){
    MyOutlinedTextField(
        value = title,
        modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
        onValueChange = { title = it },
        label = LocalContext.current.getString(R.string.title),
        maxLength = 20,
        leadingIcon = {
            Icon(painter = painterResource(R.drawable.title_100px_1),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.height(24.dp),
                )
                      },
    )
    Spacer(modifier = Modifier.height(8.dp))
    MyOutlinedTextField(
        value = description,
        modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
        onValueChange = { description = it },
        label = LocalContext.current.getString(R.string.description),
        maxLength = 20,
        leadingIcon = {
            Icon(painter = painterResource(R.drawable.description_100px_2),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.height(24.dp)
            )
                      },
    )
    Spacer(modifier = Modifier.height(12.dp))
    SectionHeader(painterResource(R.drawable.calendar_icon), LocalContext.current.getString(R.string.select_DateTime))
        MyDatePicker (
            selectedDate = date,
        ){
            date = it
        }
        Spacer(modifier = Modifier.height(12.dp))
    SectionHeader(painterResource(R.drawable.time_icon), LocalContext.current.getString(R.string.select_Time))
        MyTimePicker(
            selectedDate = date
        ){
            time = it
        }

    Spacer(modifier = Modifier.height(12.dp))
    SectionHeader(painterResource(R.drawable.repeat_100px_1), LocalContext.current.getString(R.string.select_repeatType))
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        RepeatType.entries.forEach { type ->
            AssistChip(
                onClick = {repeatType = type.name  },
                label = {
                    Text(text = type.getLabel(),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                        )
                        },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = if (repeatType == type.name) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.surface
                    ,
                    labelColor = if (repeatType == type.name) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onSurface

                )
            )
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
    Button(
        enabled = !title.isEmpty() && !date.isEmpty() && !time.isEmpty() && !repeatType.isEmpty(),
        onClick = {
            val dateTime = if(USER_LANGUAGE==PERSIAN_LANGUAGE)date.parseToGregorianDate() else date
            val (days, weeks, months) = when(repeatType) {
                RepeatType.DAILY.name -> Triple(1, null, null)
                RepeatType.WEEKLY.name -> Triple(null, 1, null)
                RepeatType.MONTHLY.name -> Triple(null, null, 1)
                else -> Triple(null, null, null)
            }
            if(isEditMode)
                reminderViewModel.updateReminder(reminderModel.copy(
                    title = title,
                    description = description,
                    dateTime = dateTime,
                    hourTime = time,
                    repeatType = repeatType,
                    repeatIntervalDays = days,
                    repeatIntervalWeeks = weeks,
                    repeatIntervalMonths = months,
                    isActive = true
                )) else
            reminderViewModel.insertReminder(
                title = title,
                description = description,
                dateTime = dateTime,
                hourTime = time,
                repeatType = repeatType,
                repeatIntervalDays = days,
                repeatIntervalWeeks = weeks,
                repeatIntervalMonths = months
            )
            navController.popBackStack()
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
           ,
        shape = RoundedCornerShape(12.dp),
    ) {
        Text(text = stringResource(if(isEditMode) R.string.Edit_task else R.string.save_reminder))
    }
}
}

@Composable
fun SectionHeader(icon: Painter, title: String) {
    Row(
        modifier = Modifier.padding(start = 16.dp),
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
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}






