package com.example.turboazapp.presentation.ui.fragment

import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.turboazapp.databinding.FragmentNewAnnouncement9Binding

class NewAnnouncementFragment9 : Fragment() {

    private var _binding: FragmentNewAnnouncement9Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewAnnouncement9Binding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val htmlText = "<font color='#FF0000'>Qadağandır!</font><br><b>Skrinşotlar, çərçivəli şəkillər və ekran şəkilləri</b>"
        binding.sertText2.text = Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY)




    }
    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).setToolbarVisible(false)
        (requireActivity() as MainActivity).setBottomNavVisible(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
