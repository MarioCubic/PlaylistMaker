package com.ivan.playlistmaker

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.ivan.playlistmaker.App.Companion.PLAYLIST_MAKER_PREFERENCES

class SearchActivity : AppCompatActivity() {

    val presenter = Presenter()


    var tracks = ArrayList<Track>()
    var history = ArrayDeque<Track>()
    private val resultsAdapter = TrackAdapter(tracks)
    private val historyAdapter = TrackAdapter(history, true)
    private lateinit var searchField: EditText


    @SuppressLint("MissingInflatedId")
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
        val searchHistory = SearchHistory(sharedPrefs)
        history.addAll(searchHistory.readSharedPrefs())
        historyAdapter.notifyDataSetChanged()


        findViewById<RecyclerView>(R.id.musicRecycler).apply {
            adapter = this@SearchActivity.resultsAdapter
        }
        findViewById<RecyclerView>(R.id.historyRecycler).apply {
            adapter = this@SearchActivity.historyAdapter
        }
        searchField = findViewById(R.id.search_input)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        val backButton = findViewById<ImageView>(R.id.back_button)
        backButton.setOnClickListener {
            finish()
        }
        searchField.post {
            searchField.requestFocus()
        }


        val nothingFoundImg = findViewById<ImageView>(R.id.nothingFoundImg)
        val nothingFoundText = findViewById<TextView>(R.id.nothingFoundText)
        val noInternetImg = findViewById<ImageView>(R.id.noInternetImg)
        val noInternetText = findViewById<TextView>(R.id.noInternetText)
        val updateButton = findViewById<Button>(R.id.updateButton)
        val historyRecycler = findViewById<RecyclerView>(R.id.historyRecycler)

        val simpleTextWatcher = object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                historyAdapter.notifyDataSetChanged()
                historyRecycler.visibility =
                    if (searchField.hasFocus() && s?.isEmpty() == true && !history.isEmpty()) View.VISIBLE else View.GONE

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.isVisible = !s.isNullOrEmpty()
                historyAdapter.notifyDataSetChanged()
                historyRecycler.visibility =
                    if (searchField.hasFocus() && s?.isEmpty() == true && !history.isEmpty()) View.VISIBLE else View.GONE
            }


        }
        resultsAdapter.onTrackClick = { track ->
            searchHistory.addTrackToHistory(track, history)
            historyAdapter.notifyDataSetChanged()

        }
        historyAdapter.onClearHistoryClick = {
            history.clear()
            searchHistory.clearSharedPrefs()
            historyRecycler.visibility = View.GONE
            historyAdapter.notifyDataSetChanged()
        }
        searchField.setOnFocusChangeListener { view, hasFocus ->
            historyRecycler.visibility =
                if (hasFocus && searchField.text.isEmpty() && !history.isEmpty()) View.VISIBLE else View.GONE
            historyAdapter.notifyDataSetChanged()
        }

        fun allPlaceholdersDisabled() {
            nothingFoundText.isGone = true
            nothingFoundImg.isGone = true
            noInternetImg.isGone = true
            noInternetText.isGone = true
            updateButton.isGone = true
        }

        fun noInternetPlaceholders() {
            resultsAdapter.notifyDataSetChanged()
            noInternetImg.isVisible = true
            noInternetText.isVisible = true
            updateButton.isVisible = true
        }

        fun nothingFoundPlaceholders() {
            resultsAdapter.notifyDataSetChanged()
            nothingFoundText.isVisible = true
            nothingFoundImg.isVisible = true
        }

        clearButton.setOnClickListener {
            searchField.setText("")
            searchField.clearFocus()
            allPlaceholdersDisabled()
            tracks.clear()
            resultsAdapter.notifyDataSetChanged()


            WindowInsetsControllerCompat(window, window.decorView)
                .hide(WindowInsetsCompat.Type.ime())
        }

        presenter.screenState.observe(this) { screenState ->
            when (screenState) {
                is SearchScreenState.DataLoaded -> {
                    allPlaceholdersDisabled()
                    tracks.clear()
                    tracks.addAll(screenState.data)
                    resultsAdapter.notifyDataSetChanged()

                }

                is SearchScreenState.NothingFound -> {
                    tracks.clear()
                    resultsAdapter.notifyDataSetChanged()
                    allPlaceholdersDisabled()
                    nothingFoundPlaceholders()
                }

                is SearchScreenState.NetworkError -> {
                    tracks.clear()
                    resultsAdapter.notifyDataSetChanged()
                    allPlaceholdersDisabled()
                    noInternetPlaceholders()
                }

                else -> {}
            }
        }

        searchField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE && !searchField.text.isBlank()) {
                presenter.fetchTracks(searchField.text.toString())
            }
            false
        }
        updateButton.setOnClickListener { presenter.fetchTracks(searchField.text.toString()) }

        searchField.addTextChangedListener(simpleTextWatcher)


    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_INPUT, searchField.toString())

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        searchField.setText(savedInstanceState.getString(SEARCH_INPUT))
    }

    companion object {
        const val SEARCH_INPUT = "SEARCH_INPUT"
    }


}
