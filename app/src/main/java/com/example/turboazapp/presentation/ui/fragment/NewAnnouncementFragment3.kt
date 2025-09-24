package com.example.turboazapp.presentation.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.turboazapp.databinding.FragmentNewAnnouncement3Binding

class NewAnnouncementFragment3 : Fragment() {
    private var _binding: FragmentNewAnnouncement3Binding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewAnnouncement3Binding.inflate(inflater, container, false)
        return binding.root

    }

}