package com.example.pertemuan5_tugas

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

// Data biodata yang dikirim dari form ke layar hasil
data class BiodataResult(
    val firstName: String,
    val lastName: String,
    val email: String,
    val birthDate: String,
    val experience: String,
    val skills: List<String>
)

// Pengatur layar: null = tampilkan form, ada isi = tampilkan hasil
@Composable
fun Act5Tugas() {
    var result by remember { mutableStateOf<BiodataResult?>(null) }

    val currentResult = result
    if (currentResult == null) {
        FormScreen(onProceed = { result = it })
    } else {
        ResultScreen(
            data = currentResult,
            onReset = { result = null }
        )
    }
}

// Kerangka bersama: Scaffold + gambar header + Box mengapung di atasnya
@Composable
private fun BiodataLayout(content: @Composable ColumnScope.() -> Unit) {
    Scaffold(
        containerColor = colorResource(R.color.screen_background)
    ) { innerPadding ->
        // Layer bawah: gambar header di dalam Scaffold
        Image(
            painter = painterResource(R.drawable.gothic_church),
            contentDescription = stringResource(R.string.cd_header_image),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.header_height))
        )

        // Layer atas: Box mengapung di atas Scaffold
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = dimensionResource(R.dimen.form_top_offset))
                .shadow(
                    elevation = dimensionResource(R.dimen.form_elevation),
                    shape = RoundedCornerShape(
                        topStart = dimensionResource(R.dimen.form_corner),
                        topEnd = dimensionResource(R.dimen.form_corner)
                    )
                )
                .padding(innerPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = colorResource(R.color.white),
                shape = RoundedCornerShape(
                    topStart = dimensionResource(R.dimen.form_corner),
                    topEnd = dimensionResource(R.dimen.form_corner)
                )
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(dimensionResource(R.dimen.form_padding)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    content = content
                )
            }
        }
    }
}

