package com.example.canvastodo

import android.graphics.drawable.Icon
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.canvastodo.ui.theme.CanvasTODOTheme
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.foundation.layout.*
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.IntSize
import androidx.compose.foundation.gestures.*
import androidx.compose.ui.input.pointer.util.VelocityTracker
import kotlin.math.abs

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CanvasTODOTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.primary
                ) {
//                    Greeting("Android")
//                    Clock(()  // 确保可以调用Clock()
//                    Text_0()
                }

                // 使用 Material 3 主题
                MaterialTheme(
                    colorScheme = lightColorScheme(),
                    typography = Typography()
                ) {
                    MainScreen()
                }
            }
        }
    }
}

fun getCurrentTime(): String {
    val dateFormat = SimpleDateFormat("HH\nmm", Locale.getDefault())
    return dateFormat.format(Calendar.getInstance().time)
}

fun getCurrentDate(): String {
    val dateFormat = SimpleDateFormat("yyy\nMM.dd", Locale.getDefault())
    return dateFormat.format((Calendar.getInstance().time))
}

// 将 Greeting 函数放在 MainActivity 类外部
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}


@Composable
fun Clock() {
    val currentTime = remember { mutableStateOf(getCurrentTime()) }
    val currentDate = remember { mutableStateOf(getCurrentDate()) }

    LaunchedEffect(Unit) {
        while (true) {
            currentTime.value = getCurrentTime()
            currentDate.value = getCurrentDate()
            delay(1000) // 每秒更新一次
        }
    }

    val surfaceSize = remember { mutableStateOf(IntSize.Zero) }

    var dragOffset by remember { mutableStateOf(0f) }  // 记录滑动偏移量
    var scale by remember { mutableStateOf(1f) }  // 控制缩放
    var timePosition by remember { mutableStateOf(0f) }  // 控制垂直偏移

    // 动态计算缩放值
    val animatedScale by animateFloatAsState(targetValue = scale, label = "")
    // 动态计算垂直偏移
    val animatedPosition by animateFloatAsState(targetValue = timePosition, label = "")

    // 用于跟踪滑动速度
    var velocityTracker = remember { VelocityTracker() }
    var isDragging by remember { mutableStateOf(false) }
    var velocity = remember { 0f }

    Surface(
        color = MaterialTheme.colorScheme.primary.copy(alpha = .08f),
        modifier = Modifier
            .fillMaxSize()
            .fillMaxHeight()
//            .onGloballyPositioned { layoutCoordinates ->
//                surfaceSize.value = layoutCoordinates.size
//            }   // 用于获取尺寸
//            .wrapContentSize(Alignment.TopCenter)
            .pointerInput(Unit) {
                detectVerticalDragGestures(
//                    onDragStart = {
//                        isDragging = true // 开始拖动
//                    },
//                    onDragEnd = {
//                        // 结束拖动，应用惯性
//                        isDragging = false
//                        // 根据滑动的速度应用惯性
//                        velocity = velocityTracker.calculateVelocity().y
//                        // 施加惯性，基于速度进行额外滑动
//                        LaunchedEffect(velocity) {
//                            var currentVelocity = velocity
//                            while (abs(currentVelocity) > 0.5f) {
//                                dragOffset = (dragOffset + currentVelocity / 10).coerceIn(-1000f, 0f)
//                                currentVelocity *= 0.9f // 逐渐衰减速度，实现惯性减速
//                                scale = (scale + currentVelocity / 1000).coerceIn(0.5f, 1f)
//                                timePosition = dragOffset.coerceAtMost(0f)
//                                delay(16) // 模拟 60fps
//                            }
//                        }
//                    },
//                    onDragCancel = {
//                        isDragging = false
//                    }
                ) { change, dragAmount  ->
                    change.consume()    // 消耗滑动事件
                    dragOffset = (dragOffset + dragAmount).coerceIn(-1220f, 0f)
                    // 根据滑动量控制缩放和垂直移动
                    scale = (scale + dragAmount / 2100f).coerceIn(0.41f, 1f)
                    timePosition = dragOffset.coerceAtMost(0f)
                }
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth() // 填充整个可用空间
                .wrapContentHeight()
//                .padding(30.dp) // 添加一些边距
                .padding(start = 50.dp)
                .padding(top = 30.dp, bottom = 30.dp)
//                .wrapContentSize(Alignment.TopCenter)
        ) {
//            小时分钟
            Text(
                text = currentTime.value,
                modifier = Modifier
                    .padding(top = 20.dp, start = 20.dp)
                    .offset(y = animatedPosition.dp / 4f, x = animatedPosition.dp / 24f),
                textAlign = TextAlign.Left,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = 100.sp * animatedScale,  // 动态控制字体大小
                    lineHeight = 110.sp * animatedScale,
                    color = MaterialTheme.colorScheme.primary, // 使用主题的主色
                    fontFamily = FontFamily(Font(R.font.harmonyos_sans_black))
                )
            )


//            年月
            Text(
                text = currentDate.value,
                modifier = Modifier
                    .padding(20.dp)
                    .padding(start = 135.dp, bottom = 2.dp)
                    .align(Alignment.BottomStart)
                    .offset(y = (animatedPosition.dp / 3.95f), x = animatedPosition.dp / 10f),
                textAlign = TextAlign.Left,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 27.sp,
                    lineHeight = 30.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontFamily = FontFamily((Font(R.font.harmonyos_sans_black)))
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    var selectedItem by remember { mutableStateOf(0) }

    val items = listOf("Home", "Dashboard", "Notifications", "My")

    val icons = listOf(
        R.drawable.baseline_home_24, // 第一个图标
        R.drawable.baseline_space_dashboard_24, // 第二个图标
        R.drawable.baseline_notifications_24, // 第三个图标
        R.drawable.baseline_account_circle_24
    )
    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(
                            imageVector = ImageVector.vectorResource(id = icons[index]),
                            contentDescription = null,
                            modifier = Modifier.size((33.dp))
                        ) },
//                        label = { Text(
//                            item,
//                            style = MaterialTheme.typography.labelLarge.copy(
//                                fontFamily = FontFamily(Font(R.font.harmonyos_sans_medium))
//                            )
//                            ) },
                        selected = selectedItem == index,
                        onClick = { selectedItem = index }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedItem) {
                0 -> HomeScreen()
                1 -> DashboardScreen()
                2 -> NotificationsScreen()
                3 -> MyScreen()
            }
        }
    }
}

