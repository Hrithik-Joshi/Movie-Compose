package com.hrithik.moviecompose.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage

@Composable
fun MovieCard(
    imageUrl: String?,
    likes: Int?,
    title: String?,
    releaseDate: String?,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(280.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1F2A44))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                AsyncImage(
                    model = "https://image.tmdb.org/t/p/w500/${imageUrl}",
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp))
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp)
                        .offset(y = 28.dp)
                ) {
                    likes?.let {
                        CustomerCircularProgressBar(
                            progress = it,
                            startAngle = 180f,
                            size = 40.dp,
                            strokeWidth = 5.dp,
                            progressArcColor1 = Color(0xFFD4AF37),
                            progressArcColor2 = Color(0xFFFFC300),
                            backgroundArcColor = Color(0xFF4A4A4A),
                            modifier = Modifier.align(Alignment.BottomStart)
                        )
                    }
                    Text(
                        text = "$likes%",
                        color = Color.White,
                        fontSize = 12.sp,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
            if (title != null) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 16.sp,
                    color = Color(0xFFF0F0F0),
                    modifier = Modifier.padding(top = 25.dp)
                )
            }
            if (releaseDate != null) {
                Text(
                    text = releaseDate,
                    fontSize = 12.sp,
                    color = Color(0xFFB0B0B0)
                )
            }
        }
    }
}
