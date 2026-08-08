package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.database.CivicIssue
import com.example.data.database.CompletedChallenge
import com.example.data.database.SavedRoute
import com.example.data.repository.CityRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// Challenge UI data model
data class Challenge(
    val id: String,
    val title: String,
    val description: String,
    val points: Int,
    val category: String,
    val iconType: String // "transit", "recycle", "energy", "nature"
)

// Community Event UI data model
data class CommunityEvent(
    val id: String,
    val title: String,
    val description: String,
    val date: String,
    val location: String,
    val organizer: String,
    val category: String,
    val initialAttendees: Int,
    val isUserAttending: Boolean = false
)

// Transit Route Option UI data model
data class GreenRouteOption(
    val id: String,
    val mode: String, // "Public Transit", "Bicycle", "Walking", "Gas Car (Ref)"
    val durationMinutes: Int,
    val distanceKm: Double,
    val carbonGrams: Double,
    val carbonSavedKg: Double, // compared to Gas Car (Ref)
    val recommended: Boolean = false
)

class CityViewModel(private val repository: CityRepository) : ViewModel() {

    // Daily Sustainability Challenges (Static List)
    val dailyChallenges = listOf(
        Challenge(
            "ch_1",
            "Eco-Transit Commuter",
            "Walk, bike, or take public transit for your trips today to curb CO2 emissions.",
            50,
            "SDG 13 - Climate Action",
            "transit"
        ),
        Challenge(
            "ch_2",
            "Zero Single-Use Plastic",
            "Bring your reusable grocery bags and water container today.",
            20,
            "SDG 12 - Responsible Consumption",
            "recycle"
        ),
        Challenge(
            "ch_3",
            "Compost Kitchen Scraps",
            "Separate food waste into organic composting to reduce landfill methane.",
            30,
            "SDG 12 - Responsible Consumption",
            "recycle"
        ),
        Challenge(
            "ch_4",
            "Park Steward",
            "Spend 15 minutes picking up stray litter in a local green space or sidewalk.",
            40,
            "SDG 11 - Sustainable Cities",
            "nature"
        ),
        Challenge(
            "ch_5",
            "Energy Vigilante",
            "Unplug standby electronic adapters and dim smart thermostats by 2 degrees.",
            25,
            "SDG 13 - Climate Action",
            "energy"
        )
    )

    // Air Quality Data (Mocked but interactive)
    val aqiFlow = MutableStateFlow(42) // 42 is Good
    val weatherTempFlow = MutableStateFlow(24) // 24 degrees C

