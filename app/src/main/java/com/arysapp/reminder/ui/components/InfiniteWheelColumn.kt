package com.arysapp.reminder.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun InfiniteWheelColumn(
    items: List<Int>,
    initialIndex: Int = 0,
    width: Dp,
    itemHeight: Dp,
    repeatCount: Int = 20,
    formatter: (Int) -> String = { it.toString() },
    onItemSelected: (Int) -> Unit
) {
    val safeInitial = initialIndex.coerceIn(0, items.lastIndex)
    val repeated = remember(items, repeatCount) {
        List(repeatCount) { items }.flatten()
    }
    val centerOffset = repeated.size / 2
    val startIndex = centerOffset + safeInitial

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = startIndex)
    val flingBehavior = rememberSnapFlingBehavior(listState)

    val visibleCount = 3
    val totalHeight = itemHeight * visibleCount

    Box(
        modifier = Modifier
            .width(width)
            .height(totalHeight),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .height(itemHeight)
                .fillMaxWidth()
        )

        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(vertical = (totalHeight - itemHeight) / 2),
        ) {
            itemsIndexed(repeated, key = { index, _ -> index }) { index, value ->

                val centerIndex by remember {
                    derivedStateOf {
                        val firstIndex = listState.firstVisibleItemIndex
                        val offsetCorrection = -1
                        firstIndex + visibleCount / 2 + offsetCorrection
                    }
                }
                val isSelected = centerIndex == index

                val fontSizeAnim by animateFloatAsState(targetValue = if (isSelected) 22f else 16f)
                val backgroundColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f) else Color.Transparent
                val fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                val textColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                val shadowElevation = if (isSelected) 1.dp else 0.dp

                Box(
                    modifier = Modifier
                        .height(itemHeight)
                        .fillMaxWidth()
                        .background(backgroundColor, shape = RoundedCornerShape(8.dp))
                        .shadow(shadowElevation, shape = RoundedCornerShape(1.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = formatter(value),
                        fontSize = fontSizeAnim.sp,
                        fontWeight = fontWeight,
                        color = textColor,
                        modifier = Modifier.wrapContentHeight(Alignment.CenterVertically),
                    )
                }
            }
        }

        LaunchedEffect(listState) {
            snapshotFlow {
                val visible = listState.layoutInfo.visibleItemsInfo
                if (visible.isEmpty()) null else visible[visible.size / 2].index
            }
                .map { it ?: listState.firstVisibleItemIndex }
                .distinctUntilChanged()
                .collectLatest { centerIndex ->
                    val actualIndex = centerIndex % items.size
                    val value = repeated.getOrNull(centerIndex) ?: return@collectLatest
                    onItemSelected(value)

                    val threshold = items.size * 2
                    if (centerIndex < threshold || centerIndex > repeated.size - threshold) {
                        val relative = actualIndex.coerceIn(0, items.lastIndex)
                        val target = centerOffset + relative
                        listState.animateScrollToItem(target)
                    }
                }
        }
    }
}
