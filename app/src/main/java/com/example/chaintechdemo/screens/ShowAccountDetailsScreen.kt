package com.example.chaintechdemo.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chaintechdemo.R
import com.example.chaintechdemo.data.model.PasswordData


@Composable
fun ShowAccountDetailsScreen(passwordData: PasswordData,onEditClick: () -> Unit,onDeleteClick: (id:Int) -> Unit) {
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

        Text(
            text = "Account Type",
            color = colorResource(id = R.color.text_color_view),
            fontSize = 16.sp
        )
        Text(
            text = passwordData.accountType,
            color = colorResource(id = R.color.show_text_color),
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Username/ Email",
            color = colorResource(id = R.color.text_color_view),
            fontSize = 16.sp
        )
        Text(
            text = passwordData.username,
            color = colorResource(id = R.color.show_text_color),
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Password", color = colorResource(id = R.color.text_color_view), fontSize = 16.sp
        )
        Row(
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Text(
                text = "*********",
                color = colorResource(id = R.color.show_text_color),
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )
            ClickableText(
                text = AnnotatedString("🔒"),
                onClick = { /* Handle show/hide password */ },
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 18.sp, fontWeight = FontWeight.SemiBold
                )
            )
        }
        Spacer(modifier = Modifier.height(32.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = { onEditClick() },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
            ) {
                Text(
                    text = "Edit", color = colorResource(id = R.color.white), fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = { onDeleteClick(passwordData.id)},
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