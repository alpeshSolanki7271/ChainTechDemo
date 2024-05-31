package com.example.chaintechdemo.util

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.launch
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import kotlin.random.Random

class AppConstants {

    companion object {

        @RequiresApi(Build.VERSION_CODES.O)
        fun encrypt(text: String, secretKey: String): String {
            val cipher = Cipher.getInstance("AES")
            val keySpec = SecretKeySpec(secretKey.toByteArray(), "AES")
            cipher.init(Cipher.ENCRYPT_MODE, keySpec)
            val encryptedBytes = cipher.doFinal(text.toByteArray())
            return Base64.getEncoder().encodeToString(encryptedBytes)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun decrypt(encryptedText: String, secretKey: String): String {
            val cipher = Cipher.getInstance("AES")
            val keySpec = SecretKeySpec(secretKey.toByteArray(), "AES")
            cipher.init(Cipher.DECRYPT_MODE, keySpec)
            val encryptedBytes = Base64.getDecoder().decode(encryptedText)
            val decryptedBytes = cipher.doFinal(encryptedBytes)
            return String(decryptedBytes)
        }

        fun getPasswordStrength(password: String): String {
            val lengthCriteria = password.length >= 8
            val numberCriteria = password.any { it.isDigit() }
            val uppercaseCriteria = password.any { it.isUpperCase() }
            val lowercaseCriteria = password.any { it.isLowerCase() }
            val specialCharCriteria = password.any { "!@#$%^&*()-_+=<>?/".contains(it) }

            return when {
                password.isEmpty() -> ""
                lengthCriteria && numberCriteria && uppercaseCriteria && lowercaseCriteria && specialCharCriteria -> "Very Strong"
                lengthCriteria && (numberCriteria || uppercaseCriteria || lowercaseCriteria || specialCharCriteria) -> "Strong"
                lengthCriteria -> "Medium"
                else -> "Weak"
            }
        }

        fun getColorForStrength(strength: String): Color {
            return when (strength) {
                "Very Strong" -> Color.Green
                "Strong" -> Color.Blue
                "Medium" -> Color.Yellow
                "Weak" -> Color.Red
                else -> Color.Gray
            }
        }


        fun generatePassword(): String {
            val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
            val random = Random.Default
            return (1..6)
                .map { chars[random.nextInt(chars.length)] }
                .joinToString("")
        }

    }
}