@Composable
fun HomeScreen() {

    // 创建状态来跟踪缩放比例
    var scale by remember { mutableStateOf(1f) }

    // 使用滑动手势来更新缩放比例
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.TopCenter),
//            .pointerInput(Unit) {
//                detectDragGestures { _, dragAmount ->
//                    scale = (scale + dragAmount.y / 1000).coerceIn(0.5f, 1f) // 限制缩放范围
//                }
//            },
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Clock() // 传递缩放因子
        }
    }

//// 使用 Box 让 Clock 和文本居中显示
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .wrapContentSize(Alignment.TopCenter) // 内容居中
//    ) {
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Clock() // 显示时钟
//        }
    }

@Composable
fun DashboardScreen() {
    Text(text = "Dashboard Screen")
}

@Composable
fun NotificationsScreen() {
    Text(text = "Notifications Screen")
}

@Composable
fun MyScreen() {
    Text(text = "My Screen")
}

@Composable
fun CanvasTODOTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val context = LocalContext.current

    // 使用动态颜色 (仅适用于 Android 12 及以上)
    val colorScheme = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    } else {
        if (darkTheme) darkColorScheme() else lightColorScheme()
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        content = content
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    com.example.canvastodo.CanvasTODOTheme() {
        MainScreen()
    }
}