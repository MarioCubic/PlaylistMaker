package com.ivan.playlistmaker.search

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.gson.Gson
import com.ivan.playlistmaker.search.presentation.SearchPresenter
import com.ivan.playlistmaker.R
import com.ivan.playlistmaker.search.presentation.SearchScreenState
import com.ivan.playlistmaker.search.adapters.TrackAdapter
import com.ivan.playlistmaker.App.Companion.PLAYLIST_MAKER_PREFERENCES
import com.ivan.playlistmaker.model.HistoryActions
import com.ivan.playlistmaker.model.Track
import com.ivan.playlistmaker.player.PlayerActivity

class SearchActivity : AppCompatActivity() {


    private var tracks = ArrayList<Track>()
    private val adapter = TrackAdapter(tracks)
    private lateinit var searchField: EditText
    private var isClickAllowed = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val sharedPrefs = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
        val searchPresenter = SearchPresenter(sharedPrefs)
        searchPresenter.updateHistory(HistoryActions.READ)


        findViewById<RecyclerView>(R.id.musicRecycler).apply {
            adapter = this@SearchActivity.adapter
        }
        searchField = findViewById(R.id.search_input)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        val navToolbar = findViewById<MaterialToolbar>(R.id.header)
        navToolbar.setNavigationOnClickListener {
            finish()
        }
        searchField.post {
            searchField.requestFocus()
        }

        val searchRunnable = Runnable { searchPresenter.fetchTracks(searchField.text.toString()) }
        val searchHandler = Handler(Looper.getMainLooper())
        val nothingFoundImg = findViewById<ImageView>(R.id.nothingFoundImg)
        val nothingFoundText = findViewById<TextView>(R.id.nothingFoundText)
        val noInternetImg = findViewById<ImageView>(R.id.noInternetImg)
        val noInternetText = findViewById<TextView>(R.id.noInternetText)
        val updateButton = findViewById<Button>(R.id.updateButton)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        fun searchDebounce() {
            if (searchField.text.isBlank()) {
                searchHandler.removeCallbacks(searchRunnable)

            } else {
                progressBar.isVisible = true
                searchHandler.removeCallbacks(searchRunnable)
                searchHandler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
            }

        }


        fun clickDebounce(): Boolean {
            val current = isClickAllowed
            if (isClickAllowed) {
                isClickAllowed = false
                searchHandler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
            }
            return current
        }



        adapter.onTrackClick = { track ->
            if (clickDebounce()) {
                searchHandler.removeCallbacks(searchRunnable)
                searchPresenter.saveHistory(track)
                val json = Gson().toJson(track)
                val playerIntent = Intent(this, PlayerActivity::class.java)
                playerIntent.putExtra("track", json)
                startActivity(playerIntent)
            }
        }
        adapter.onClearHistoryClick = {
            tracks.clear()
            searchPresenter.updateHistory(HistoryActions.CLEAR)
        }
        searchField.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && searchField.text.isEmpty()) {
                searchPresenter.updateHistory(HistoryActions.READ)
            }


            fun allPlaceholdersDisabled() {
                nothingFoundText.isGone = true
                nothingFoundImg.isGone = true
                noInternetImg.isGone = true
                noInternetText.isGone = true
                updateButton.isGone = true
                progressBar.isGone = true
            }

            fun noInternetPlaceholders() {
                adapter.notifyDataSetChanged()
                noInternetImg.isVisible = true
                noInternetText.isVisible = true
                updateButton.isVisible = true
            }

            fun nothingFoundPlaceholders() {
                adapter.notifyDataSetChanged()
                nothingFoundText.isVisible = true
                nothingFoundImg.isVisible = true
            }


            searchField.doOnTextChanged { s, _, _, _ ->
                clearButton.isVisible = !s.isNullOrEmpty()
                if (searchField.hasFocus() && s?.isBlank() == true) {
                    allPlaceholdersDisabled()
                    searchPresenter.updateHistory(HistoryActions.READ)
                } else {
                    tracks.clear()
                    adapter.notifyDataSetChanged()
                }
                searchDebounce()
            }

            clearButton.setOnClickListener {
                searchField.setText("")

                searchField.clearFocus()
                allPlaceholdersDisabled()
                tracks.clear()
                adapter.notifyDataSetChanged()


                WindowInsetsControllerCompat(window, window.decorView)
                    .hide(WindowInsetsCompat.Type.ime())
            }

            searchPresenter.screenState.observe(this) { screenState ->
                when (screenState) {
                    is SearchScreenState.DataLoaded -> {
                        adapter.changeHeaderFooter(false)
                        allPlaceholdersDisabled()
                        tracks.clear()
                        tracks.addAll(screenState.data)
                        adapter.notifyDataSetChanged()

                    }

                    is SearchScreenState.NothingFound -> {
                        tracks.clear()
                        adapter.notifyDataSetChanged()
                        allPlaceholdersDisabled()
                        nothingFoundPlaceholders()
                    }

                    is SearchScreenState.NetworkError -> {
                        tracks.clear()
                        adapter.notifyDataSetChanged()
                        allPlaceholdersDisabled()
                        noInternetPlaceholders()
                    }

                    is SearchScreenState.SearchHistory -> {
                        tracks.clear()
                        adapter.changeHeaderFooter(true)
                        adapter.notifyDataSetChanged()
                        tracks.addAll(screenState.data)
                        adapter.notifyDataSetChanged()
                    }


                    else -> {}
                }
            }
            searchField.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    searchField.clearFocus()
                }
                false
            }
            updateButton.setOnClickListener {
                searchHandler.removeCallbacks(searchRunnable)
                searchHandler.post(searchRunnable)
            }



        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_INPUT, searchField.text.toString())

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        searchField.setText(savedInstanceState.getString(SEARCH_INPUT))
    }

    companion object {
        const val SEARCH_INPUT = "SEARCH_INPUT"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }


}
