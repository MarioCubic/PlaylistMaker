package com.ivan.playlistmaker

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Locale

class SearchActivity : AppCompatActivity() {

    private val iTunesUrl = "https://itunes.apple.com/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val tracks = ArrayList<Track>()
    private val adapter = TrackAdapter(tracks)
    private val iTunesService = retrofit.create(ItunesApi::class.java)
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
        findViewById<RecyclerView>(R.id.musicRecycler).apply {
            adapter = this@SearchActivity.adapter
        }
        searchField = findViewById(R.id.search_input)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        val backButton = findViewById<ImageView>(R.id.back_button)
        backButton.setOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            searchField.setText("")
            searchField.clearFocus()
            tracks.clear()
            adapter.notifyDataSetChanged()


            WindowInsetsControllerCompat(window, window.decorView)
                .hide(WindowInsetsCompat.Type.ime())
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.isVisible = !s.isNullOrEmpty()
            }


        }
        fun convertTrackTimeToLocale(trackTimeMilis : Long): String {
            return SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMilis)
        }

        val nothingFoundImg = findViewById<ImageView>(R.id.nothingFoundImg)
        val nothingFoundText = findViewById<TextView>(R.id.nothingFoundText)
        val noInternetImg = findViewById<ImageView>(R.id.noInternetImg)
        val noInternetText = findViewById<TextView>(R.id.noInternetText)
        val updateButton = findViewById<Button>(R.id.updateButton)
        fun allPlaceholdersDisabled(){
            nothingFoundText.isGone = true
            nothingFoundImg.isGone = true
            noInternetImg.isGone = true
            noInternetText.isGone = true
            updateButton.isGone = true
        }
        fun noInternetPlaceholders(){
            adapter.notifyDataSetChanged()
            noInternetImg.isVisible = true
            noInternetText.isVisible = true
            updateButton.isVisible = true
        }
        fun nothingFoundPlaceholders(){
            adapter.notifyDataSetChanged()
            nothingFoundText.isVisible = true
            nothingFoundImg.isVisible = true
        }
        fun searchAction(){
            if(searchField.text.isNotEmpty()){
                iTunesService.search(searchField.text.toString()).enqueue(object : Callback<TrackResponse> {
                    override fun onResponse(
                        call: Call<TrackResponse?>,
                        response: Response<TrackResponse?>
                    ) {
                        if (response.code() == 200) {
                            tracks.clear()
                            allPlaceholdersDisabled()
                            if (response.body()?.results?.isNotEmpty() == true) {
                                val results = response.body()?.results!!.map {track ->
                                    Track(
                                        trackTime = convertTrackTimeToLocale(track.trackTimeMillis),
                                        trackName = track.trackName,
                                        artistName = track.artistName,
                                        artworkUrl100 = track.artworkUrl100
                                    )
                                }
                                tracks.addAll(results)
                                adapter.notifyDataSetChanged()
                            }
                            if (tracks.isEmpty()){
                                allPlaceholdersDisabled()
                                nothingFoundPlaceholders()
                            }
                        }
                        else {
                            tracks.clear()
                            allPlaceholdersDisabled()
                            noInternetPlaceholders()
                        }
                    }

                    override fun onFailure(
                        call: Call<TrackResponse?>,
                        t: Throwable
                    ) {
                        tracks.clear()
                        allPlaceholdersDisabled()
                        noInternetPlaceholders()
                    }
                })
            }
        }
        searchField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchAction()
            }
            false
        }
        updateButton.setOnClickListener { searchAction() }

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
