package com.usil.myappcomponents

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.usil.myappcomponents.ui.theme.MyAppComponentsTheme

class ImageViewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyAppComponentsTheme {
                ImageMainView()
            }
        }
    }
}

//@Preview
//@Composable
//fun ListComponent() {
//    val elements = (0..100).map { "$it - text" }
//    LazyColumn {
//        items(elements) { element ->
//            RenderItem(item = element)
//        }
//    }
//}

@Composable
fun RenderItem(item: String) {
    Text(item)
}

//@Preview
@Composable
fun ImageMainView() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Icono de Favorito",
                    tint = Color.Red
                )
                Text(
                    "Imagenes Favoritas",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Image(
                painter = painterResource(R.drawable.avenger),
                contentDescription = "Es una imagen lo de los vengadores",
                modifier = Modifier.size(350.dp)
            )
            Image(
                painter = painterResource(R.drawable.avenger_2),
                contentDescription = "Imagen 2 de los vengadores",
                modifier = Modifier.size(250.dp)
            )
            Image(
                painter = painterResource(R.drawable.avengers_3jpg),
                contentDescription = "Imagen 3 de los vengadores"
            )
        }

    }
}

