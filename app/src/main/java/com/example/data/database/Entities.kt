package com.example.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "civic_issues")
data class CivicIssue(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val category: String,
    val description: String,
    val location: String,
    val photoUri: String?,
    val status: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "completed_challenges")
data class CompletedChallenge(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val challengeId: String,
    val title: String,
    val points: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "saved_routes")
data class SavedRoute(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val origin: String,
    val destination: String,
    val mode: String, // "Transit", "Bicycle", "Walking"
    val carbonSaved: Double, // in kg CO2
    val distanceKm: Double,
    val timestamp: Long = System.currentTimeMillis()
)
