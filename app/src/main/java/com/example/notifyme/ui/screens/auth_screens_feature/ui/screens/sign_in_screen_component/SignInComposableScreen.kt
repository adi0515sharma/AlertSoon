package com.example.notifyme.ui.screens.auth_screens_feature.ui.screens.sign_in_screen_component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.notifyme.R
import com.example.notifyme.ui.utils.Validator.isEmailValid
import com.example.notifyme.ui.utils.Validator.isFieldCorrect
import com.example.notifyme.ui.utils.ValidatorResponse
import com.example.notifyme.ui.component.AppBar
import com.example.notifyme.ui.navigation.AuthNavScreen
import com.example.notifyme.ui.component.EntryField
import com.example.notifyme.ui.component.AppButton
import com.example.notifyme.ui.component.HandleClick
import com.example.notifyme.ui.component.SetEntryFieldValue
import java.util.Calendar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInComposableScreen(
    navController: NavHostController,
    onGoogleSignIn: () -> Unit,
    onFacebookSignIn: () -> Unit,
    onNormalSignIn: (email: String, password: String) -> Unit
) {




    var emailId by rememberSaveable { mutableStateOf("") }
    var emailIdError by rememberSaveable { mutableStateOf<String?>(null) }

    var password by rememberSaveable { mutableStateOf("") }
    var passwordError by rememberSaveable { mutableStateOf<String?>(null) }


    var emailIdListener = object : SetEntryFieldValue {
        override fun setValue(s: String) {
            emailId = s
        }

    }
    var passwordListener = object : SetEntryFieldValue {
         override fun setValue(s: String) {
            password = s
        }

    }

    // Initialize sign in client

    Scaffold(
        topBar = {
            AppBar()
        },
        content = {it->
            Column(

                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()

                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,

                ) {

                Text(
                    text = "SIGN IN", fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                    fontWeight = FontWeight.W800
                )
                Spacer(modifier = Modifier.height(13.dp))
                EntryField(Modifier, emailId, emailIdListener, "Enter your email id", emailIdError)
                Spacer(modifier = Modifier.height(13.dp))
                EntryField(Modifier, password, passwordListener, "Enter your password", passwordError, true)

                Spacer(modifier = Modifier.height(20.dp))


                AppButton(Modifier.fillMaxWidth(),"Sign In", object : HandleClick {
                    override fun onClick() {

                        val emailVarify = emailId.isEmailValid()
                        if (emailVarify != ValidatorResponse.Success) {
                            emailIdError = (emailVarify as ValidatorResponse.Error).message
                            return
                        } else {
                            emailIdError = null
                        }


                        val passwordVarify = password.isFieldCorrect("password")
                        if (passwordVarify != ValidatorResponse.Success) {
                            passwordError = (passwordVarify as ValidatorResponse.Error).message
                            return
                        } else {
                            passwordError = null
                        }

                        onNormalSignIn(emailId, password)

                    }

                })

                Spacer(modifier = Modifier.height(24.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Text(
                        text = "Not a user? ",
                        fontFamily = FontFamily(Font(R.font.poppins_regular)),
                    )
                    Text(
                        text = "SIGN UP",
                        color = Color.Blue,
                        textDecoration = TextDecoration.Underline,
                        fontFamily = FontFamily(Font(R.font.poppins_regular)),
                        modifier = Modifier.clickable {
                            navController.navigate(AuthNavScreen.SIGN_UP.name)
                        }
                    )
                }


                Spacer(modifier = Modifier.height(20.dp))

                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Divider(
                        modifier = Modifier
                            .weight(1f)
                            .height(1.dp),
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "OR", color = Color.Gray, modifier = Modifier
                            .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                            .padding(horizontal = 4.dp, vertical = 3.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Divider(
                        modifier = Modifier
                            .weight(1f)
                            .height(1.dp),
                        color = Color.Gray
                    )
                }


                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Sign in directly through socials",
                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                    fontWeight = FontWeight.W800,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_google),
                        contentDescription = "google sign in",
                        modifier = Modifier.clickable {
                            onGoogleSignIn()
                        }
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    Image(
                        painter = painterResource(id = R.drawable.ic_facebook),
                        contentDescription = "facebook sign in",
                        modifier = Modifier.clickable {
                            onFacebookSignIn()
                        }
                    )

                }
            }
        }

    )

}

