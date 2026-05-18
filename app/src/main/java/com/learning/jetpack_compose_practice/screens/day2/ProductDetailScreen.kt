package com.learning.jetpack_compose_practice.screens.day2

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.learning.jetpack_compose_practice.screens.day2.data.AddOn
import com.learning.jetpack_compose_practice.screens.day2.data.ProductDetail
import com.learning.jetpack_compose_practice.screens.day2.data.sampleProduct
import com.learning.jetpack_compose_practice.ui.theme.DarkBg
import com.learning.jetpack_compose_practice.ui.theme.GreenColor
import com.learning.jetpack_compose_practice.ui.theme.OrangeAccent
import com.learning.jetpack_compose_practice.ui.theme.PageBg
import com.learning.jetpack_compose_practice.ui.theme.StarColor

// ─────────────────────────────────────────
// COLLAPSING CONSTANTS
// ─────────────────────────────────────────

private const val HERO_MAX_DP = 260f  // fully expanded
private const val HERO_MIN_DP = 60f   // fully collapsed (only TopBar visible)

// ─────────────────────────────────────────
// MAIN SCREEN
// ─────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    product: ProductDetail = sampleProduct,
    onBackClicked: () -> Unit = {}
) {
    var selectedSize by remember { mutableStateOf(product.sizes.first()) }
    var isFavorite by remember { mutableStateOf(false) }
    var quantity by remember { mutableIntStateOf(1) }
    var showBottomSheet by remember { mutableStateOf(false) }
    val selectedAddOns = remember { mutableStateListOf<AddOn>() }

    val addOnTotal = selectedAddOns.sumOf { it.price }
    val totalPrice = (product.price + addOnTotal) * quantity

    // ── Step 1: One shared ScrollState for both Column and hero calculations ──
    val scrollState = rememberScrollState()
    val density = LocalDensity.current

    // ── Step 2: derivedStateOf — recalculates ONLY when scrollState.value changes ──
    // Without derivedStateOf — every recomposition recalculates even if scroll didn't change
    // With derivedStateOf — calculation runs only when scrollState.value actually changes

    // Hero height: 260dp → 60dp
    val heroDp by remember(density) {
        derivedStateOf {
            val scrolledDp = with(density) { scrollState.value.toDp().value }
            (HERO_MAX_DP - scrolledDp * 0.6f).coerceIn(HERO_MIN_DP, HERO_MAX_DP)
        }
    }

    // Emoji alpha: 1f → 0f (fades out by the time 120dp is scrolled)
    val heroContentAlpha by remember(density) {
        derivedStateOf {
            val scrolledDp = with(density) { scrollState.value.toDp().value }
            (1f - scrolledDp / 120f).coerceIn(0f, 1f)
        }
    }

    // Title alpha: 0f → 1f (fades in after 120dp scrolled)
    val collapsedTitleAlpha by remember(density) {
        derivedStateOf {
            val scrolledDp = with(density) { scrollState.value.toDp().value }
            ((scrolledDp - 120f) / 60f).coerceIn(0f, 1f)
        }
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Scaffold(
        containerColor = PageBg,
        bottomBar = {
            StickyBottomBar(
                total = totalPrice,
                onAddToCartClick = { showBottomSheet = true }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(scrollState)  // ← Step 3: same scrollState here
            ) {

                // ── Step 4: Hero gets live derived values ──
                HeroSection(
                    emoji = product.emoji,
                    badge = product.badge,
                    productName = product.name,
                    isFavorite = isFavorite,
                    heroHeight = heroDp,
                    heroContentAlpha = heroContentAlpha,
                    collapsedTitleAlpha = collapsedTitleAlpha,
                )

                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    ProductInfoSection(product = product)
                    Spacer(Modifier.height(16.dp))
                    HorizontalDivider(color = Color(0xFFEEEBE5))

                    SectionLabel("Description")
                    Text(text = product.description, fontSize = 13.sp, color = Color.Gray, lineHeight = 20.sp)

                    SectionLabel("Size")
                    SizeSelector(
                        sizes = product.sizes,
                        selectedSize = selectedSize,
                        onSizeSelected = { selectedSize = it }
                    )

                    SectionLabel("Add-ons")
                    product.addOns.forEach { addOn ->
                        val isSelected = addOn in selectedAddOns
                        AddOnRow(
                            addOn = addOn,
                            isSelected = isSelected,
                            onToggle = {
                                if (isSelected) selectedAddOns.remove(addOn)
                                else selectedAddOns.add(addOn)
                            }
                        )
                    }

                    SectionLabel("Quantity")
                    QuantitySelector(
                        quantity = quantity,
                        onDecrease = { if (quantity > 1) quantity-- },
                        onIncrease = { quantity++ }
                    )
                    Spacer(Modifier.height(16.dp))
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopStart)
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                HeroIconButton(icon = Icons.AutoMirrored.Outlined.ArrowBack, onClick = onBackClicked)

                // Title appears when emoji is gone
                Text(
                    text = product.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 12.dp)
                        .alpha(collapsedTitleAlpha)  // ← fades IN
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    HeroIconButton(
                        icon = Icons.Outlined.FavoriteBorder,
                        tint = if (isFavorite) Color.Red else Color.White,
                        onClick = { isFavorite = !isFavorite }
                    )
                    HeroIconButton(icon = Icons.Outlined.MoreVert, onClick = {})
                }
            }
        }


    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            dragHandle = { BottomSheetDefaults.DragHandle() },
            containerColor = Color.White
        ) {
            OrderSummarySheet(
                product = product,
                selectedSize = selectedSize,
                selectedAddOns = selectedAddOns.toList(),
                quantity = quantity,
                total = totalPrice,
                onConfirm = { showBottomSheet = false }
            )
        }
    }
}

