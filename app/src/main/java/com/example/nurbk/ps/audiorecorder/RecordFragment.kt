package com.example.nurbk.ps.audiorecorder

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_record.*
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class RecordFragment : Fragment(R.layout.fragment_record) {


    private lateinit var navController: NavController
    private var isRecording = false
    private val recordPermission = Manifest.permission.RECORD_AUDIO
    private val PERMISSION_CODE = 111
    private var mediaRecorder: MediaRecorder? = null
    private lateinit var recordFile: String


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        record_list_btn.setOnClickListener {
            navController.navigate(R.id.action_recordFragment_to_audioListFragment)

        }


        record_btn.setOnClickListener {
            if (isRecording) {
                stopRecording()
                record_btn.setImageDrawable(
                    resources.getDrawable(
                        R.drawable.record_btn_stopped,
                        null
                    )
                )
                isRecording = false
            } else {
                if (checkPermission()) {
                    startRecording()
                    record_btn.setImageDrawable(
                        resources.getDrawable(
                            R.drawable.record_btn_recording,
                            null
                        )
                    )
                    isRecording = true
                }
            }
        }

    }

    private fun stopRecording() {
        record_timer.stop()
        record_filename.text = "Recording Stopped, File Saved: $recordFile"

        mediaRecorder!!.stop()
        mediaRecorder!!.release()
        mediaRecorder = null

    }

    private fun startRecording() {
        record_timer.base = SystemClock.elapsedRealtime()
        record_timer.start()

        val recordPath = requireActivity()
            .getExternalFilesDir("/")!!
            .absolutePath

        val format = SimpleDateFormat("yyyy_MM_dd_hh_mm_ss", Locale.CANADA_FRENCH)
        val data = format.format(Date())


        recordFile = "Recording$data.3gp"
        record_filename.text = "Recording, file name: $recordFile"
        mediaRecorder = MediaRecorder()
        mediaRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        mediaRecorder!!.setOutputFile("$recordPath/$recordFile")
        mediaRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

        try {
            mediaRecorder!!.prepare()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        //Start Recording
        mediaRecorder!!.start()
    }

    private fun checkPermission(): Boolean {
        return if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            true
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(recordPermission),
                PERMISSION_CODE
            )
            false
        }
    }

}