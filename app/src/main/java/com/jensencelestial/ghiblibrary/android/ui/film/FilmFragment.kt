package com.jensencelestial.ghiblibrary.android.ui.film

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.jensencelestial.ghiblibrary.android.R
import com.jensencelestial.ghiblibrary.android.viewmodel.FilmViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilmFragment : Fragment() {
    val filmViewModel: FilmViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_film, container, false)

//        lifecycle.addObserver(filmsViewModel)

        return root
    }
}