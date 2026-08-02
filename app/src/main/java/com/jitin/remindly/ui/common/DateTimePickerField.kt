package com.jitin.remindly.ui.common

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun DateTimePickerField(
    label: String,
    value: LocalDateTime?,
    onValueChange: (LocalDateTime?) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val formatter = remember { DateTimeFormatter.ofPattern("EEE, MMM d yyyy · h:mm a") }

    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        OutlinedButton(
            onClick = {
                val base = value ?: LocalDateTime.now().plusHours(1).withMinute(0).withSecond(0)
                DatePickerDialog(
                    context,
                    { _, year, month, day ->
                        TimePickerDialog(
                            context,
                            { _, hour, minute ->
                                onValueChange(LocalDateTime.of(year, month + 1, day, hour, minute))
                            },
                            base.hour, base.minute, false
                        ).show()
                    },
                    base.year, base.monthValue - 1, base.dayOfMonth
                ).show()
            },
            modifier = Modifier.weight(1f)
        ) {
            Text(if (value != null) "$label: ${value.format(formatter)}" else "$label: Not set")
        }
        if (value != null) {
            IconButton(onClick = { onValueChange(null) }, modifier = Modifier.padding(start = 4.dp)) {
                Icon(Icons.Filled.Close, contentDescription = "Clear $label")
            }
        }
    }
}