// ─────────────────────────────────────────
// HERO — collapsing happens here
// ─────────────────────────────────────────

@Composable
fun HeroSection(
    emoji: String,
    badge: String?,
    productName: String,
    isFavorite: Boolean,
    heroHeight: Float,           // live: 260f → 60f
    heroContentAlpha: Float,     // live: 1f → 0f
    collapsedTitleAlpha: Float,  // live: 0f → 1f
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(heroHeight.dp)  // ← COLLAPSING: height changes every scroll frame
            .background(DarkBg),
        contentAlignment = Alignment.Center
    ) {
        // Emoji fades OUT as hero collapses
        Text(
            text = emoji,
            fontSize = 100.sp,
            modifier = Modifier.alpha(heroContentAlpha)
        )

        // Bottom gradient — fades page content into hero
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, PageBg),
                        startY = 300f, endY = 700f
                    )
                )
        )

        // Badge fades OUT with emoji
        badge?.let {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 16.dp, bottom = 20.dp)
                    .alpha(heroContentAlpha)  // ← fades OUT
                    .clip(RoundedCornerShape(10.dp))
                    .background(OrangeAccent)
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(text = it, fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Medium)
            }
        }
    }
}

// ─────────────────────────────────────────
// SUPPORTING COMPOSABLES
// ─────────────────────────────────────────

@Composable
fun HeroIconButton(icon: ImageVector, onClick: () -> Unit, tint: Color = Color.White) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.15f))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(20.dp))
    }
}

@Composable
fun ProductInfoSection(product: ProductDetail) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = product.name, fontSize = 20.sp, fontWeight = FontWeight.Medium, color = Color(0xFF1A1A1A))
            Text(text = product.restaurant, fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(top = 3.dp))
        }
        Spacer(Modifier.width(8.dp))
        Text(text = "₹${product.price}", fontSize = 20.sp, fontWeight = FontWeight.Medium, color = OrangeAccent)
    }
    Spacer(Modifier.height(12.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        MetaChip("★ ${product.rating}", "(${product.reviewCount})", starColor = true)
        MetaChip("${product.deliveryTime} min", "delivery")
        MetaChip("Free", "shipping", green = true)
    }
}

@Composable
fun MetaChip(label: String, sub: String, starColor: Boolean = false, green: Boolean = false) {
    Row(
        modifier = Modifier.clip(RoundedCornerShape(20.dp)).background(Color.White).padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(text = label, fontSize = 12.sp, fontWeight = FontWeight.Medium,
            color = when { starColor -> StarColor; green -> GreenColor; else -> Color(0xFF1A1A1A) })
        Text(text = sub, fontSize = 11.sp, color = Color.Gray)
    }
}

