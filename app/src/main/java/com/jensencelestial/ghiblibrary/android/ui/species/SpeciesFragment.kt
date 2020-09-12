package com.jensencelestial.ghiblibrary.android.ui.species

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jensencelestial.ghiblibrary.android.data.model.Species
import com.jensencelestial.ghiblibrary.android.databinding.FragmentSpeciesBinding
import com.jensencelestial.ghiblibrary.android.ui.UIState
import com.jensencelestial.ghiblibrary.android.viewmodel.SpeciesViewModel
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.IAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SpeciesFragment : Fragment() {
    private val speciesViewModel: SpeciesViewModel by viewModels()

    private var _binding: FragmentSpeciesBinding? = null
    private val binding get() = _binding!!

    private lateinit var fastAdapter: FastAdapter<SpeciesItem>
    private lateinit var speciesAdapter: ItemAdapter<SpeciesItem>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSpeciesBinding.inflate(inflater, container, false)
        val view = binding.root

        speciesAdapter = ItemAdapter()
        fastAdapter = FastAdapter.with(listOf(speciesAdapter))

        binding.rvspecies.apply {
            layoutManager = LinearLayoutManager(view.context)
            adapter = fastAdapter
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewEvents()
        initObservers()
    }

    private fun initViewEvents() {
        binding.swltSpecies.setOnRefreshListener {
            speciesViewModel.getSpecies()
        }

        fastAdapter.onClickListener =
            { view: View?, iAdapter: IAdapter<SpeciesItem>, speciesItem: SpeciesItem, i: Int ->
                findNavController().navigate(
                    SpeciesFragmentDirections.actionSpeciesFragmentToSpeciesIdvFragment(
                        speciesItem.species.id
                    )
                )
                false
            }
    }

    private fun initObservers() {
        speciesViewModel.species.observe(this.viewLifecycleOwner, SpeciesObserver())
    }

    private inner class SpeciesObserver : Observer<UIState<List<Species>>> {
        override fun onChanged(species: UIState<List<Species>>) {
            when (species) {
                is UIState.Loading -> {
                    if (!binding.swltSpecies.isRefreshing) {
                        binding.swltSpecies.isRefreshing = true
                    }
                }
                is UIState.Success -> {
                    binding.swltSpecies.isRefreshing = false

                    speciesAdapter.clear()
                    speciesAdapter.add(species.value.map { SpeciesItem(it) })
                }
                is UIState.Failure -> {
                    binding.swltSpecies.isRefreshing = false
                }
            }
        }
    }
}