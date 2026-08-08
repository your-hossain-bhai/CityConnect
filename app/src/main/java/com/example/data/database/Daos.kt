package com.example.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CityConnectDao {

    // Civic Issues
    @Query("SELECT * FROM civic_issues ORDER BY timestamp DESC")
    fun getAllIssues(): Flow<List<CivicIssue>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIssue(issue: CivicIssue)

    @Delete
    suspend fun deleteIssue(issue: CivicIssue)

    // Completed Challenges
    @Query("SELECT * FROM completed_challenges ORDER BY timestamp DESC")
    fun getAllCompletedChallenges(): Flow<List<CompletedChallenge>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompletedChallenge(challenge: CompletedChallenge)

    // Saved Routes
    @Query("SELECT * FROM saved_routes ORDER BY timestamp DESC")
    fun getAllSavedRoutes(): Flow<List<SavedRoute>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedRoute(route: SavedRoute)

    @Delete
    suspend fun deleteSavedRoute(route: SavedRoute)
}