    // Completed challenges list from Room
    val completedChallenges: StateFlow<List<CompletedChallenge>> = repository.allCompletedChallenges
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Civic issues reported list from Room
    val reportedIssues: StateFlow<List<CivicIssue>> = repository.allIssues
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Saved low-carbon routes list from Room
    val savedRoutes: StateFlow<List<SavedRoute>> = repository.allSavedRoutes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Dynamic Eco-Score calculations combining completed actions
    val ecoScoreState: StateFlow<EcoScoreData> = combine(
        completedChallenges,
        savedRoutes
    ) { completed, routes ->
        val challengePoints = completed.sumOf { it.points }
        val transitPoints = routes.sumOf { (it.carbonSaved * 15).toInt() }
        val totalPoints = 120 + challengePoints + transitPoints // starts at baseline 120

        // Calculate level
        val level = when {
            totalPoints >= 500 -> "Sustainability Hero"
            totalPoints >= 300 -> "Green Guardian"
            totalPoints >= 200 -> "Eco Explorer"
            else -> "Mindful Citizen"
        }

        val carbonSavedTotal = routes.sumOf { it.carbonSaved } + (completed.filter { it.challengeId == "ch_1" }.size * 2.8)

        EcoScoreData(
            score = totalPoints,
            level = level,
            carbonSavedKg = carbonSavedTotal,
            nextLevelProgress = (totalPoints % 150) / 150f,
            challengesCompletedCount = completed.size,
            routesCount = routes.size
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        EcoScoreData(120, "Mindful Citizen", 0.0, 0.8f, 0, 0)
    )

    // Community Events feed with local status
    private val _communityEvents = MutableStateFlow(
        listOf(
            CommunityEvent(
                "ev_1",
                "Riverside Park Cleanup",
                "Help restore our waterfront by cleaning litter. Grabbers, gloves, and eco-snacks provided!",
                "Saturday, July 11 • 9:00 AM",
                "Riverside Park South, Gate 3",
                "GreenCity Alliance",
                "SDG 11 - Sustainable Cities",
                34
            ),
            CommunityEvent(
                "ev_2",
                "Home Composting Workshop",
                "Transform food leftovers into hyper-fertile soil. Attendees receive a free premium counter-top compost bin.",
                "Tuesday, July 14 • 6:30 PM",
                "District Community Hall B",
                "RecycleRight Coalition",
                "SDG 12 - Responsible Consumption",
                18
            ),
            CommunityEvent(
                "ev_3",
                "Eco-Transit Town Hall",
                "Discuss upcoming expansions to city bus priority lanes and safe cycle superhighways. Make your voice heard!",
                "Thursday, July 16 • 7:00 PM",
                "City Hall Annex Room 402",
                "Metropolitan Transit Authority",
                "SDG 11 - Sustainable Cities",
                52
            ),
            CommunityEvent(
                "ev_4",
                "Oakridge Tree Planting Initiative",
                "Plant native seedlings to augment the urban canopy, suppress summer heat waves, and welcome bird biodiversity.",
                "Sunday, July 19 • 8:00 AM",
                "Oakridge Public Forest Reserve",
                "Urban Canopy Project",
                "SDG 13 - Climate Action",
                85
            )
        )
    )
    val communityEvents: StateFlow<List<CommunityEvent>> = _communityEvents

    // Transit Route planning variables and results
    val originInput = MutableStateFlow("")
    val destinationInput = MutableStateFlow("")
    private val _routeOptions = MutableStateFlow<List<GreenRouteOption>>(emptyList())
    val routeOptions: StateFlow<List<GreenRouteOption>> = _routeOptions
    val isSearchingRoutes = MutableStateFlow(false)

    // Civic Fix-It form variables
    val issueDescription = MutableStateFlow("")
    val issueCategory = MutableStateFlow("Illegal Dumping")
    val mockGpsCoordinates = MutableStateFlow("37.7749° N, 122.4194° W")
    val mockCameraCapturedUri = MutableStateFlow<String?>(null)
    val isCameraViewfinderActive = MutableStateFlow(false)

    // ----------------------------------------------------
    // Actions / Intents
    // ----------------------------------------------------

    fun completeChallenge(challenge: Challenge) {
        viewModelScope.launch {
            repository.insertCompletedChallenge(
                CompletedChallenge(
                    challengeId = challenge.id,
                    title = challenge.title,
                    points = challenge.points
                )
            )
        }
    }

    fun reportIssue() {
        if (issueDescription.value.isBlank()) return

        viewModelScope.launch {
            val newIssue = CivicIssue(
                category = issueCategory.value,
                description = issueDescription.value,
                location = mockGpsCoordinates.value,
                photoUri = mockCameraCapturedUri.value,
                status = "Submitted"
            )
            repository.insertIssue(newIssue)

            // Clear form
            issueDescription.value = ""
            mockCameraCapturedUri.value = null
        }
    }

    fun deleteIssue(issue: CivicIssue) {
        viewModelScope.launch {
            repository.deleteIssue(issue)
        }
    }

    fun captureMockPhoto() {
        // Generates a mock picture name depending on chosen issue category
        val mockName = "mock_photo_${issueCategory.value.lowercase().replace(" ", "_")}.jpg"
        mockCameraCapturedUri.value = mockName
        isCameraViewfinderActive.value = false
    }

    fun refreshGps() {
        val lat = (37.75 + Math.random() * 0.05).toString().take(7)
        val lng = (-122.45 + Math.random() * 0.05).toString().take(8)
        mockGpsCoordinates.value = "$lat° N, $lng° W"
    }

    fun searchEcoRoutes() {
        if (originInput.value.isBlank() || destinationInput.value.isBlank()) return

        viewModelScope.launch {
            isSearchingRoutes.value = true
            // Simulate brief network delay for realism
            kotlinx.coroutines.delay(800)

            // Generates route options with carbon saved metrics
            val gasCarRefCarbon = 4200.0 // grams of CO2 for a car trip of this length

            _routeOptions.value = listOf(
                GreenRouteOption(
                    "rt_1",
                    "Public Transit (Bus #28)",
                    22,
                    8.2,
                    1100.0,
                    (gasCarRefCarbon - 1100.0) / 1000.0,
                    recommended = true
                ),
                GreenRouteOption(
                    "rt_2",
                    "Bicycle (Park Route)",
                    28,
                    6.5,
                    0.0,
                    gasCarRefCarbon / 1000.0,
                    recommended = true
                ),
                GreenRouteOption(
                    "rt_3",
                    "Walking Corridor",
                    72,
                    5.8,
                    0.0,
                    gasCarRefCarbon / 1000.0
                ),
                GreenRouteOption(
                    "rt_4",
                    "Gasoline Sedan (Ref)",
                    18,
                    8.5,
                    gasCarRefCarbon,
                    0.0
                )
            )
            isSearchingRoutes.value = false
        }
    }

    fun saveTransitRoute(option: GreenRouteOption) {
        viewModelScope.launch {
            repository.insertSavedRoute(
                SavedRoute(
                    origin = originInput.value,
                    destination = destinationInput.value,
                    mode = option.mode,
                    carbonSaved = option.carbonSavedKg,
                    distanceKm = option.distanceKm
                )
            )
        }
    }

    fun deleteSavedRoute(route: SavedRoute) {
        viewModelScope.launch {
            repository.deleteSavedRoute(route)
        }
    }

    fun toggleAttendance(eventId: String) {
        _communityEvents.value = _communityEvents.value.map { event ->
            if (event.id == eventId) {
                val newStatus = !event.isUserAttending
                event.copy(
                    isUserAttending = newStatus,
                    initialAttendees = if (newStatus) event.initialAttendees + 1 else event.initialAttendees - 1
                )
            } else {
                event
            }
        }
    }

    fun addNewCommunityEvent(title: String, desc: String, date: String, loc: String, cat: String) {
        if (title.isBlank() || desc.isBlank()) return
        val newEvent = CommunityEvent(
            id = "ev_${System.currentTimeMillis()}",
            title = title,
            description = desc,
            date = if (date.isBlank()) "Tomorrow • 10:00 AM" else date,
            location = if (loc.isBlank()) "Community Center" else loc,
            organizer = "Civic User",
            category = cat,
            initialAttendees = 1,
            isUserAttending = true
        )
        _communityEvents.value = listOf(newEvent) + _communityEvents.value
    }
}

data class EcoScoreData(
    val score: Int,
    val level: String,
    val carbonSavedKg: Double,
    val nextLevelProgress: Float,
    val challengesCompletedCount: Int,
    val routesCount: Int
)

class CityViewModelFactory(private val repository: CityRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CityViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
