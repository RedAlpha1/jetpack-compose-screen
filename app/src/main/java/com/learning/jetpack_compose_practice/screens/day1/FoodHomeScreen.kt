package com.learning.jetpack_compose_practice.screens.day1

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learning.jetpack_compose_practice.screens.day1.data.Category
import com.learning.jetpack_compose_practice.screens.day1.data.FoodItem
import com.learning.jetpack_compose_practice.screens.day1.data.allItems
import com.learning.jetpack_compose_practice.screens.day1.data.categories
import com.learning.jetpack_compose_practice.screens.day1.data.popularItems
import com.learning.jetpack_compose_practice.ui.theme.DarkBg
import com.learning.jetpack_compose_practice.ui.theme.OrangeAccent
import com.learning.jetpack_compose_practice.ui.theme.PageBg
import com.learning.jetpack_compose_practice.ui.theme.StarColor

@Composable
fun FoodHomeScreen() {
    val selectedCategoryId = remember { mutableIntStateOf(1) }
    val cartCount = remember { mutableIntStateOf(0) }


    Scaffold(
        containerColor = PageBg,
        topBar = {
            FoodTopBar(
                cartCount = cartCount,
                locationName = "Delhi, India"
            )
        }
    ) { innerPadding ->
        // Outer Lazy Column for Vertical Scroll
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(0.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // Search Bar Section
            item {
                SearchBarSection()
            }

            item {
                PromoBanner(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
            }

            item {
                CategoriesChipRow(
                    categories = categories,
                    selectedCategoryId = selectedCategoryId.value,
                    onCategorySelected = {
                        selectedCategoryId.value = it
                    }
                )
            }

            item {
                SectionHeader(
                    title = "Popular near you",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            item {
                PopularItemRow(
                    popularItems = popularItems,
                    onAddToCartClicked = {
                        cartCount.value += 1
                    }
                )
            }

            item {
                SectionHeader(
                    title = "All Item",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
            // ── Vertical list of food items ──
            // key = item.id for stable recomposition
            items(items = allItems, key = { it.id }) { item ->
                FoodListCard(
                    item = item,
                    onAddToCart = {
                        cartCount.value += 1
                    },
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 10.dp)
                )
            }

        }
    }
}


@Composable
fun FoodListCard(
    item: FoodItem,
    onAddToCart: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Emoji image box
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(PageBg),
            contentAlignment = Alignment.Center
        ) {
            Text(text = item.emoji, fontSize = 32.sp)
        }

        // Info column
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.name,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1A1A1A),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = item.restaurant,
                fontSize = 11.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 2.dp)
            )

            // Meta row: price · time · rating
            Row(
                modifier = Modifier.padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "₹${item.price}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF1A1A1A)
                )
                Text(text = "⏱ ${item.deliveryTime} min", fontSize = 10.sp, color = Color.Gray)
                Text(text = "★", fontSize = 11.sp, color = StarColor)
                Text(
                    text = "${item.rating}",
                    fontSize = 10.sp,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Add button
        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .background(OrangeAccent)
                .clickable { onAddToCart() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "+",
                fontSize = 18.sp,
                color = Color.White,
                fontWeight = FontWeight.Medium
            )
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FoodHomeScreenPreview() {
    MaterialTheme {
        FoodHomeScreen()
    }
}

@Composable
fun SectionHeader(title: String, modifier: Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF1A1A1A)
        )
        Text(
            text = "See all",
            fontSize = 12.sp,
            color = OrangeAccent
        )
    }
}


@Composable
fun FoodTopBar(cartCount: MutableIntState, locationName: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 10.dp),

        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = "Deliver to", fontSize = 11.sp, color = Color.Gray)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = locationName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF1A1A1A)
                )
                Text(
                    text = " ▾",
                    fontSize = 12.sp,
                    color = OrangeAccent
                )
            }
        }

        BadgedBox(
            badge = {
                if (cartCount.value > 0) {
                    Badge(
                        containerColor = OrangeAccent,
                        contentColor = Color.White
                    ) {
                        Text(text = cartCount.value.toString(), fontSize = 12.sp)
                    }
                }
            }
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = "Cart Icon",
                tint = Color(0xFF1A1A1A)
            )
        }
    }

}

@Composable
fun SearchBarSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF1EFEA))
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = "🔍", fontSize = 14.sp)
        Text(
            text = "Search restaurants or dishes...",
            fontSize = 13.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun PromoBanner(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(DarkBg)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "Limited time offer",
                fontSize = 11.sp,
                color = Color.White.copy(alpha = 0.5f)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Get 40% off\nyour first order",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White,
                lineHeight = 24.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Use code: FOODIE40",
                fontSize = 11.sp,
                color = Color.White.copy(alpha = 0.5f)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(OrangeAccent)
                    .clickable { /* navigate */ }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Order now",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
        }

        Text(text = "🍔", fontSize = 56.sp)
    }

}

@Composable
fun CategoriesChipRow(
    categories: List<Category>,
    selectedCategoryId: Int,
    onCategorySelected: (Int) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(vertical = 10.dp)
    ) {
        items(items = categories, key = { it.id }) { category ->
            val isSelected = category.id == selectedCategoryId

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (isSelected) OrangeAccent else Color.White)
                    .clickable { onCategorySelected(category.id) }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = if (category.emoji.isNotEmpty()) "${category.emoji} ${category.name}"
                    else category.name,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isSelected) Color.White else Color(0xFF555555)
                )
            }
        }
    }
}

@Composable
fun PopularItemRow(
    popularItems: List<FoodItem>,
    onAddToCartClicked: () -> Unit
) {
    // Second inner LazyRow — nested inside the outer LazyColumn
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items = popularItems, key = { it.id }) { item ->
            PopularFoodCard(
                item = item,
                onAddToCart = onAddToCartClicked
            )
        }
    }
}

@Composable
fun PopularFoodCard(
    item: FoodItem,
    onAddToCart: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(150.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(10.dp)
    ) {
        // Emoji image box + optional badge
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(PageBg),
            contentAlignment = Alignment.Center
        ) {
            Text(text = item.emoji, fontSize = 40.sp)

            // Badge (Hot / New)
            item.badge?.let { badge ->
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(OrangeAccent)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = badge,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = item.name,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF1A1A1A),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = item.restaurant,
            fontSize = 10.sp,
            color = Color.Gray,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        // Rating + delivery time
        Row(
            modifier = Modifier.padding(top = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text(text = "★", fontSize = 11.sp, color = StarColor)
            Text(
                text = "${item.rating}",
                fontSize = 10.sp,
                color = Color.DarkGray,
                fontWeight = FontWeight.Medium
            )
            Text(text = "· ${item.deliveryTime} min", fontSize = 10.sp, color = Color.Gray)
        }

        // Price + Add button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "₹${item.price}",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1A1A1A)
            )

            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(OrangeAccent)
                    .clickable { onAddToCart() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "+",
                    fontSize = 16.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }


}



