package com.example.chaintechdemo.screens

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chaintechdemo.R
import com.example.chaintechdemo.data.model.PasswordData
import com.example.chaintechdemo.util.AppConstants
import com.example.chaintechdemo.util.AppConstants.Companion.generatePassword
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddAccountScreen(onAddButtonClick: (passwordData: PasswordData) -> Unit) {

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var password by rememberSaveable { mutableStateOf("") }
    var accountName by rememberSaveable { mutableStateOf("") }
    var username by rememberSaveable { mutableStateOf("") }
    val strength = AppConstants.getPasswordStrength(password)
    var passwordVisible by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Account Details",
            color = colorResource(id = R.color.blue),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(30.dp))
        OutlinedTextField(
            value = accountName,
            onValueChange = { accountName = it },
            label = { Text("Account Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
            ),

            )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username/ Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
            ),
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
            ),
            trailingIcon = @androidx.compose.runtime.Composable {
                val image = if (passwordVisible) Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff
                val description = if (passwordVisible) "Hide password" else "Show password"
                IconButton(onClick = {
                    passwordVisible = !passwordVisible
                }) {
                    Icon(imageVector = image, description)
                }
            },
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { password = generatePassword() }, modifier = Modifier.fillMaxWidth()
        ) {
            Text("Generate Password")
        }
        Text(
            text = "Password Strength: $strength", color = AppConstants.getColorForStrength(
                strength
            ), textAlign = TextAlign.End
        )
        Spacer(modifier = Modifier.height(25.dp))
        Button(
            onClick = {
                if (accountName.isEmpty()) {
                    Toast.makeText(context, "Account name cannot be empty", Toast.LENGTH_SHORT)
                        .show()
                } else if (username.isEmpty()) {
                    Toast.makeText(context, "Username/email cannot be empty", Toast.LENGTH_SHORT)
                        .show()
                } else if (password.isEmpty()) {
                    Toast.makeText(context, "Password cannot be empty", Toast.LENGTH_SHORT).show()
                } else {
                    val encryptedPass = AppConstants.encrypt(text = password, secretKey = "ASDFGHJKLASDFGHJ")
                    val passwordData = PasswordData(accountName, username, encryptedPass)
                    onAddButtonClick(passwordData)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.button_color))
        ) {
            Text(
                text = "Add New Account",
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier.padding(vertical = 5.dp)
            )
        }
    }
}


