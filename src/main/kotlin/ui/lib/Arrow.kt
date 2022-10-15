package ui.lib

import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathBuilder
import androidx.compose.ui.unit.dp

enum class ArrowDirection {
    UP, DOWN, LEFT, RIGHT
}

@Composable
fun Arrow(
    direction: ArrowDirection,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current
) {
    Icon(
        imageVector = arrowVectorResource(direction),
        contentDescription = null,
        modifier = modifier,
        tint = tint
    )
}

@Composable
fun arrowVectorResource(direction: ArrowDirection): ImageVector {
    return remember(direction) {
        ImageVector.Builder(defaultWidth = 24.dp, defaultHeight = 12.dp, viewportWidth = 2f, viewportHeight = 2f)
            .addPath(
                pathData = when (direction) {
                    ArrowDirection.UP -> PathBuilder()
                        .moveTo(0f, 2f)
                        .lineTo(1f, 0f)
                        .lineTo(2f, 2f)
                        .close()
                        .getNodes()
                    ArrowDirection.DOWN -> PathBuilder()
                        .moveTo(0f, 0f)
                        .lineTo(2f, 0f)
                        .lineTo(1f, 2f)
                        .close()
                        .getNodes()
                    ArrowDirection.LEFT -> PathBuilder()
                        .moveTo(2f, 0f)
                        .lineTo(0f, 1f)
                        .lineTo(2f, 2f)
                        .close()
                        .getNodes()
                    ArrowDirection.RIGHT -> PathBuilder()
                        .moveTo(0f, 0f)
                        .lineTo(2f, 1f)
                        .lineTo(0f, 2f)
                        .close()
                        .getNodes()
                },
                fill = SolidColor(Color.Black)
            )
            .build()
    }
}
