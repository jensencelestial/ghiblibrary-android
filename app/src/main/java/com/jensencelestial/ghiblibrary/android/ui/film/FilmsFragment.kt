package com.jensencelestial.ghiblibrary.android.ui.film

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jensencelestial.ghiblibrary.android.data.model.Film
import com.jensencelestial.ghiblibrary.android.databinding.FragmentFilmsBinding
import com.jensencelestial.ghiblibrary.android.ui.UIState
import com.jensencelestial.ghiblibrary.android.viewmodel.FilmsViewModel
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.IAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilmsFragment : Fragment() {
    private val filmsViewModel: FilmsViewModel by viewModels()

    private var _binding: FragmentFilmsBinding? = null
    private val binding get() = _binding!!

    //save our FastAdapter
    private lateinit var fastAdapter: FastAdapter<FilmItem>

    //save our FastAdapter
    private lateinit var filmAdapter: ItemAdapter<FilmItem>

    private lateinit var rvFilms: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFilmsBinding.inflate(inflater, container, false)
        val view = binding.root

        filmAdapter = ItemAdapter()
        fastAdapter = FastAdapter.with(listOf(filmAdapter))

        binding.rvFilms.apply {
            layoutManager = LinearLayoutManager(view.context)
            adapter = fastAdapter
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initObservers()
        initViewEvents()
    }

    private fun initViewEvents() {
        binding.swltFilms.setOnRefreshListener {
            filmsViewModel.getFilms()
        }

        fastAdapter.onClickListener =
            { view: View?, iAdapter: IAdapter<FilmItem>, filmItem: FilmItem, i: Int ->
                findNavController().navigate(
                    FilmsFragmentDirections.actionFilmsFragmentToFilmFragment(
                        filmItem.film.id
                    )
                )
                false
            }
    }

    private fun initObservers() {
        filmsViewModel.films.observe(this.viewLifecycleOwner, FilmsObserver())
    }

    private inner class FilmsObserver : Observer<UIState<List<Film>>> {
        override fun onChanged(films: UIState<List<Film>>) {
            when (films) {
                is UIState.Loading -> {
                    if (!binding.swltFilms.isRefreshing) {
                        binding.swltFilms.isRefreshing = true
                    }
                }
                is UIState.Success -> {
                    binding.swltFilms.isRefreshing = false

                    filmAdapter.clear()
                    filmAdapter.add(films.value.map { FilmItem(it) })
                }
                is UIState.Failure -> {
                    binding.swltFilms.isRefreshing = false
                }
            }
        }
    }
}