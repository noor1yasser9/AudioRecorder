package com.example.nurbk.ps.audiorecorder

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_audio_list.*
import kotlinx.android.synthetic.main.player_sheet.*
import java.io.File
import java.io.IOException

class ListAudioFragment : Fragment(R.layout.fragment_audio_list) {

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private var allFile: ArrayList<File>? = null
    private lateinit var adapter: AudioListAdapter

    private lateinit var mediaPlayer: MediaPlayer
    private var isPlaying = false

    private var filePlay: File? = null

    private lateinit var seekbarHandler: Handler
    private lateinit var updateSeekbar: Runnable

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        bottomSheetBehavior = BottomSheetBehavior.from(player_sheet)

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
            }
        })

        val path = requireActivity()
            .getExternalFilesDir("/")!!
            .absolutePath
        val directory = File(path)

        adapter = AudioListAdapter()
        adapter.differ.submitList(directory.listFiles().toList())
        audio_list_view.layoutManager = LinearLayoutManager(requireActivity())
        audio_list_view.setHasFixedSize(true)
        audio_list_view.adapter = adapter


        adapter.setOnItemClickListener {
            filePlay = it
            if (isPlaying) {
                stopAudio()
                playAudio(filePlay!!)
            } else {
                playAudio(filePlay!!)
            }
        }

        player_play_btn.setOnClickListener {
            if (isPlaying) {
                pauseAudio()
            } else {
                if (filePlay != null) {
                    resumeAudio();
                }
            }
        }

    }

    private fun pauseAudio() {
        mediaPlayer.pause()
        replaceButton(R.drawable.player_play_btn)
        isPlaying = false
    }

    private fun resumeAudio() {
        mediaPlayer.stop()
        replaceButton(R.drawable.player_pause_btn)
        isPlaying = true
    }
    private fun stopAudio() {
        isPlaying = false
        replaceButton(R.drawable.player_play_btn)
        player_header_title.text = "Stopped"
        mediaPlayer.stop()
    }



    private fun playAudio(filePlay: File) {
        mediaPlayer = MediaPlayer()
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        try {
            mediaPlayer.setDataSource(filePlay.absolutePath)
            mediaPlayer.prepare()
            mediaPlayer.start()
        } catch (e: IOException) {
            e.printStackTrace()
        }


        replaceButton(R.drawable.player_pause_btn)

        player_filename.text = filePlay.name
        player_header_title.text = "Playing"
        isPlaying = true

        mediaPlayer.setOnCompletionListener {
            stopAudio()
            player_header_title.text = "Finished"
        }

        player_seekbar.max = mediaPlayer.duration

        seekbarHandler = Handler()
        updateRunnable()
        seekbarHandler.postDelayed(updateSeekbar, 0)

    }


    private fun updateRunnable() {
        updateSeekbar = object : Runnable {
            override fun run() {
                player_seekbar.progress = mediaPlayer.currentPosition
                seekbarHandler.postDelayed(this, 500)
            }
        }
    }

    private fun replaceButton(id: Int) {
        player_play_btn.setImageDrawable(
            requireActivity().resources.getDrawable(
                id,
                null
            )
        )

    }
}