@Composable
fun FormScreen(onProceed: (BiodataResult) -> Unit) {
    val context = LocalContext.current
    val toastMessage = stringResource(R.string.toast_proceed)

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var experience by remember { mutableStateOf("") }
    val skills = remember { mutableStateListOf<String>() }

    val isFormValid = firstName.isNotBlank() && lastName.isNotBlank() &&
            email.isNotBlank() && birthDate.isNotBlank() &&
            experience.isNotBlank() && skills.isNotEmpty()

    BiodataLayout {
        Text(
            text = stringResource(R.string.form_title),
            color = colorResource(R.color.purple_title),
            fontSize = dimensionResource(R.dimen.text_title).value.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(dimensionResource(R.dimen.spacing_small)))
        Text(
            text = stringResource(R.string.form_subtitle),
            color = colorResource(R.color.text_subtitle),
            fontSize = dimensionResource(R.dimen.text_subtitle).value.sp,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(dimensionResource(R.dimen.spacing_large)))

        Row(horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small))) {
            FormField(
                value = firstName,
                onValueChange = { firstName = it },
                label = stringResource(R.string.first_name),
                modifier = Modifier.weight(dimensionResource(R.dimen.weight_equal).value)
            )
            FormField(
                value = lastName,
                onValueChange = { lastName = it },
                label = stringResource(R.string.last_name),
                modifier = Modifier.weight(dimensionResource(R.dimen.weight_equal).value)
            )
        }
        Spacer(Modifier.height(dimensionResource(R.dimen.spacing_medium)))

        FormField(
            value = email,
            onValueChange = { email = it },
            label = stringResource(R.string.email),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(dimensionResource(R.dimen.spacing_medium)))

        Row(horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small))) {
            DateField(
                value = birthDate,
                onDateSelected = { birthDate = it },
                modifier = Modifier.weight(dimensionResource(R.dimen.weight_equal).value)
            )
            ExperienceDropdown(
                value = experience,
                onSelected = { experience = it },
                modifier = Modifier.weight(dimensionResource(R.dimen.weight_equal).value)
            )
        }
        Spacer(Modifier.height(dimensionResource(R.dimen.spacing_medium)))

        Text(
            text = stringResource(R.string.choose_skill),
            color = colorResource(R.color.text_label),
            fontSize = dimensionResource(R.dimen.text_label).value.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf(
                stringResource(R.string.skill_android),
                stringResource(R.string.skill_uiux),
                stringResource(R.string.skill_web)
            ).forEach { skill ->
                SkillCheckbox(
                    label = skill,
                    checked = skill in skills,
                    onCheckedChange = { isChecked ->
                        if (isChecked) skills.add(skill) else skills.remove(skill)
                    }
                )
            }
        }
        Spacer(Modifier.height(dimensionResource(R.dimen.spacing_large)))

        Button(
            onClick = {
                Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
                onProceed(
                    BiodataResult(
                        firstName = firstName,
                        lastName = lastName,
                        email = email,
                        birthDate = birthDate,
                        experience = experience,
                        skills = skills.toList()
                    )
                )
            },
            enabled = isFormValid,
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.button_height)),
            shape = RoundedCornerShape(dimensionResource(R.dimen.button_corner)),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.button_enabled),
                contentColor = colorResource(R.color.white),
                disabledContainerColor = colorResource(R.color.button_disabled),
                disabledContentColor = colorResource(R.color.button_disabled_text)
            )
        ) {
            Text(
                text = stringResource(R.string.proceed),
                fontSize = dimensionResource(R.dimen.text_button).value.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun ResultScreen(
    data: BiodataResult,
    onReset: () -> Unit
) {
    val textSize = dimensionResource(R.dimen.text_label).value.sp
    val textColor = colorResource(R.color.text_label)

    BiodataLayout {
        Text(
            text = stringResource(R.string.result_title),
            color = colorResource(R.color.purple_title),
            fontSize = dimensionResource(R.dimen.text_title).value.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(dimensionResource(R.dimen.spacing_large)))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(dimensionResource(R.dimen.form_corner)))
                .background(colorResource(R.color.result_card_background))
                .padding(dimensionResource(R.dimen.form_padding)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_small))
        ) {
            Text(
                text = stringResource(R.string.result_full_name, data.firstName, data.lastName),
                color = textColor,
                fontSize = textSize,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(R.string.result_email, data.email),
                color = textColor,
                fontSize = textSize
            )
            Text(
                text = stringResource(R.string.result_birth_date, data.birthDate),
                color = textColor,
                fontSize = textSize
            )
            Text(
                text = stringResource(R.string.result_experience, data.experience),
                color = textColor,
                fontSize = textSize
            )
            Text(
                text = stringResource(R.string.result_skill, data.skills.joinToString(", ")),
                color = textColor,
                fontSize = textSize
            )
        }
        Spacer(Modifier.height(dimensionResource(R.dimen.spacing_large)))

        Button(
            onClick = onReset,
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.button_height)),
            shape = RoundedCornerShape(dimensionResource(R.dimen.button_corner)),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.button_enabled),
                contentColor = colorResource(R.color.white)
            )
        ) {
            Text(
                text = stringResource(R.string.result_reset),
                fontSize = dimensionResource(R.dimen.text_button).value.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun fieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = colorResource(R.color.purple_title),
    unfocusedBorderColor = colorResource(R.color.field_border),
    focusedTextColor = colorResource(R.color.field_text),
    unfocusedTextColor = colorResource(R.color.field_text),
    focusedLabelColor = colorResource(R.color.purple_title),
    unfocusedLabelColor = colorResource(R.color.field_text),
    disabledBorderColor = colorResource(R.color.field_border),
    disabledTextColor = colorResource(R.color.field_text),
    disabledLabelColor = colorResource(R.color.field_text),
    disabledTrailingIconColor = colorResource(R.color.field_text)
)

@Composable
fun FormField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, fontSize = dimensionResource(R.dimen.text_field).value.sp) },
        singleLine = true,
        shape = RoundedCornerShape(dimensionResource(R.dimen.field_corner)),
        colors = fieldColors(),
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateField(
    value: String,
    onDateSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val pattern = stringResource(R.string.date_format)
    val timeZoneId = stringResource(R.string.timezone_utc)

    Box(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            enabled = false,
            label = { Text(stringResource(R.string.birth_date), fontSize = dimensionResource(R.dimen.text_field).value.sp) },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = stringResource(R.string.cd_calendar),
                    modifier = Modifier.size(dimensionResource(R.dimen.icon_size))
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(dimensionResource(R.dimen.field_corner)),
            colors = fieldColors(),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDialog = true }
        )
    }

    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val formatter = SimpleDateFormat(pattern, Locale.getDefault())
                        formatter.timeZone = TimeZone.getTimeZone(timeZoneId)
                        onDateSelected(formatter.format(Date(millis)))
                    }
                    showDialog = false
                }) { Text(stringResource(R.string.ok)) }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text(stringResource(R.string.cancel)) }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
fun ExperienceDropdown(
    value: String,
    onSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val options = stringArrayResource(R.array.experience_options)

    Box(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            enabled = false,
            label = { Text(stringResource(R.string.experience), fontSize = dimensionResource(R.dimen.text_field).value.sp) },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = stringResource(R.string.cd_dropdown)
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(dimensionResource(R.dimen.field_corner)),
            colors = fieldColors(),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun SkillCheckbox(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = colorResource(R.color.checkbox_checked),
                uncheckedColor = colorResource(R.color.checkbox_unchecked)
            )
        )
        Text(
            text = label,
            color = colorResource(R.color.text_label),
            fontSize = dimensionResource(R.dimen.text_field).value.sp
        )
    }
}