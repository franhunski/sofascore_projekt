package com.example.sofascore_zavrsni_projekt.ui.tournament_details

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.sofascore_zavrsni_projekt.data.repository.MiniSofaRepository
import com.example.sofascore_zavrsni_projekt.ui.adapter.MatchesItem
import com.example.sofascore_zavrsni_projekt.data.remote.Result


const val ITEMS_PER_PAGE = 20

class MatchesPagingSource(
    private val repository: MiniSofaRepository,
    private val tournamentId: Int
) : PagingSource<Int, MatchesItem>() {


    override fun getRefreshKey(state: PagingState<Int, MatchesItem>): Int {
        return state.anchorPosition?.let { anchorPosition ->
            anchorPosition / ITEMS_PER_PAGE
        } ?: 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MatchesItem> {
        val page = params.key ?: 0
        return LoadResult.Page(getMatches(page), (page - 1).takeIf { it >= 0 }, page + 1)
    }

    private suspend fun getMatches(page: Int): List<MatchesItem> {
        val response = repository.getTournamentEvents(tournamentId, "next", page)
        return  if (response is Result.Success) {
            var currentRound = response.data[0].round
            val matches: MutableList<MatchesItem> = mutableListOf()
            for (event in response.data) {
                if (currentRound != event.round) {
                    currentRound = event.round
                    matches.add(MatchesItem.HeaderInfoItem(currentRound.toString()))
                }
                matches.add(MatchesItem.MatchInfoItem(event))
            }
            matches
        } else {
            emptyList()
        }
    }
}

