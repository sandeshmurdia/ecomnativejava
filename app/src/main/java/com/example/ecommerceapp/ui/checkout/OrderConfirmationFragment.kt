package com.example.ecommerceapp.ui.checkout

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.ecommerceapp.R
import com.example.ecommerceapp.databinding.FragmentOrderConfirmationBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

@AndroidEntryPoint
class OrderConfirmationFragment : Fragment() {

    private var _binding: FragmentOrderConfirmationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderConfirmationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOrderDetails()
        setupClickListeners()
        startSuccessAnimation()
    }

    private fun setupOrderDetails() {
        // Generate random order number
        val orderNumber = generateOrderNumber()
        binding.tvOrderNumber.text = orderNumber

        // Calculate and set estimated delivery date
        val estimatedDelivery = calculateEstimatedDelivery()
        binding.tvEstimatedDelivery.text = estimatedDelivery
    }

    private fun startSuccessAnimation() {
        // Scale animation for success icon
        val scaleX = ObjectAnimator.ofFloat(binding.successAnimation, View.SCALE_X, 0.5f, 1.2f, 1f)
        val scaleY = ObjectAnimator.ofFloat(binding.successAnimation, View.SCALE_Y, 0.5f, 1.2f, 1f)
        
        // Rotation animation
        val rotation = ObjectAnimator.ofFloat(binding.successAnimation, View.ROTATION, 0f, 360f)
        
        // Combine animations
        AnimatorSet().apply {
            playTogether(scaleX, scaleY, rotation)
            interpolator = OvershootInterpolator()
            duration = 1000
            start()
        }

        // Fade in order details after animation
        binding.orderDetailsCard.animate()
            .alpha(1f)
            .setStartDelay(1000)
            .setDuration(500)
            .start()
    }

    private fun generateOrderNumber(): String {
        val timestamp = System.currentTimeMillis()
        val random = Random.nextInt(1000, 9999)
        return "ORDER-${timestamp.toString().takeLast(6)}-$random"
    }

    private fun calculateEstimatedDelivery(): String {
        val calendar = Calendar.getInstance()
        // Add 3-5 business days
        val deliveryDays = Random.nextInt(3, 6)
        var daysAdded = 0
        var currentDay = 0

        while (daysAdded < deliveryDays) {
            currentDay++
            calendar.add(Calendar.DAY_OF_MONTH, 1)
            // Skip weekends
            if (calendar.get(Calendar.DAY_OF_WEEK) !in arrayOf(
                    Calendar.SATURDAY,
                    Calendar.SUNDAY
                )
            ) {
                daysAdded++
            }
        }

        val dateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    private fun setupClickListeners() {
        binding.btnContinueShopping.setOnClickListener {
            // Navigate back to home fragment
            findNavController().navigate(R.id.action_orderConfirmationFragment_to_homeFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 