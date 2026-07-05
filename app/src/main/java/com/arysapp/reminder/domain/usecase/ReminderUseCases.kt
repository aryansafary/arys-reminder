package com.arysapp.reminder.domain.usecase

import javax.inject.Inject

data class ReminderUseCases @Inject constructor(
    val insertReminder: InsertReminderUseCase,
    val deleteReminder: DeleteReminderUseCase,
    val updateReminderStatus: UpdateReminderStatusUseCase,
    val updateReminder: UpdateReminderUseCase,
    val getAllReminders: GetAllRemindersUseCase,
    val updateReminderDateTime:UpdateReminderDateTimeUseCase,
    val backupReminders: BackupReminderUseCase,
    val restoreReminders: RestoreRemindersUseCase,
    val processExpiredReminders: ProcessExpiredRemindersUseCase
)
