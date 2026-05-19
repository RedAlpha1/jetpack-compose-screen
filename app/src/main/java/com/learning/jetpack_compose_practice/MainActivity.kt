package com.learning.jetpack_compose_practice

import OnboardingScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.learning.jetpack_compose_practice.screens.day1.FoodHomeScreen
import com.learning.jetpack_compose_practice.screens.day2.ProductDetailScreen
import com.learning.jetpack_compose_practice.ui.theme.JETPACK_COMPOSE_PRACTICETheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JETPACK_COMPOSE_PRACTICETheme {
                //FoodHomeScreen()
                //ProductDetailScreen(onBackClicked = { finish() },)
                OnboardingScreen()
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JETPACK_COMPOSE_PRACTICETheme {
        Greeting("Android")
    }
}