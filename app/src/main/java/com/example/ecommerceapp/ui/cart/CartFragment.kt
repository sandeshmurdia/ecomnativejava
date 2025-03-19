package com.example.ecommerceapp.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerceapp.databinding.FragmentCartBinding
import com.example.ecommerceapp.model.SampleProducts
import com.example.ecommerceapp.ui.adapter.ProductAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.text.NumberFormat
import java.util.Locale

@AndroidEntryPoint
class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private lateinit var productAdapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        updateTotal()
    }

    private fun setupRecyclerView() {
        val cartProducts = SampleProducts.products.filter { it.isInCart }
        
        productAdapter = ProductAdapter(
            cartProducts,
            onWishlistClick = { product ->
                product.isInWishlist = !product.isInWishlist
            },
            onAddToCartClick = { product ->
                product.quantity++
                updateTotal()
            }
        )

        binding.rvCart.apply {
            adapter = productAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        binding.tvEmptyCart.visibility = 
            if (cartProducts.isEmpty()) View.VISIBLE else View.GONE
        binding.groupCartDetails.visibility = 
            if (cartProducts.isNotEmpty()) View.VISIBLE else View.GONE
    }

    private fun updateTotal() {
        val cartProducts = SampleProducts.products.filter { it.isInCart }
        val subtotal = cartProducts.sumOf { it.price * it.quantity }
        val shipping = if (subtotal > 0) 10.0 else 0.0
        val total = subtotal + shipping

        val formatter = NumberFormat.getCurrencyInstance(Locale.US)
        binding.tvSubtotal.text = "Subtotal: ${formatter.format(subtotal)}"
        binding.tvShipping.text = "Shipping: ${formatter.format(shipping)}"
        binding.tvTotal.text = "Total: ${formatter.format(total)}"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 