package com.yilmazvolkan.simplenoteapp

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.yilmazvolkan.simplenoteapp.database.NotesDBHelper
import com.yilmazvolkan.simplenoteapp.databinding.ActivityMainBinding
import com.yilmazvolkan.simplenoteapp.fragments.NoteListFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var recordFragment: NoteListFragment? = null
    private lateinit var notesDBHelper : NotesDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        if (savedInstanceState == null) {
            recordFragment = NoteListFragment.newInstance()
            if (recordFragment != null && this.isFinishing.not()) {
                supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fragment_container, recordFragment!!)
                    .addToBackStack(null)
                    .commitAllowingStateLoss()
            }
        }

        notesDBHelper = NotesDBHelper(this)
    }
}