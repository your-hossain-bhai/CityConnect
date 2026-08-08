package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Eco
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.example.data.database.AppDatabase
import com.example.data.repository.CityRepository
import com.example.ui.screens.CivicFixItScreen
import com.example.ui.screens.CommunityHubScreen
import com.example.ui.screens.EcoDashboardScreen
import com.example.ui.screens.GreenTransitScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.CityViewModel
import com.example.ui.viewmodel.CityViewModelFactory

enum class ScreenTab {
    DASHBOARD,
    CIVIC,
    TRANSIT,
    COMMUNITY
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Room database initialization
        val database = AppDatabase.getDatabase(applicationContext)
        val repository = CityRepository(database.cityConnectDao())
        val factory = CityViewModelFactory(repository)
        val viewModel = ViewModelProvider(this, factory)[CityViewModel::class.java]

        setContent {
            MyApplicationTheme {
                CityConnectApp(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityConnectApp(viewModel: CityViewModel) {
    var currentTab by remember { mutableStateOf(ScreenTab.DASHBOARD) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Urban Companion",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Medium,
                            letterSpacing = 1.2.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "CityConnect",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = (-0.5).sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    )
                }

                // Profile Avatar Circular Badge representing "JD"
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "JD",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .testTag("bottom_nav_bar")
                    .windowInsetsPadding(WindowInsets.navigationBars),
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                // Dashboard Tab
                NavigationBarItem(
                    selected = currentTab == ScreenTab.DASHBOARD,
                    onClick = { currentTab = ScreenTab.DASHBOARD },
                    modifier = Modifier.testTag("dashboard_tab_button"),
                    icon = {
                        Icon(
                            imageVector = if (currentTab == ScreenTab.DASHBOARD) Icons.Filled.Eco else Icons.Outlined.Eco,
                            contentDescription = "Eco Dashboard"
                        )
                    },
                    label = { Text("Eco-Score") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )

                // Civic Fix-It Tab
                NavigationBarItem(
                    selected = currentTab == ScreenTab.CIVIC,
                    onClick = { currentTab = ScreenTab.CIVIC },
                    modifier = Modifier.testTag("civic_tab_button"),
                    icon = {
                        Icon(
                            imageVector = if (currentTab == ScreenTab.CIVIC) Icons.Filled.Build else Icons.Outlined.Build,
                            contentDescription = "Civic Fix-it"
                        )
                    },
                    label = { Text("Civic Fix-It") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )

                // Transit Tab
                NavigationBarItem(
                    selected = currentTab == ScreenTab.TRANSIT,
                    onClick = { currentTab = ScreenTab.TRANSIT },
                    modifier = Modifier.testTag("transit_tab_button"),
                    icon = {
                        Icon(
                            imageVector = if (currentTab == ScreenTab.TRANSIT) Icons.Filled.Map else Icons.Outlined.Map,
                            contentDescription = "Green Transit Map"
                        )
                    },
                    label = { Text("Transit") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )

                // Community Tab
                NavigationBarItem(
                    selected = currentTab == ScreenTab.COMMUNITY,
                    onClick = { currentTab = ScreenTab.COMMUNITY },
                    modifier = Modifier.testTag("community_tab_button"),
                    icon = {
                        Icon(
                            imageVector = if (currentTab == ScreenTab.COMMUNITY) Icons.Filled.Groups else Icons.Outlined.Groups,
                            contentDescription = "Community Hub"
                        )
                    },
                    label = { Text("Community") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                ScreenTab.DASHBOARD -> EcoDashboardScreen(viewModel = viewModel)
                ScreenTab.CIVIC -> CivicFixItScreen(viewModel = viewModel)
                ScreenTab.TRANSIT -> GreenTransitScreen(viewModel = viewModel)
                ScreenTab.COMMUNITY -> CommunityHubScreen(viewModel = viewModel)
            }
        }
    }
}
