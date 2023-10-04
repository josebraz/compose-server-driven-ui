import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.josebraz.serverdrivenui.core.action.Action
import com.josebraz.serverdrivenui.core.action.CalcContextMap
import com.josebraz.serverdrivenui.core.action.CommandExecutor
import com.josebraz.serverdrivenui.core.action.CommandParser
import com.josebraz.serverdrivenui.core.action.DialogAction
import com.josebraz.serverdrivenui.core.action.Operand
import com.josebraz.serverdrivenui.core.components.Box
import com.josebraz.serverdrivenui.core.components.BoxLayout
import com.josebraz.serverdrivenui.core.components.Button
import com.josebraz.serverdrivenui.core.components.ButtonView
import com.josebraz.serverdrivenui.core.components.Column
import com.josebraz.serverdrivenui.core.components.ColumnLayout
import com.josebraz.serverdrivenui.core.components.Component
import com.josebraz.serverdrivenui.core.components.EmptyComponent
import com.josebraz.serverdrivenui.core.components.RowLayout
import com.josebraz.serverdrivenui.core.components.Scaffold
import com.josebraz.serverdrivenui.core.components.ScaffoldLayout
import com.josebraz.serverdrivenui.core.components.StateHolder
import com.josebraz.serverdrivenui.core.components.Text
import com.josebraz.serverdrivenui.core.components.TextField
import com.josebraz.serverdrivenui.core.components.TextFieldView
import com.josebraz.serverdrivenui.core.components.TextView
import com.josebraz.serverdrivenui.core.components.TopBar
import com.josebraz.serverdrivenui.core.components.TopBarComponent
import com.josebraz.serverdrivenui.core.model.Alignment
import com.josebraz.serverdrivenui.core.model.AnyValue
import com.josebraz.serverdrivenui.core.model.HorizontalAlignment
import com.josebraz.serverdrivenui.core.model.dp
import com.josebraz.serverdrivenui.core.model.toAnyValues
import com.josebraz.serverdrivenui.core.model.toOperand
import com.josebraz.serverdrivenui.core.modifier.fillMaxWidth
import com.josebraz.serverdrivenui.core.modifier.padding
import com.josebraz.serverdrivenui.core.modifier.size
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import test.doTest
import com.josebraz.serverdrivenui.core.modifier.Modifier as ServerDrivenModifier

val JsonScreen = run {
    StateHolder(
        states = mapOf(
            "login_value" to "",
            "password_value" to ""
        ).toAnyValues()
    ) {
        Scaffold(
            topBar = {
                TopBar {
                    Text(text = "Login")
                }
            }
        ) {
            Column(
                modifier = ServerDrivenModifier.fillMaxWidth(),
                horizontalAlignment = HorizontalAlignment.CenterHorizontally
            ) {
                Text(text = "Login")
                TextField(
                    text = "\$login_value",
                    onChange = "login_value = arg0"
                )
                Box(modifier = ServerDrivenModifier.size(height = 20.dp))
                Text(text = "Password")
                TextField(
                    text = "\$password_value",
                    onChange = "password_value = arg0"
                )
                Box(modifier = ServerDrivenModifier.size(height = 20.dp))
                Box(
                    modifier = ServerDrivenModifier
                        .fillMaxWidth()
                        .padding(right = 16.dp)
                ) {
                    Text(
                        text = "Remember me?",
                        modifier = ServerDrivenModifier.align(Alignment.BottomEnd)
                    )
                }
                Button(
                    action = Action.Request.Post(
                        path = "/login",
                        body = "{ \"username\": \"\$login_value\", \"password\": \"\$password_value\" }",
                        onSuccess = Action.Navigate("/logged"),
                        onError = Action.ShowDialog(
                            title = "Login error",
                            actions = listOf(
                                DialogAction(
                                    title = "OK",
                                    action = Action.CloseDialog
                                )
                            )
                        )
                    )
                ) {
                    Text(text = "Send")
                }
            }
        }
    }.let {
        Json.encodeToString(it)
    }
}

typealias AnyScope = Any

@Composable
fun App() {
    MaterialTheme {
        val screenModel = remember {
            Json.decodeFromString<StateHolder>(JsonScreen)
        }
        StateHolderComposable(screenModel)
    }
}

