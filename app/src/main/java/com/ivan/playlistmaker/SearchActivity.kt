package com.ivan.playlistmaker

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.google.android.material.appbar.MaterialToolbar
import com.ivan.playlistmaker.App.Companion.PLAYLIST_MAKER_PREFERENCES

class SearchActivity : AppCompatActivity() {


    private var tracks = ArrayList<Track>()
    private val adapter = TrackAdapter(tracks)
    private lateinit var searchField: EditText


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
        val presenter = Presenter(sharedPrefs)
        presenter.updateHistory(HistoryAction.READ)


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


        val nothingFoundImg = findViewById<ImageView>(R.id.nothingFoundImg)
        val nothingFoundText = findViewById<TextView>(R.id.nothingFoundText)
        val noInternetImg = findViewById<ImageView>(R.id.noInternetImg)
        val noInternetText = findViewById<TextView>(R.id.noInternetText)
        val updateButton = findViewById<Button>(R.id.updateButton)

        val simpleTextWatcher = object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                if (searchField.hasFocus() && s?.isEmpty() == true) {
                    presenter.updateHistory(HistoryAction.READ)
                }

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.isVisible = !s.isNullOrEmpty()
                if (searchField.hasFocus() && s?.isEmpty() == true) {
                    presenter.updateHistory(HistoryAction.READ)
                }
            }


        }
        adapter.onTrackClick = { track ->
            presenter.saveHistory(track)

        }
        adapter.onClearHistoryClick = {
            tracks.clear()
            presenter.updateHistory(HistoryAction.CLEAR)
        }
        searchField.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && searchField.text.isEmpty()) {
                presenter.updateHistory(HistoryAction.READ)
            }


            fun allPlaceholdersDisabled() {
                nothingFoundText.isGone = true
                nothingFoundImg.isGone = true
                noInternetImg.isGone = true
                noInternetText.isGone = true
                updateButton.isGone = true
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



            clearButton.setOnClickListener {
                searchField.setText("")
                searchField.clearFocus()
                allPlaceholdersDisabled()
                tracks.clear()
                adapter.notifyDataSetChanged()


                WindowInsetsControllerCompat(window, window.decorView)
                    .hide(WindowInsetsCompat.Type.ime())
            }

            presenter.screenState.observe(this) { screenState ->
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
                if (actionId == EditorInfo.IME_ACTION_DONE && !searchField.text.isBlank()) {
                    presenter.fetchTracks(searchField.text.toString())
                }
                false
            }
            updateButton.setOnClickListener { presenter.fetchTracks(searchField.text.toString()) }

            searchField.addTextChangedListener(simpleTextWatcher)


        }
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
