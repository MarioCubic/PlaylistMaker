package com.ivan.playlistmaker.search.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.inputmethod.EditorInfo
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import com.google.gson.Gson
import com.ivan.playlistmaker.creator.Creator
import com.ivan.playlistmaker.data.dto.Track
import com.ivan.playlistmaker.databinding.ActivitySearchBinding
import com.ivan.playlistmaker.player.ui.PlayerActivity
import com.ivan.playlistmaker.search.presentation.SearchScreenState
import com.ivan.playlistmaker.search.presentation.SearchViewModel
import com.ivan.playlistmaker.search.presentation.SearchViewModelFactory
import com.ivan.playlistmaker.search.ui.adapters.TrackAdapter

class SearchActivity : AppCompatActivity() {

    private var tracks = ArrayList<Track>()
    private val adapter = TrackAdapter(tracks)

    private var isClickAllowed = true
    private lateinit var binding: ActivitySearchBinding
    private val searchViewModel: SearchViewModel by viewModels {
        SearchViewModelFactory(
            Creator.provideTracksInteractor(),
            Creator.provideSearchHistoryInteractor(applicationContext)
        )
    }
    private lateinit var searchRunnable : Runnable
    private val searchHandler = Handler(Looper.getMainLooper())


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        searchRunnable = Runnable { searchViewModel.fetchTracks(binding.searchInput.text.toString()) }
        setupWindowInsets()
        setupRecycler()
        setupListeners()
        observeViewModel()
        searchViewModel.readHistory()
    }

    private fun observeViewModel() {
        searchViewModel.screenState.observe(this) { screenState ->
            when (screenState) {
                is SearchScreenState.DataLoaded -> {
                    adapter.changeHeaderFooter(false)
                    allPlaceholdersDisabled()
                    tracks.clear()
                    tracks.addAll(screenState.data)
                    adapterNotify()
                }

                is SearchScreenState.NothingFound -> {
                    tracks.clear()
                    adapterNotify()
                    allPlaceholdersDisabled()
                    nothingFoundPlaceholders()
                }

                is SearchScreenState.NetworkError -> {
                    tracks.clear()
                    adapterNotify()
                    allPlaceholdersDisabled()
                    noInternetPlaceholders()
                }

                is SearchScreenState.SearchHistory -> {
                    tracks.clear()
                    adapter.changeHeaderFooter(true)
                    tracks.addAll(screenState.data)
                    adapterNotify()
                }

                else -> {}
            }
        }
    }

    private fun setupRecycler() {
        binding.musicRecycler.apply {
            adapter = this@SearchActivity.adapter
        }
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupListeners() {
        binding.searchInput.post {
            binding.searchInput.requestFocus()
        }

        binding.header.setNavigationOnClickListener {
            finish()
        }
        binding.searchInput.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.searchInput.text.isEmpty()) {
                searchViewModel.readHistory()
            }
        }
        binding.clearIcon.setOnClickListener {
            binding.searchInput.setText("")
            binding.searchInput.clearFocus()
            allPlaceholdersDisabled()
            tracks.clear()
            adapterNotify()

            WindowInsetsControllerCompat(
                window,
                window.decorView
            ).hide(WindowInsetsCompat.Type.ime())
        }
        binding.searchInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.searchInput.clearFocus()
            }
            false
        }
        binding.updateButton.setOnClickListener {
            searchHandler.removeCallbacks(searchRunnable)
            searchHandler.post(searchRunnable)
        }
        adapter.onTrackClick = { track ->
            if (clickDebounce()) {
                searchHandler.removeCallbacks(searchRunnable)
                searchViewModel.saveHistory(track)
                val json = Gson().toJson(track)
                val playerIntent = Intent(this, PlayerActivity::class.java)
                playerIntent.putExtra("track", json)
                startActivity(playerIntent)
            }
        }
        adapter.onClearHistoryClick = {
            tracks.clear()
            searchViewModel.clearHistory()
        }


        binding.searchInput.doOnTextChanged { s, _, _, _ ->
            binding.clearIcon.isVisible = !s.isNullOrEmpty()
            if (binding.searchInput.hasFocus() && s?.isBlank() == true) {
                allPlaceholdersDisabled()
                searchViewModel.readHistory()
            } else {
                tracks.clear()
                adapterNotify()
            }
            searchDebounce()
        }

    }

    private fun searchDebounce() {
        if (binding.searchInput.text.isBlank()) {
            searchHandler.removeCallbacks(searchRunnable)
        } else {
            binding.progressBar.isVisible = true
            searchHandler.removeCallbacks(searchRunnable)
            searchHandler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            searchHandler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun allPlaceholdersDisabled() {
        binding.apply {
            nothingFoundText.isGone = true
            nothingFoundImg.isGone = true
            noInternetImg.isGone = true
            noInternetText.isGone = true
            updateButton.isGone = true
            progressBar.isGone = true
        }

    }

    private fun noInternetPlaceholders() {
        binding.apply {
            adapterNotify()
            noInternetImg.isVisible = true
            noInternetText.isVisible = true
            updateButton.isVisible = true
        }
    }

    private fun nothingFoundPlaceholders() {
        binding.apply {
            adapterNotify()
            nothingFoundText.isVisible = true
            nothingFoundImg.isVisible = true
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun adapterNotify() {
        adapter.notifyDataSetChanged()
    }




    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}
