import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment as ComposeAlignment
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.josebraz.serverdrivenui.core.model.Alignment
import com.josebraz.serverdrivenui.core.model.HorizontalAlignment
import com.josebraz.serverdrivenui.core.model.HorizontalArrangement
import com.josebraz.serverdrivenui.core.model.Size
import com.josebraz.serverdrivenui.core.model.VerticalAlignment
import com.josebraz.serverdrivenui.core.model.VerticalArrangement
import com.josebraz.serverdrivenui.core.modifier.Align
import com.josebraz.serverdrivenui.core.modifier.AlignHorizontal
import com.josebraz.serverdrivenui.core.modifier.AlignVertical
import com.josebraz.serverdrivenui.core.modifier.Element
import com.josebraz.serverdrivenui.core.modifier.FillModifier
import com.josebraz.serverdrivenui.core.modifier.Modifier
import com.josebraz.serverdrivenui.core.modifier.Padding
import com.josebraz.serverdrivenui.core.modifier.SizeModifier
import androidx.compose.ui.Modifier as ComposeModifier

@Stable
fun Modifier.toCompose(scope: AnyScope? = null): ComposeModifier {
    return this.fold<ComposeModifier>(ComposeModifier) { companion, element ->
        companion.then(toComposeElement(element, scope))
    }
}

@Stable
private fun toComposeElement(element: Element, scope: AnyScope? = null) = when (element) {
    is Padding -> ComposeModifier.padding(
        start = element.left.toComposeDp(),
        top = element.top.toComposeDp(),
        end = element.right.toComposeDp(),
        bottom = element.bottom.toComposeDp(),
    )
    is SizeModifier -> ComposeModifier.size(
        width = element.width.toComposeDp(),
        height = element.height.toComposeDp()
    )
    is FillModifier -> {
        when {
            element.width != -1F && element.height != -1F -> {
                ComposeModifier.fillMaxSize(element.width)
            }
            element.width != -1F -> ComposeModifier.fillMaxWidth(element.width)
            element.height != -1F -> ComposeModifier.fillMaxHeight(element.height)
            else -> ComposeModifier
        }
    }
    is Align -> {
        when (scope) {
            is BoxScope -> with(scope) { ComposeModifier.align(element.align.toCompose()) }
            else -> ComposeModifier
        }
    }
    is AlignHorizontal -> {
        when (scope) {
            is ColumnScope -> with(scope) { ComposeModifier.align(element.align.toCompose()) }
            else -> ComposeModifier
        }
    }
    is AlignVertical -> {
        when (scope) {
            is RowScope -> with(scope) { ComposeModifier.align(element.align.toCompose()) }
            else -> ComposeModifier
        }
    }
}

@Stable
fun Size.toComposeDp(): Dp = this.value.dp

@Stable
fun Alignment.toCompose(): ComposeAlignment {
    return when (this) {
        Alignment.TopStart -> ComposeAlignment.TopStart
        Alignment.TopCenter -> ComposeAlignment.TopCenter
        Alignment.TopEnd -> ComposeAlignment.TopEnd
        Alignment.CenterStart -> ComposeAlignment.CenterStart
        Alignment.Center -> ComposeAlignment.Center
        Alignment.CenterEnd -> ComposeAlignment.CenterEnd
        Alignment.BottomStart -> ComposeAlignment.BottomStart
        Alignment.BottomCenter -> ComposeAlignment.BottomCenter
        Alignment.BottomEnd -> ComposeAlignment.BottomEnd
    }
}

@Stable
fun VerticalAlignment.toCompose(): ComposeAlignment.Vertical {
    return when (this) {
        VerticalAlignment.Top -> ComposeAlignment.Top
        VerticalAlignment.CenterVertically -> ComposeAlignment.CenterVertically
        VerticalAlignment.Bottom -> ComposeAlignment.Bottom
    }
}

@Stable
fun HorizontalAlignment.toCompose(): ComposeAlignment.Horizontal {
    return when (this) {
        HorizontalAlignment.Start -> ComposeAlignment.Start
        HorizontalAlignment.CenterHorizontally -> ComposeAlignment.CenterHorizontally
        HorizontalAlignment.End -> ComposeAlignment.End
    }
}

@Stable
fun VerticalArrangement.toCompose(): Arrangement.Vertical {
    return when (this) {
        VerticalArrangement.Top -> Arrangement.Top
        VerticalArrangement.Bottom -> Arrangement.Bottom
        VerticalArrangement.Center -> Arrangement.Center
        VerticalArrangement.SpaceEvenly -> Arrangement.SpaceEvenly
        VerticalArrangement.SpaceBetween -> Arrangement.SpaceBetween
        VerticalArrangement.SpaceAround -> Arrangement.SpaceAround
    }
}

@Stable
fun HorizontalArrangement.toCompose(): Arrangement.Horizontal {
    return when (this) {
        HorizontalArrangement.Start -> Arrangement.Start
        HorizontalArrangement.End -> Arrangement.End
        HorizontalArrangement.Center -> Arrangement.Center
        HorizontalArrangement.SpaceEvenly -> Arrangement.SpaceEvenly
        HorizontalArrangement.SpaceBetween -> Arrangement.SpaceBetween
        HorizontalArrangement.SpaceAround -> Arrangement.SpaceAround
    }
}