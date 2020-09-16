package com.jensencelestial.ghiblibrary.android.ui.species

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.RoundedCornersTransformation
import com.jensencelestial.ghiblibrary.android.R
import com.jensencelestial.ghiblibrary.android.data.model.Species
import com.jensencelestial.ghiblibrary.android.databinding.FragmentSpeciesIdvBinding
import com.jensencelestial.ghiblibrary.android.ui.UIState
import com.jensencelestial.ghiblibrary.android.viewmodel.SpeciesIdvViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SpeciesIdvFragment : Fragment() {
    private val speciesIdvViewModel: SpeciesIdvViewModel by viewModels()

    private var _binding: FragmentSpeciesIdvBinding? = null
    private val binding get() = _binding!!

    private val args: SpeciesIdvFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSpeciesIdvBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewEvents()
        initObservers()

        speciesIdvViewModel.getSpecies(args.speciesId)
    }

    private fun initViewEvents() {
        binding.swltSpeciesIdv.setOnRefreshListener {
            speciesIdvViewModel.getSpecies(args.speciesId)
        }
    }

    private fun initObservers() {
        speciesIdvViewModel.species.observe(this.viewLifecycleOwner, SpeciesObserver())
    }

    private fun setSpeciesContent(species: Species) {
        binding.lytSpeciesIdv.tvName.text = species.name

        binding.lytSpeciesIdv.tvClassification.text = species.classification

        binding.lytSpeciesIdv.tvEyeColors.text = species.eyeColors

        binding.lytSpeciesIdv.tvHairColors.text = species.hairColors

        binding.lytSpeciesIdv.ivThumbnail.load(species.imageUrl) {
            placeholder(R.drawable.shape_image_fallback)
            fallback(R.drawable.shape_image_fallback)
            error(R.drawable.shape_image_fallback)
            crossfade(true)
            transformations(RoundedCornersTransformation(4f))
        }
    }

    private inner class SpeciesObserver : Observer<UIState<Species>> {
        override fun onChanged(species: UIState<Species>) {
            when (species) {
                is UIState.Loading -> {
                    if (!binding.swltSpeciesIdv.isRefreshing) {
                        binding.swltSpeciesIdv.isRefreshing = true
                    }
                }
                is UIState.Success -> {
                    binding.swltSpeciesIdv.isRefreshing = false

                    setSpeciesContent(species.value)
                }
                is UIState.Failure -> {
                    binding.swltSpeciesIdv.isRefreshing = false
                }
            }
        }
    }
}