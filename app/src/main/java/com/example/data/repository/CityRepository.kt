package com.example.data.repository

import com.example.data.database.CityConnectDao
import com.example.data.database.CivicIssue
import com.example.data.database.CompletedChallenge
import com.example.data.database.SavedRoute
import kotlinx.coroutines.flow.Flow

class CityRepository(private val dao: CityConnectDao) {
    val allIssues: Flow<List<CivicIssue>> = dao.getAllIssues()
    val allCompletedChallenges: Flow<List<CompletedChallenge>> = dao.getAllCompletedChallenges()
    val allSavedRoutes: Flow<List<SavedRoute>> = dao.getAllSavedRoutes()

    suspend fun insertIssue(issue: CivicIssue) {
        dao.insertIssue(issue)
    }

    suspend fun deleteIssue(issue: CivicIssue) {
        dao.deleteIssue(issue)
    }

    suspend fun insertCompletedChallenge(challenge: CompletedChallenge) {
        dao.insertCompletedChallenge(challenge)
    }

    suspend fun insertSavedRoute(route: SavedRoute) {
        dao.insertSavedRoute(route)
    }

    suspend fun deleteSavedRoute(route: SavedRoute) {
        dao.deleteSavedRoute(route)
    }
}
