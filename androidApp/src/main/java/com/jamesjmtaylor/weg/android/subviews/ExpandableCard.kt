package com.jamesjmtaylor.weg.android.subviews

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import com.jamesjmtaylor.weg.android.R
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jamesjmtaylor.weg.models.Variant

//TODO: Build Previews
@Composable
fun ExpandableCard(
    variant: Variant,
    onCardArrowClick: () -> Unit,
    expanded: Boolean,
) {
    val transitionState = remember {
        MutableTransitionState(expanded).apply {
            targetState = !expanded
        }
    }
    val transition = updateTransition(transitionState, label = "transition")
    val cardBgColor by transition.animateColor({
        tween(durationMillis = EXPAND_ANIMATION_DURATION)
    }, label = "cardBgColor") { expand ->
        if (expand) colorResource(id = R.color.cardExpandedBackgroundColor)
        else colorResource(id = R.color.cardCollapsedBackgroundColor)
    }
    val cardPaddingHorizontal by transition.animateDp({
        tween(durationMillis = EXPAND_ANIMATION_DURATION)
    }, label = "cardPaddingHorizontal") { expand -> if (expand) 48.dp else 24.dp }
    val cardElevation by transition.animateDp({
        tween(durationMillis = EXPAND_ANIMATION_DURATION)
    }, label = "cardElevation") { expand -> if (expand) 24.dp else 4.dp }
    val cardRoundedCorners by transition.animateDp({
        tween(
            durationMillis = EXPAND_ANIMATION_DURATION,
            easing = FastOutSlowInEasing
        )
    }, label = "cardRoundedCorners") { expand -> if (expand) 0.dp else 16.dp }
    val arrowRotationDegree by transition.animateFloat({
        tween(durationMillis = EXPAND_ANIMATION_DURATION)
    }, label = "arrowRotationDegree") { expand -> if (expand) 0f else 180f }

    Card(
        backgroundColor = cardBgColor,
        contentColor = colorResource(id = R.color.contentColor),
        elevation = cardElevation,
        shape = RoundedCornerShape(cardRoundedCorners),
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = cardPaddingHorizontal,
                vertical = 8.dp
            )
    ) {
        Column {
            Box {
                CardArrow(
                    degrees = arrowRotationDegree,
                    onClick = onCardArrowClick
                )
                CardTitle(title = variant.name)
            }
            ExpandableContent(visible = expanded)
        }
    }
}


@Composable
fun CardTitle(title: String) {
    Text(
        text = title,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        textAlign = TextAlign.Center,
    )
}

@Composable
fun CardArrow(
    degrees: Float,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        content = {
            Icon(
                painter = painterResource(id = R.drawable.ic_expand_less_24),
                contentDescription = "Expandable Arrow",
                modifier = Modifier.rotate(degrees),
            )
        }
    )
}

@Composable
fun ExpandableContent(
    visible: Boolean = true
) {
    val enterTransition = remember {
        expandVertically(
            expandFrom = Alignment.Top,
            animationSpec = tween(EXPANSION_TRANSITION_DURATION)
        ) + fadeIn(
            initialAlpha = 0.3f,
            animationSpec = tween(EXPANSION_TRANSITION_DURATION)
        )
    }
    val exitTransition = remember {
        shrinkVertically(
            // Expand from the top.
            shrinkTowards = Alignment.Top,
            animationSpec = tween(EXPANSION_TRANSITION_DURATION)
        ) + fadeOut(
            // Fade in with the initial alpha of 0.3f.
            animationSpec = tween(EXPANSION_TRANSITION_DURATION)
        )
    }
    AnimatedVisibility(
        visible = visible,
        enter = enterTransition,
        exit = exitTransition
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Spacer(modifier = Modifier.heightIn(100.dp))
            Text(
                text = "Expandable content here",
                textAlign = TextAlign.Center
            )
        }

    }
}
const val EXPAND_ANIMATION_DURATION = 2000 //Transition time from arrow down to arrow up
const val EXPANSION_TRANSITION_DURATION = 4000 //Transition time from box closed to box open