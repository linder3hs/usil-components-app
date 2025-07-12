package com.usil.myappcomponents

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.usil.myappcomponents.ui.theme.MyAppComponentsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyAppComponentsTheme {
                MainView()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun MainView() {
    Scaffold(topBar = {
        TopAppBar(
            title = { Text("My Components", color = Color.White) },
            colors = TopAppBarDefaults.topAppBarColors(
                colorResource(R.color.purple_500)
            )
        )
    }) { paddingValues ->
        ComponentsView(paddingValues)
    }
}

@Composable
fun ComponentsView(paddingValues: PaddingValues = PaddingValues(12.dp)) {
    // current context
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(paddingValues)
            .padding(horizontal = 12.dp)
    ) {
        Button(modifier = Modifier.fillMaxWidth(),
            onClick = {
                // instance
                val intent = Intent(context, TextViewActivity::class.java)
                context.startActivity(intent)
            }) {
            Text("Textos")
        }
        Button(modifier = Modifier.fillMaxWidth(),
            onClick = {}) {
            Text("Botones")
        }
        Button(onClick = {
            val intent = Intent(context, ImageViewActivity::class.java)
            context.startActivity(intent)
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Images")
        }
        Button(onClick = {
            val intent = Intent(context, TasksViewActivity::class.java)
            context.startActivity(intent)
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Tareas")
        }
    }
}