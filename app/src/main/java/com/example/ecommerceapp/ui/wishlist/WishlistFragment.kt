package com.example.ecommerceapp.ui.wishlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerceapp.databinding.FragmentWishlistBinding
import com.example.ecommerceapp.model.SampleProducts
import com.example.ecommerceapp.ui.adapter.ProductAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WishlistFragment : Fragment() {

    private var _binding: FragmentWishlistBinding? = null
    private val binding get() = _binding!!
    private lateinit var productAdapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWishlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val wishlistProducts = SampleProducts.products.filter { it.isInWishlist }
        
        productAdapter = ProductAdapter(
            wishlistProducts,
            onWishlistClick = { product ->
                product.isInWishlist = !product.isInWishlist
                setupRecyclerView() // Refresh the list
            },
            onAddToCartClick = { product ->
                product.isInCart = true
                product.quantity++
            }
        )

        binding.rvWishlist.apply {
            adapter = productAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        binding.tvEmptyWishlist.visibility = 
            if (wishlistProducts.isEmpty()) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 