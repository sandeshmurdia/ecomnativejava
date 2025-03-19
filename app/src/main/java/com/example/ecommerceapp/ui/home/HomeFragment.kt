package com.example.ecommerceapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ecommerceapp.databinding.FragmentHomeBinding
import com.example.ecommerceapp.model.Product
import com.example.ecommerceapp.model.SampleProducts
import com.example.ecommerceapp.ui.adapter.ProductAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var productAdapter: ProductAdapter
    private var allProducts = SampleProducts.products

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSearch()
        setupCategoryFilter()
        setupFilterFab()
    }

    private fun setupRecyclerView() {
        productAdapter = ProductAdapter(
            allProducts,
            onWishlistClick = { product ->
                product.isInWishlist = !product.isInWishlist
                Toast.makeText(
                    requireContext(),
                    if (product.isInWishlist) "Added to wishlist" else "Removed from wishlist",
                    Toast.LENGTH_SHORT
                ).show()
            },
            onAddToCartClick = { product ->
                product.isInCart = true
                product.quantity++
                Toast.makeText(
                    requireContext(),
                    "Added to cart",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )

        val gridLayoutManager = GridLayoutManager(requireContext(), 2)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int = 1
        }

        binding.rvProducts.apply {
            layoutManager = gridLayoutManager
            adapter = productAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupSearch() {
        binding.searchLayout.editText?.addTextChangedListener { text ->
            val query = text.toString().lowercase()
            val filteredProducts = if (query.isEmpty()) {
                allProducts
            } else {
                allProducts.filter { product ->
                    product.name.lowercase().contains(query) ||
                    product.description.lowercase().contains(query)
                }
            }
            productAdapter.updateProducts(filteredProducts)
        }
    }

    private fun setupCategoryFilter() {
        binding.categoryChips.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isEmpty()) return@setOnCheckedStateChangeListener
            
            val selectedChip = group.findViewById<Chip>(checkedIds[0])
            val category = selectedChip.text.toString()
            
            val filteredProducts = if (category == "All") {
                allProducts
            } else {
                allProducts.filter { product ->
                    when (category) {
                        "Clothing" -> product.name.contains("Jacket") || product.name.contains("Dress")
                        "Shoes" -> product.name.contains("Sneakers")
                        "Accessories" -> !product.name.contains("Jacket") && 
                                       !product.name.contains("Dress") && 
                                       !product.name.contains("Sneakers")
                        else -> true
                    }
                }
            }
            productAdapter.updateProducts(filteredProducts)
        }
    }

    private fun setupFilterFab() {
        binding.fabFilter.setOnClickListener {
            val dialog = BottomSheetDialog(requireContext())
            // TODO: Implement filter dialog
            Toast.makeText(requireContext(), "Filter options coming soon!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 