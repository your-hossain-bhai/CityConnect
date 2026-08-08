package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.database.HabitLog
import com.example.ui.viewmodel.Challenge
import com.example.ui.viewmodel.CityViewModel
import com.example.ui.viewmodel.HabitPreset
import com.example.ui.viewmodel.WeeklyHabitStats

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EcoDashboardScreen(
    viewModel: CityViewModel,
    modifier: Modifier = Modifier
) {
    val ecoScoreData by viewModel.ecoScoreState.collectAsStateWithLifecycle()
    val completedChallenges by viewModel.completedChallenges.collectAsStateWithLifecycle()
    val habitLogs by viewModel.habitLogs.collectAsStateWithLifecycle()
    val weeklyHabitStats by viewModel.weeklyHabitStats.collectAsStateWithLifecycle()
    val aqi by viewModel.aqiFlow.collectAsStateWithLifecycle()
    val temp by viewModel.weatherTempFlow.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp)
    ) {
        // Welcome Header
        item {
            Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                Text(
                    text = "Welcome, Earth Guardian",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        letterSpacing = 0.5.sp
                    )
                )
                Text(
                    text = "Every small action counts toward UN SDG 11, 12, & 13 goals.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            }
        }

        // 1. Eco-Score & Level Header (Natural Tones Polished Card)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("eco_score_card")
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(32.dp)
                    ),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    // Decorative organic background circle overlapping on top-right corner
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = 40.dp, y = (-40).dp)
                            .size(140.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                                shape = CircleShape
                            )
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "ECO SCORE",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary,
                                        letterSpacing = 1.5.sp
                                    )
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.Bottom) {
                                    Text(
                                        text = "${ecoScoreData.score}",
                                        style = MaterialTheme.typography.headlineLarge.copy(
                                            fontWeight = FontWeight.Black,
                                            fontSize = 48.sp,
                                            color = MaterialTheme.colorScheme.onBackground
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "pts",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        ),
                                        modifier = Modifier.padding(bottom = 6.dp)
                                    )
                                }
                            }

                            // Badge indicating Level, styled like the top 5% badge in design
                            Box(
                                modifier = Modifier
                                    .background(
                                        MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
                                        shape = RoundedCornerShape(20.dp)
                                    )
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = ecoScoreData.level.uppercase(),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                )
                            }
                        }

                        // Progress Indicator to next level
                        val progressAnimated by animateFloatAsState(targetValue = ecoScoreData.nextLevelProgress)
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            LinearProgressIndicator(
                                progress = { progressAnimated },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(CircleShape),
                                color = MaterialTheme.colorScheme.primary,
                                trackColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f)
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Progress to Next Level",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                                )
                                Text(
                                    text = "${(ecoScoreData.nextLevelProgress * 100).toInt()}%",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }

                        HorizontalDivider(color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f))

                        // Stats counters
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Co2,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = String.format("%.1f kg", ecoScoreData.carbonSavedKg),
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                                Text(
                                    text = "Carbon Saved",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                                )
                            }

                            VerticalDivider(
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
                                modifier = Modifier.height(36.dp)
                            )

                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${ecoScoreData.challengesCompletedCount}",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                                Text(
                                    text = "Tasks Done",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                                )
                            }

                            VerticalDivider(
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
                                modifier = Modifier.height(36.dp)
                            )

                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Map,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${ecoScoreData.routesCount}",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                                Text(
                                    text = "Eco Routes",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                                )
                            }
                        }
                    }
                }
            }
        }

        // 2. Mocked Air Quality (AQI) Widget
        item {
            val goodColor = MaterialTheme.colorScheme.primary
            val moderateColor = Color(0xFFF9A825)
            val unhealthyColor = Color(0xFFC62828)

            val (aqiCategory, aqiColor, aqiAdvice) = when {
                aqi <= 50 -> Triple("Good", goodColor, "Air quality is excellent. Great day for outdoor biking and walking! (SDG 11)")
                aqi <= 100 -> Triple("Moderate", moderateColor, "Moderate air quality. Safe for normal outdoor activities.")
                else -> Triple("Unhealthy", unhealthyColor, "Active urban smog. Public transit with filtered air is recommended.")
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("aqi_card")
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(24.dp)
                    ),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Circle displaying AQI value
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(aqiColor.copy(alpha = 0.12f), shape = CircleShape)
                            .border(2.dp, aqiColor, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "$aqi",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                                color = aqiColor
                            )
                            Text(
                                text = "AQI",
                                style = MaterialTheme.typography.labelSmall,
                                color = aqiColor
                            )
                        }
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "Air Quality:",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = aqiCategory,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.ExtraBold),
                                color = aqiColor
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = "$temp°C",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                            )
                        }
                        Text(
                            text = aqiAdvice,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }

        // 3. Sustainable Habit Tracker (Weekly Impact & Log)
        item {
            HabitTrackerSection(
                viewModel = viewModel,
                weeklyStats = weeklyHabitStats,
                habitLogs = habitLogs
            )
        }

        // 4. Daily Sustainability Challenges Label
        item {
            Text(
                text = "Daily Green Action Challenges",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        // List of Challenges
        items(viewModel.dailyChallenges) { challenge ->
            val isCompleted = completedChallenges.any { it.challengeId == challenge.id }
            val challengeCountOfThisType = completedChallenges.count { it.challengeId == challenge.id }

            ChallengeItem(
                challenge = challenge,
                isCompleted = isCompleted,
                completedTimes = challengeCountOfThisType,
                onComplete = { viewModel.completeChallenge(challenge) }
            )
        }
    }
}

