package com.example.ecommerceapp.ui.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.ecommerceapp.R
import com.example.ecommerceapp.databinding.FragmentCheckoutBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import com.zipy.zipyandroid.Zipy

@AndroidEntryPoint
class CheckoutFragment : Fragment() {

    private var _binding: FragmentCheckoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupAnimations()
        setupClickListeners()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupAnimations() {
        // Slide up animation for cards
        val slideUp = AnimationUtils.loadAnimation(requireContext(), android.R.anim.slide_in_left)
        binding.apply {
            root.findViewWithTag<View>("address_card")?.startAnimation(slideUp)
            root.findViewWithTag<View>("payment_card")?.startAnimation(slideUp)
            root.findViewWithTag<View>("summary_card")?.startAnimation(slideUp)
            root.findViewWithTag<View>("promo_card")?.startAnimation(slideUp)
        }
    }

    private fun setupClickListeners() {
        // Place Order button click
        binding.btnPlaceOrder.setOnClickListener {
            if (validateFields()) {
                showOrderConfirmation()
            }
        }

        // Apply promo code
        binding.root.findViewWithTag<View>("apply_promo")?.setOnClickListener {
            val promoCode = binding.root.findViewWithTag<View>("promo_input")?.toString() ?: ""
            if (promoCode.isNotEmpty()) {
                applyPromoCode(promoCode)
            } else {
                showError("Please enter a promo code")
            }
        }
    }

    private fun validateFields(): Boolean {
        var isValid = true
        
        try {
            // Validate name
            binding.root.findViewWithTag<View>("name_input")?.let {
                if (it.toString().isEmpty()) {
                    showError("Please enter your name")
                    isValid = false
                    Zipy.logError("Checkout validation failed: Name empty")
                }
            }

            // Validate address
            binding.root.findViewWithTag<View>("address_input")?.let {
                if (it.toString().isEmpty()) {
                    showError("Please enter your address")
                    isValid = false
                    Zipy.logError("Checkout validation failed: Address empty")
                }
            }

            // Validate city
            binding.root.findViewWithTag<View>("city_input")?.let {
                if (it.toString().isEmpty()) {
                    showError("Please enter your city")
                    isValid = false
                }
            }

            // Validate ZIP code
            binding.root.findViewWithTag<View>("zip_input")?.let {
                if (it.toString().isEmpty()) {
                    showError("Please enter your ZIP code")
                    isValid = false
                }
            }
        } catch (e: Exception) {
            Zipy.logException("Checkout validation error", e)
            isValid = false
        }

        return isValid
    }

    private fun showOrderConfirmation() {
        // Animate the button
        binding.btnPlaceOrder.apply {
            isEnabled = false
            text = "Processing..."
        }

        try {
            // Log order processing start
            val orderDetails = mapOf(
                "subtotal" to binding.tvSubtotal.text.toString(),
                "shipping" to binding.tvShipping.text.toString(),
                "total" to binding.tvTotal.text.toString()
            )
            Zipy.logMessage("Order processing started", orderDetails)

            // Simulate order processing
            view?.postDelayed({
                binding.btnPlaceOrder.text = "Order Placed!"
                showSuccessMessage()
                
                // Log successful order placement
                Zipy.logMessage("Order placed successfully", orderDetails)

                // Navigate to order confirmation screen after delay
                view?.postDelayed({
                    findNavController().navigate(R.id.action_checkoutFragment_to_orderConfirmationFragment)
                }, 1000)
            }, 2000)
        } catch (e: Exception) {
            // Log order processing error
            Zipy.logException("Order processing failed", e)
            showError("Failed to process order. Please try again.")
            binding.btnPlaceOrder.apply {
                isEnabled = true
                text = "Place Order"
            }
        }
    }

    private fun applyPromoCode(code: String) {
        // Simulate promo code validation
        when (code.uppercase()) {
            "WELCOME10" -> {
                updatePricesWithDiscount(10)
                showSuccess("Promo code applied successfully!")
            }
            "SALE20" -> {
                updatePricesWithDiscount(20)
                showSuccess("Promo code applied successfully!")
            }
            else -> showError("Invalid promo code")
        }
    }

    private fun updatePricesWithDiscount(discountPercentage: Int) {
        val subtotal = binding.tvSubtotal.text.toString().removePrefix("$").toDouble()
        val shipping = binding.tvShipping.text.toString().removePrefix("$").toDouble()
        
        val discount = subtotal * (discountPercentage / 100.0)
        val newSubtotal = subtotal - discount
        val newTotal = newSubtotal + shipping

        binding.tvSubtotal.text = String.format("$%.2f", newSubtotal)
        binding.tvTotal.text = String.format("$%.2f", newTotal)

        // Animate price changes
        binding.tvSubtotal.startAnimation(
            AnimationUtils.loadAnimation(requireContext(), android.R.anim.fade_in)
        )
        binding.tvTotal.startAnimation(
            AnimationUtils.loadAnimation(requireContext(), android.R.anim.fade_in)
        )
    }

    private fun showError(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
            .setBackgroundTint(resources.getColor(android.R.color.holo_red_light, null))
            .show()
    }

    private fun showSuccess(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
            .setBackgroundTint(resources.getColor(android.R.color.holo_green_light, null))
            .show()
    }

    private fun showSuccessMessage() {
        val snackbar = Snackbar.make(
            binding.root,
            "Order placed successfully! Thank you for shopping with us.",
            Snackbar.LENGTH_LONG
        )
        snackbar.setBackgroundTint(resources.getColor(android.R.color.holo_green_light, null))
        snackbar.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 