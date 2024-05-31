package com.example.chaintechdemo.screens

import android.annotation.SuppressLint
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chaintechdemo.R
import com.example.chaintechdemo.data.model.PasswordData
import com.example.chaintechdemo.screens.viewmodel.PasswordViewModel
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("RememberReturnType")
@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen() {
    val context = LocalContext.current
    val passwordViewModel: PasswordViewModel = hiltViewModel()
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    var bottomSheetContent by remember { mutableStateOf<@Composable ColumnScope.() -> Unit>({}) }


    val passwordDataList by passwordViewModel.passwordDataList.observeAsState(initial = emptyList())

    // Trigger the fetching of user list when the screen is first created
    LaunchedEffect(key1 = true) {
        passwordViewModel.fetchUserList()
    }

    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                text = "Password Manager   ", style = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    fontFamily = FontFamily.Default
                )
            )
        })
    }, floatingActionButton = {
        FloatingActionButton(
            onClick = {
                showBottomSheet = true
                bottomSheetContent = {
                    AddAccountScreen(onAddButtonClick = { passwordData ->
                        scope.launch {
                            passwordViewModel.addAccount(passwordData = passwordData)
                            passwordViewModel.fetchUserList()
                            Toast.makeText(
                                context, "Account details Added Successfully", Toast.LENGTH_SHORT
                            ).show()
                        }
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                showBottomSheet = false
                            }
                        }
                    })
                }
            }, containerColor = colorResource(id = R.color.blue)
        ) {
            Icon(Icons.Filled.Add, "add", tint = Color.White)
        }
    }) {
        Column(
            Modifier
                .padding(it)
                .fillMaxSize()
        ) {

            Divider(color = Color.LightGray, thickness = 1.dp)

            if (passwordDataList.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "No Data Found", style = TextStyle(
                            fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center
                        ), fontSize = 25.sp, modifier = Modifier.fillMaxWidth()
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 25.dp),
                    content = {
                        items(passwordDataList) { passwordData ->
                            AccountDetailItem(passwordData = passwordData, onCardClick = { pData ->
                                showBottomSheet = true
                                bottomSheetContent = {
                                    ShowAccountDetailsScreen(passwordData = passwordData,
                                        onEditClick = {
                                            showBottomSheet = true
                                            bottomSheetContent = {
                                                EditAccountDetailsScreen(passwordData = passwordData,
                                                    onUpdateClick = { pmData ->
                                                        scope.launch {
                                                            passwordViewModel.updateData(
                                                                passwordData = pmData
                                                            )
                                                            passwordViewModel.fetchUserList()
                                                            Toast.makeText(
                                                                context,
                                                                "Account details updated Successfully",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                        }
                                                        scope.launch { sheetState.hide() }
                                                            .invokeOnCompletion {
                                                                if (!sheetState.isVisible) {
                                                                    showBottomSheet = false
                                                                }
                                                            }
                                                    },
                                                    onDeleteClick = { accountId ->
                                                        scope.launch {
                                                            passwordViewModel.deleteData(id = accountId)
                                                            passwordViewModel.fetchUserList()
                                                            Toast.makeText(
                                                                context,
                                                                "Account details Added Successfully",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                        }
                                                        scope.launch { sheetState.hide() }
                                                            .invokeOnCompletion {
                                                                if (!sheetState.isVisible) {
                                                                    showBottomSheet = false
                                                                }
                                                            }
                                                    })
                                            }
                                        },
                                        onDeleteClick = { id ->
                                            scope.launch {
                                                passwordViewModel.deleteData(id = id)
                                                passwordViewModel.fetchUserList()
                                                Toast.makeText(
                                                    context,
                                                    "Account details Added Successfully",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                                if (!sheetState.isVisible) {
                                                    showBottomSheet = false
                                                }
                                            }
                                        })
                                }
                            })


                        }
                    },
                    contentPadding = PaddingValues(bottom = 16.dp)
                )
            }

            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = {
                        showBottomSheet = false
                    }, sheetState = sheetState, content = bottomSheetContent
                )
            }
        }
    }
}


@Composable
fun AccountDetailItem(
    passwordData: PasswordData, onCardClick: (passwordData: PasswordData) -> Unit
) {
    Card(elevation = CardDefaults.cardElevation(10.dp),
        border = BorderStroke(1.dp, color = Color.LightGray),
        shape = RoundedCornerShape(50),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.clickable {
            onCardClick(passwordData)
        }) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White, RoundedCornerShape(50))
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = passwordData.accountType,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.wrapContentWidth()
            )
            Text(
                text = "********",
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Gray,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(top = 5.dp, start = 12.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow),
                contentDescription = "Arrow Icon",
                tint = Color.Gray,
            )
        }
    }
}