@Composable
fun ChallengeItem(
    challenge: Challenge,
    isCompleted: Boolean,
    completedTimes: Int,
    onComplete: () -> Unit
) {
    val icon = when (challenge.iconType) {
        "transit" -> Icons.Default.DirectionsTransit
        "recycle" -> Icons.Default.Autorenew
        "energy" -> Icons.Default.Bolt
        "nature" -> Icons.Default.LocalFlorist
        else -> Icons.Default.Star
    }

    val iconColor = when (challenge.iconType) {
        "transit" -> MaterialTheme.colorScheme.primary
        "recycle" -> MaterialTheme.colorScheme.secondary
        "energy" -> Color(0xFFFFB703)
        "nature" -> Color(0xFF2D6A4F)
        else -> MaterialTheme.colorScheme.tertiary
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("challenge_item_${challenge.id}")
            .animateContentSize()
            .border(
                width = 1.dp,
                color = if (isCompleted) MaterialTheme.colorScheme.primary.copy(alpha = 0.3f) else MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(20.dp)
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isCompleted) {
                MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(iconColor.copy(alpha = 0.12f), shape = RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = challenge.title,
                    tint = iconColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = challenge.title,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = if (isCompleted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                    Badge(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ) {
                        Text(
                            text = "+${challenge.points} pts",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
                Text(
                    text = challenge.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.padding(top = 2.dp)
                )
                Text(
                    text = challenge.category,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(top = 4.dp)
                )
                if (completedTimes > 0) {
                    Text(
                        text = "Completed $completedTimes ${if (completedTimes == 1) "time" else "times"} today",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }

            IconButton(
                onClick = onComplete,
                modifier = Modifier
                    .size(40.dp)
                    .testTag("complete_challenge_button_${challenge.id}"),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = if (isCompleted) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f)
                    },
                    contentColor = if (isCompleted) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.onSecondaryContainer
                    }
                )
            ) {
                Icon(
                    imageVector = if (isCompleted) Icons.Default.Check else Icons.Default.AddCircleOutline,
                    contentDescription = "Complete action"
                )
            }
        }
    }
}

@Composable
fun HabitTrackerSection(
    viewModel: CityViewModel,
    weeklyStats: WeeklyHabitStats,
    habitLogs: List<HabitLog>,
    modifier: Modifier = Modifier
) {
    var showCustomDialog by remember { mutableStateOf(false) }
    var showGoalDialog by remember { mutableStateOf(false) }
    var showLoggedHistory by remember { mutableStateOf(false) }
    var lastLoggedMessage by remember { mutableStateOf<String?>(null) }

    val animatedProgress by animateFloatAsState(
        targetValue = weeklyStats.progressRatio,
        label = "weeklyProgress"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("habit_tracker_card")
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                shape = RoundedCornerShape(28.dp)
            ),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .background(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Eco,
                            contentDescription = "Habit Tracker",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "Habit Tracker",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "Log daily sustainable activities",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        )
                    }
                }

                // Edit Goal Button
                IconButton(
                    onClick = { showGoalDialog = true },
                    modifier = Modifier.testTag("edit_weekly_goal_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = "Adjust Goal",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            // Weekly Environmental Impact Progress
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Weekly Environmental Impact",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "${(weeklyStats.progressRatio * 100).toInt()}% of goal",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.ExtraBold),
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(CircleShape)
                        .testTag("habit_weekly_progress_bar"),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = String.format("%.1f / %.1f kg CO₂ saved", weeklyStats.totalLoggedCO2Kg, weeklyStats.weeklyGoalKgCO2),
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "${weeklyStats.habitCountThisWeek} activities • +${weeklyStats.totalLoggedPoints} pts",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                    )
                }
            }

            // Weekly Day-by-Day Impact Bar Chart Component
            WeeklyHabitBarChart(
                weeklyStats = weeklyStats,
                habitLogs = habitLogs
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            // Log Daily Activity - Quick Presets Grid
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Log Daily Activity",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    TextButton(
                        onClick = { showCustomDialog = true },
                        modifier = Modifier.testTag("log_custom_habit_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Custom Activity", style = MaterialTheme.typography.labelMedium)
                    }
                }

                // Preset Chips Grid
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    val presets = viewModel.habitPresets
                    presets.chunked(2).forEach { rowPresets ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            rowPresets.forEach { preset ->
                                QuickHabitChip(
                                    preset = preset,
                                    onClick = {
                                        viewModel.logHabit(
                                            type = preset.habitType,
                                            title = preset.defaultTitle,
                                            co2SavedKg = preset.co2SavedKg,
                                            points = preset.points
                                        )
                                        lastLoggedMessage = "Logged ${preset.habitType} (+${preset.co2SavedKg} kg CO₂)"
                                    },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            if (rowPresets.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }

            // Notification Banner when habit is logged
            AnimatedVisibility(visible = lastLoggedMessage != null) {
                lastLoggedMessage?.let { msg ->
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = msg,
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                            IconButton(
                                onClick = { lastLoggedMessage = null },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Dismiss",
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Recent Logged Activity History Toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showLoggedHistory = !showLoggedHistory }
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Activity History (${habitLogs.size})",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
                Icon(
                    imageVector = if (showLoggedHistory) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "Toggle History",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            if (showLoggedHistory) {
                if (habitLogs.isEmpty()) {
                    Text(
                        text = "No sustainable habits logged yet. Tap any activity button above to log your impact!",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        habitLogs.take(8).forEach { log ->
                            HabitHistoryItem(
                                log = log,
                                onDelete = { viewModel.deleteHabitLog(log) }
                            )
                        }
                    }
                }
            }
        }
    }

    // Custom Habit Dialog
    if (showCustomDialog) {
        CustomHabitDialog(
            onDismiss = { showCustomDialog = false },
            onConfirm = { type, title, co2, pts, day ->
                viewModel.logHabit(type, title, co2, pts, day)
                lastLoggedMessage = "Logged $title (+${co2} kg CO₂)"
                showCustomDialog = false
            }
        )
    }

    // Edit Weekly Goal Dialog
    if (showGoalDialog) {
        EditWeeklyGoalDialog(
            currentGoal = weeklyStats.weeklyGoalKgCO2,
            onDismiss = { showGoalDialog = false },
            onConfirm = { newGoal ->
                viewModel.updateWeeklyGoal(newGoal)
                showGoalDialog = false
            }
        )
    }
}

@Composable
fun QuickHabitChip(
    preset: HabitPreset,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier
            .testTag("quick_habit_chip_${preset.habitType.lowercase().replace(" ", "_")}"),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val icon = when (preset.iconType) {
                "bike" -> Icons.Default.DirectionsBike
                "transit" -> Icons.Default.DirectionsBus
                "recycle" -> Icons.Default.Recycling
                "food" -> Icons.Default.Restaurant
                "flash" -> Icons.Default.Bolt
                else -> Icons.Default.Eco
            }

            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = preset.habitType,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = preset.habitType,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = "+${preset.co2SavedKg} kg CO₂",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun HabitHistoryItem(
    log: HabitLog,
    onDelete: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("habit_log_item_${log.id}"),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                val icon = when (log.habitType.lowercase()) {
                    "biking" -> Icons.Default.DirectionsBike
                    "public transport", "transit" -> Icons.Default.DirectionsBus
                    "recycling" -> Icons.Default.Recycling
                    "plant-based meal", "food" -> Icons.Default.Restaurant
                    "energy conservation", "power" -> Icons.Default.Bolt
                    else -> Icons.Default.Eco
                }

                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )

                Column {
                    Text(
                        text = log.activityName,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${log.dayOfWeek} • ${log.habitType}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Badge(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) {
                    Text(
                        text = "+${log.co2SavedKg} kg CO₂",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(28.dp).testTag("delete_habit_log_${log.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete entry",
                        tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomHabitDialog(
    onDismiss: () -> Unit,
    onConfirm: (type: String, title: String, co2: Double, pts: Int, day: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("Biking") }
    var co2Saved by remember { mutableStateOf("1.5") }
    var points by remember { mutableStateOf("25") }
    var selectedDay by remember { mutableStateOf("Mon") }

    val categories = listOf("Biking", "Public Transport", "Recycling", "Plant-Based Meal", "Energy Conservation", "Composting", "Other")
    val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Log Custom Activity", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Activity Description") },
                    placeholder = { Text("e.g., Biked 8km to market") },
                    modifier = Modifier.fillMaxWidth().testTag("custom_habit_title_input"),
                    singleLine = true
                )

                Text("Activity Category", style = MaterialTheme.typography.labelMedium)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    categories.take(3).forEach { cat ->
                        FilterChip(
                            selected = selectedType == cat,
                            onClick = { selectedType = cat },
                            label = { Text(cat.take(8), style = MaterialTheme.typography.labelSmall) }
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = co2Saved,
                        onValueChange = { co2Saved = it },
                        label = { Text("CO₂ Saved (kg)") },
                        modifier = Modifier.weight(1f).testTag("custom_habit_co2_input"),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = points,
                        onValueChange = { points = it },
                        label = { Text("Points") },
                        modifier = Modifier.weight(1f).testTag("custom_habit_points_input"),
                        singleLine = true
                    )
                }

                Text("Day of Week", style = MaterialTheme.typography.labelMedium)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    days.forEach { day ->
                        FilterChip(
                            selected = selectedDay == day,
                            onClick = { selectedDay = day },
                            label = { Text(day, style = MaterialTheme.typography.labelSmall) },
                            modifier = Modifier.padding(horizontal = 1.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        val co2Val = co2Saved.toDoubleOrNull() ?: 1.0
                        val ptsVal = points.toIntOrNull() ?: 20
                        onConfirm(selectedType, title, co2Val, ptsVal, selectedDay)
                    }
                },
                modifier = Modifier.testTag("submit_custom_habit_button")
            ) {
                Text("Log Activity")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun EditWeeklyGoalDialog(
    currentGoal: Double,
    onDismiss: () -> Unit,
    onConfirm: (Double) -> Unit
) {
    var goalText by remember { mutableStateOf(currentGoal.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Set Weekly CO₂ Goal (kg)") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Set your target environmental impact for this week:", style = MaterialTheme.typography.bodyMedium)
                OutlinedTextField(
                    value = goalText,
                    onValueChange = { goalText = it },
                    label = { Text("Target kg CO₂") },
                    modifier = Modifier.fillMaxWidth().testTag("weekly_goal_input"),
                    singleLine = true
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                ) {
                    listOf(5.0, 10.0, 15.0, 20.0).forEach { preset ->
                        OutlinedButton(
                            onClick = { goalText = preset.toString() },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("${preset.toInt()} kg", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val g = goalText.toDoubleOrNull()
                    if (g != null && g > 0) {
                        onConfirm(g)
                    }
                },
                modifier = Modifier.testTag("save_weekly_goal_button")
            ) {
                Text("Save Goal")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeeklyHabitBarChart(
    weeklyStats: WeeklyHabitStats,
    habitLogs: List<HabitLog>,
    modifier: Modifier = Modifier
) {
    var selectedMetric by remember { mutableStateOf("co2") } // "co2", "pts", "count"
    var selectedDay by remember { mutableStateOf<String?>(null) }

    val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

    val maxVal = when (selectedMetric) {
        "co2" -> (weeklyStats.dailyBreakdown.values.maxOrNull() ?: 1.0).coerceAtLeast(2.0)
        "pts" -> (weeklyStats.dailyPointsBreakdown.values.maxOrNull()?.toDouble() ?: 10.0).coerceAtLeast(30.0)
        else -> (weeklyStats.dailyCountBreakdown.values.maxOrNull()?.toDouble() ?: 1.0).coerceAtLeast(3.0)
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Weekly Breakdown",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )

            // Metric Toggle Chips
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                listOf("co2" to "CO₂", "pts" to "Points", "count" to "Count").forEach { (key, label) ->
                    FilterChip(
                        selected = selectedMetric == key,
                        onClick = {
                            selectedMetric = key
                            selectedDay = null
                        },
                        label = { Text(label, style = MaterialTheme.typography.labelSmall) },
                        modifier = Modifier.height(28.dp).testTag("chart_metric_${key}")
                    )
                }
            }
        }

        // Bar Chart Box
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("weekly_habits_bar_chart"),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Chart Bars Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        days.forEach { day ->
                            val value = when (selectedMetric) {
                                "co2" -> weeklyStats.dailyBreakdown[day] ?: 0.0
                                "pts" -> (weeklyStats.dailyPointsBreakdown[day] ?: 0).toDouble()
                                else -> (weeklyStats.dailyCountBreakdown[day] ?: 0).toDouble()
                            }

                            val ratio = (value / maxVal).toFloat().coerceIn(0.06f, 1f)
                            val isSelected = selectedDay == day

                            val animatedRatio by animateFloatAsState(
                                targetValue = ratio,
                                label = "barHeightRatio_$day"
                            )

                            val barColor = if (isSelected) {
                                MaterialTheme.colorScheme.primary
                            } else if (value > 0) {
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.75f)
                            } else {
                                MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                            }

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Bottom,
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .clickable {
                                        selectedDay = if (isSelected) null else day
                                    }
                                    .testTag("bar_day_$day")
                            ) {
                                // Value label above bar
                                if (value > 0) {
                                    val valText = if (selectedMetric == "co2") String.format("%.1f", value) else value.toInt().toString()
                                    Text(
                                        text = valText,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 9.sp,
                                            fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Bold
                                        ),
                                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                    )
                                } else {
                                    Spacer(modifier = Modifier.height(12.dp))
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                // Bar Pillar
                                Box(
                                    modifier = Modifier
                                        .width(if (isSelected) 22.dp else 16.dp)
                                        .fillMaxHeight(animatedRatio * 0.72f)
                                        .background(
                                            color = barColor,
                                            shape = RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp)
                                        )
                                        .border(
                                            width = if (isSelected) 2.dp else 0.dp,
                                            color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                                            shape = RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp)
                                        )
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                // Day Label
                                Text(
                                    text = day,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 10.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                    ),
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                }

                // Interactive Day Details Panel when a day bar is clicked
                selectedDay?.let { day ->
                    val dayLogs = habitLogs.filter { it.dayOfWeek.equals(day, ignoreCase = true) }
                    val dayCO2 = weeklyStats.dailyBreakdown[day] ?: 0.0
                    val dayPts = weeklyStats.dailyPointsBreakdown[day] ?: 0

                    Surface(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("bar_chart_day_details_$day")
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "$day Impact Breakdown",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "${String.format("%.1f", dayCO2)} kg CO₂ • +$dayPts pts",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                            if (dayLogs.isEmpty()) {
                                Text(
                                    text = "No activities logged on $day.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            } else {
                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    dayLogs.forEach { log ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = Icons.Default.CheckCircle,
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.primary,
                                                    modifier = Modifier.size(14.dp)
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = log.activityName,
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                            }
                                            Text(
                                                text = "+${log.co2SavedKg} kg",
                                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
