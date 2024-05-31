package com.example.chaintechdemo.screens

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.fragment.app.FragmentActivity
import com.example.chaintechdemo.R
import com.example.chaintechdemo.data.model.PasswordData
import com.example.chaintechdemo.util.AppConstants
import com.example.chaintechdemo.util.AppConstants.Companion.getColorForStrength
import com.example.chaintechdemo.util.AppConstants.Companion.getPasswordStrength
import com.example.chaintechdemo.util.BiometricHelper

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditAccountDetailsScreen(
    passwordData: PasswordData,
    onUpdateClick: (passwordData: PasswordData) -> Unit,
    onDeleteClick: (passwordId: Int) -> Unit
) {

    val context = LocalContext.current
    val activity = LocalContext.current as FragmentActivity
    val biometricHelper = remember { BiometricHelper(activity) }
    var accountName by remember { mutableStateOf(passwordData.accountType) }
    var username by remember { mutableStateOf(passwordData.username) }

    val decryptPass =
        AppConstants.decrypt(passwordData.password,secretKey = "ASDFGHJKLASDFGHJ")

    var password by remember { mutableStateOf(decryptPass) }
    var passwordVisible by remember { mutableStateOf(false) }
    val strength = getPasswordStrength(password)

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
        OutlinedTextField(value = password,
            onValueChange = { password = it },
            singleLine = true,
            label = { Text("Password") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
            ),
            trailingIcon = @androidx.compose.runtime.Composable {
                val image = if (passwordVisible) Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff
                val description = if (passwordVisible) "Hide password" else "Show password"
                IconButton(onClick = {
                    if (passwordVisible) {
                        passwordVisible = false
                    } else {
                        if (biometricHelper.isBiometricAvailable()) {
                            biometricHelper.showBiometricPrompt(activity,
                                onSuccess = { passwordVisible = true },
                                onError = { errorMessage ->
                                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                                })
                        } else if (biometricHelper.isDeviceCredentialAvailable()) {
                            biometricHelper.showDeviceCredentialPrompt(activity,
                                onSuccess = { passwordVisible = true },
                                onError = { errorMessage ->
                                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                                })
                        } else {
                            passwordVisible = !passwordVisible
                        }
                    }
                }) {
                    Icon(imageVector = image, description)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Password Strength: $strength",
            color = getColorForStrength(strength),
            textAlign = TextAlign.End
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { password = AppConstants.generatePassword() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Generate Password")
        }
        Spacer(modifier = Modifier.height(25.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {

                    if (accountName.isEmpty()) {
                        Toast.makeText(context, "Account name cannot be empty", Toast.LENGTH_SHORT)
                            .show()
                    } else if (username.isEmpty()) {
                        Toast.makeText(
                            context, "Username/email cannot be empty", Toast.LENGTH_SHORT
                        ).show()
                    } else if (password.isEmpty()) {
                        Toast.makeText(context, "Password cannot be empty", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        val encryptedPass = AppConstants.encrypt(text = password, secretKey = "ASDFGHJKLASDFGHJ")
                        val passData = PasswordData(passwordData.id,accountName, username, encryptedPass)
                        onUpdateClick(passData)
                    }

                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text(
                    text = "Update", color = colorResource(id = R.color.white), fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = { onDeleteClick(passwordData.id) },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.delete_color))
            ) {
                Text(
                    text = "Delete", color = Color.White, fontSize = 16.sp
                )
            }
        }
    }
}