@Composable
fun AnyScope.ComponentComposable(
    component: Component,
    state: MutableMap<String, AnyValue>
) {
    when (component) {
        EmptyComponent -> { }
        is ScaffoldLayout -> ScaffoldComposable(component, state)
        is BoxLayout -> BoxComposable(component, state)
        is ColumnLayout -> ColumnComposable(component, state)
        is RowLayout -> RowComposable(component, state)
        is TopBarComponent -> TopBarComposable(component, state)
        is ButtonView -> ButtonComposable(component, state)
        is TextFieldView -> TextFieldComposable(component, state)
        is TextView -> TextComposable(component, state)
        is StateHolder -> StateHolderComposable(component)
    }
}

@Composable
fun StateHolderComposable(
    stateHolder: StateHolder
) {
    val state = remember(stateHolder.states) {
        mutableStateMapOf<String, AnyValue>(
            *stateHolder.states.entries.map { it.key to it.value }.toTypedArray()
        )
    }

    Box(modifier = stateHolder.modifier.toCompose()) {
        BoxComposable(
            component = BoxLayout(children = stateHolder.children),
            state = state
        )
    }
}

@Composable
fun AnyScope.ScaffoldComposable(
    scaffold: ScaffoldLayout,
    state: MutableMap<String, AnyValue>
) {
    Scaffold(
        topBar = {
            TopBarComposable(scaffold.topbar, state)
        },
        modifier = scaffold.modifier.toCompose()
    ) {
        Box(modifier = Modifier.padding(it)) {
            BoxComposable(
                component = BoxLayout(children = scaffold.children),
                state = state
            )
        }
    }
}

@Composable
fun AnyScope.TopBarComposable(
    topBarComponent: TopBarComponent,
    state: MutableMap<String, AnyValue>
) {
    TopAppBar(
        modifier = topBarComponent.modifier.toCompose()
    ) {
        RowComposable(
            component = RowLayout(children = topBarComponent.children),
            state = state
        )
    }
}

@Composable
fun AnyScope.RowComposable(
    component: RowLayout,
    state: MutableMap<String, AnyValue>
) {
    Row(
        modifier = component.modifier.toCompose(),
        horizontalArrangement = component.horizontalArrangement.toCompose(),
        verticalAlignment = component.verticalAlignment.toCompose()
    ) {
        component.children.forEach {
            ComponentComposable(it, state)
        }
    }
}


@Composable
fun AnyScope.ColumnComposable(
    component: ColumnLayout,
    state: MutableMap<String, AnyValue>
) {
    Column(
        modifier = component.modifier.toCompose(),
        verticalArrangement = component.verticalArrangement.toCompose(),
        horizontalAlignment = component.horizontalAlignment.toCompose()
    ) {
        component.children.forEach {
            ComponentComposable(it, state)
        }
    }
}

@Composable
fun AnyScope.BoxComposable(
    component: BoxLayout,
    state: MutableMap<String, AnyValue>
) {
    Box(
        modifier = component.modifier.toCompose(),
        contentAlignment = component.contentAlignment.toCompose()
    ) {
        component.children.forEach {
            ComponentComposable(it, state)
        }
    }
}

@Composable
fun AnyScope.ButtonComposable(
    component: ButtonView,
    state: MutableMap<String, AnyValue>
) {
    Button(
        onClick = { },
        modifier = component.modifier.toCompose()
    ) {
        RowComposable(
            component = RowLayout(children = component.body),
            state = state
        )
    }
}

@Composable
fun AnyScope.TextFieldComposable(
    component: TextFieldView,
    state: MutableMap<String, AnyValue>
) {
    val executorValue = Executor('\"' + component.text + '\"', state)
    val executorOnChangeValue = Executor(component.onChange, state)
    TextField(
        value = executorValue.execute().toString(),
        modifier = component.modifier.toCompose(),
        onValueChange = { newValue ->
            executorOnChangeValue.execute(newValue.toOperand())
        }
    )
}

@Composable
fun AnyScope.TextComposable(
    component: TextView,
    state: MutableMap<String, AnyValue>
) {
    val executorText = Executor('\"' + component.text + '\"', state)
    Text(
        text = executorText.execute().toString(),
        modifier = component.modifier.toCompose(this)
    )
}

class ExecutorState(
    command: String,
    state: MutableMap<String, AnyValue>
) {
    private val ast by lazy { CommandParser().parse(command) }
    private val executor = CommandExecutor()
    private val context by lazy { CalcContextMap.wrap(state) }
    
    @Stable
    fun execute(vararg args: Operand) = AnyValue.fromAny(executor.execute(ast, context, *args))
}

@Composable
fun Executor(
    command: String, 
    state: MutableMap<String, AnyValue>
): ExecutorState = remember(command) {
    ExecutorState(command, state)
}

expect fun getPlatformName(): String