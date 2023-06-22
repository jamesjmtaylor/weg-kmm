package com.jamesjmtaylor.weg.android.subviews

import android.content.res.Configuration
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.clickable
import com.jamesjmtaylor.weg.android.R
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class ExpandableCardContent(val title: String, val text: String)
@Composable
fun ExpandableCard(
    content: ExpandableCardContent,
    onCardClick: () -> Unit,
    expanded: Boolean,
) {
    val rotation = if (expanded) 0f else 180f
    val angle: Float by animateFloatAsState(
        targetValue = rotation,
        animationSpec = tween(
            durationMillis = ROTATION_ANIMATION_DURATION,
            easing = LinearEasing
    ), label = ""
    )

    Card(
        elevation = 4.dp,
        contentColor = colorResource(id = R.color.contentColor),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.clickable { onCardClick() }) {
            Row(modifier = Modifier.padding(8.dp)) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_expand_less_24),
                    contentDescription = "Expandable Arrow",
                    modifier = Modifier.rotate(angle),
                )
                Text(
                    text = content.title,
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
            }
            ExpandableContent(visible = expanded, text = content.text)
        }
    }
}


@Composable
fun ExpandableContent(
    visible: Boolean = false,
    text: String
) {
    val enterTransition = remember {
        expandVertically(
            expandFrom = Alignment.Top,
            animationSpec = tween(EXPAND_ANIMATION_DURATION)
        ) + fadeIn(
            initialAlpha = 0.3f,
            animationSpec = tween(EXPAND_ANIMATION_DURATION)
        )
    }
    val exitTransition = remember {
        shrinkVertically(
            shrinkTowards = Alignment.Top,
            animationSpec = tween(EXPAND_ANIMATION_DURATION)
        ) + fadeOut(
            animationSpec = tween(EXPAND_ANIMATION_DURATION)
        )
    }
    AnimatedVisibility(
        visible = visible,
        enter = enterTransition,
        exit = exitTransition
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = text)
        }
    }
}
const val ROTATION_ANIMATION_DURATION = 500 //Transition time from arrow down to arrow up
const val EXPAND_ANIMATION_DURATION = 1000 //Transition time from box closed to box open

@Preview(showBackground = true, name = "Light Mode")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true, name = "Dark Mode")
@Composable
fun PreviewExpandableCard() {
    Column {
        ExpandableCard(ExpandableCardContent("T72A", "Notes go here"), {}, false)
        ExpandableCard(ExpandableCardContent("T72A", "Notes go here"), {}, true)
    }
}