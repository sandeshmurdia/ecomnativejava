package com.example.ecommerceapp

import android.app.Application
import com.zipy.zipyandroid.Zipy
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class EcommerceApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize Zipy SDK
        Zipy.setApplicationContext(this)
        Zipy.init("6d2b9556")
    }
} 