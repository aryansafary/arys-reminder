package com.arysapp.task.ui.screen
import android.util.Log
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.arysapp.task.ui.components.ArysOutlinedTextField
import com.arysapp.task.R
import com.arysapp.task.domain.model.RepeatType
import com.arysapp.task.ui.components.MyDatePicker
import com.arysapp.task.ui.components.MyTimePicker
import com.arysapp.task.ui.viewmodel.TaskViewModel
import com.arysapp.task.utils.Constants.USER_LANGUAGE
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
var time by remember { mutableStateOf("") }
    USER_LANGUAGE = "fa"
var date by remember {
        mutableStateOf(
            if (USER_LANGUAGE == "fa")
                "${JalaliDate.today().year}-" +
                "${JalaliDate.today().month.toString().padStart(2, '0')}-" +
                "${JalaliDate.today().day.toString().padStart(2, '0')} "
             else
                "${Calendar.getInstance().get(Calendar.YEAR)}-" +
                "${(Calendar.getInstance().get(Calendar.MONTH) + 1).toString().padStart(2, '0')}-" +
                "${Calendar.getInstance().get(Calendar.DAY_OF_MONTH).toString().padStart(2, '0')} "
        )
    }

    Column (
    modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState()),
    ){
    ArysOutlinedTextField(
        value = title,
        modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
        onValueChange = { title = it },
        label = LocalContext.current.getString(R.string.title),
        leadingIcon = {
            Icon(painter = painterResource(R.drawable.title_100px_1),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.height(24.dp)
            )
                      },
    )
    Spacer(modifier = Modifier.height(8.dp))
    ArysOutlinedTextField(
        value = description,
        modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
        onValueChange = { description = it },
        label = LocalContext.current.getString(R.string.description),
        leadingIcon = {
            Icon(painter = painterResource(R.drawable.description_100px_2),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.height(24.dp)
            )
                      },
    )
    Spacer(modifier = Modifier.height(12.dp))
    SectionHeader(painterResource(R.drawable.time_icon), LocalContext.current.getString(R.string.select_Time))
//    InfinityWheelTimePicker (
//        selectedDate = date,
//        userLanguage = USER_LANGUAGE
//    ){ Log.d("TAG", "AddTaskScreen: $it")   }
        MyTimePicker(
            selectedDate = date,
            userLanguage = USER_LANGUAGE){
            time = it
            Log.d("TAG", "AddTaskScreen: $it")
        }
    Spacer(modifier = Modifier.height(12.dp))
    SectionHeader(painterResource(R.drawable.calendar_icon), LocalContext.current.getString(R.string.select_DateTime))
        MyDatePicker (
            userLanguage = USER_LANGUAGE,
            selectedDate = date,
        ){dateSelected ->
            Log.d("DateNew", "AddTaskScreen: $dateSelected")
            date = dateSelected }
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
        onClick = {
            val (days, weeks, months) = when(repeatType) {
                RepeatType.DAILY.name -> Triple(1, null, null)
                RepeatType.WEEKLY.name -> Triple(null, 1, null)
                RepeatType.MONTHLY.name -> Triple(null, null, 1)
                else -> Triple(null, null, null)
            }
            taskViewModel.insertTask(
                title = title,
                description = description,
                dateTime = date,
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