@Composable
fun SizeSelector(sizes: List<String>, selectedSize: String, onSizeSelected: (String) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        sizes.forEach { size ->
            val isSelected = size == selectedSize
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (isSelected) OrangeAccent else Color.White)
                    .clickable { onSizeSelected(size) }
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Text(text = size, fontSize = 12.sp, fontWeight = FontWeight.Medium,
                    color = if (isSelected) Color.White else Color(0xFF555555))
            }
        }
    }
    Spacer(Modifier.height(8.dp))
}

@Composable
fun AddOnRow(addOn: AddOn, isSelected: Boolean, onToggle: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = addOn.name, fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color(0xFF1A1A1A))
            Text(text = "+₹${addOn.price}", fontSize = 11.sp, color = Color.Gray, modifier = Modifier.padding(top = 2.dp))
        }
        Box(
            modifier = Modifier
                .width(42.dp).height(24.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(if (isSelected) OrangeAccent else Color(0xFFDDDAD5))
                .clickable { onToggle() }
        ) {
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .align(if (isSelected) Alignment.CenterEnd else Alignment.CenterStart)
                    .padding(3.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            )
        }
    }
    HorizontalDivider(color = Color(0xFFEEEBE5))
}

@Composable
fun QuantitySelector(quantity: Int, onDecrease: () -> Unit, onIncrease: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.padding(vertical = 4.dp)) {
        Box(modifier = Modifier.size(32.dp).clip(CircleShape).background(Color.White).clickable { onDecrease() }, contentAlignment = Alignment.Center) {
            Text("−", fontSize = 18.sp, color = Color(0xFF1A1A1A))
        }
        Text("$quantity", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color(0xFF1A1A1A))
        Box(modifier = Modifier.size(32.dp).clip(CircleShape).background(OrangeAccent).clickable { onIncrease() }, contentAlignment = Alignment.Center) {
            Text("+", fontSize = 18.sp, color = Color.White)
        }
    }
}

@Composable
fun StickyBottomBar(total: Int, onAddToCartClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().background(Color.White).navigationBarsPadding().padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text("Total price", fontSize = 11.sp, color = Color.Gray)
            Text("₹$total", fontSize = 18.sp, fontWeight = FontWeight.Medium, color = Color(0xFF1A1A1A))
        }
        Box(
            modifier = Modifier.weight(1f).padding(start = 24.dp).clip(RoundedCornerShape(14.dp)).background(OrangeAccent).clickable { onAddToCartClick() }.padding(vertical = 14.dp),
            contentAlignment = Alignment.Center
        ) { Text("Add to cart", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color.White) }
    }
}

@Composable
fun OrderSummarySheet(product: ProductDetail, selectedSize: String, selectedAddOns: List<AddOn>, quantity: Int, total: Int, onConfirm: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().navigationBarsPadding().padding(horizontal = 16.dp).padding(bottom = 24.dp)) {
        Text("Order summary", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color(0xFF1A1A1A), modifier = Modifier.padding(bottom = 16.dp))
        SheetItemRow("${product.name} ($selectedSize) ×$quantity", "₹${product.price * quantity}")
        selectedAddOns.forEach { SheetItemRow(it.name, "+₹${it.price}") }
        HorizontalDivider(color = Color(0xFFEEEBE5), modifier = Modifier.padding(vertical = 8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Total", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF1A1A1A))
            Text("₹$total", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = OrangeAccent)
        }
        Spacer(Modifier.height(20.dp))
        Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(OrangeAccent).clickable { onConfirm() }.padding(vertical = 16.dp), contentAlignment = Alignment.Center) {
            Text("Confirm order", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color.White)
        }
    }
}

@Composable
fun SheetItemRow(name: String, price: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(name, fontSize = 13.sp, color = Color.Gray, modifier = Modifier.weight(1f))
        Text(price, fontSize = 13.sp, color = Color.Gray)
    }
}

@Composable
fun SectionLabel(text: String) {
    Text(text, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF1A1A1A), modifier = Modifier.padding(top = 20.dp, bottom = 10.dp))
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProductDetailPreview() {
    MaterialTheme { ProductDetailScreen() }
}