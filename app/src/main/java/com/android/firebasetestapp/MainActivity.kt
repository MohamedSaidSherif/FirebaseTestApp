package com.android.firebasetestapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.android.firebasetestapp.databinding.ActivityMainBinding
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        firebaseAnalytics = Firebase.analytics

        binding.activityMainButton.setOnClickListener {
            firebaseAnalytics.logEvent("SHERIF") {
                param(FirebaseAnalytics.Param.ITEM_ID, "1234")
                param(FirebaseAnalytics.Param.ITEM_NAME, "sherif")
                param(FirebaseAnalytics.Param.CONTENT_TYPE, "image")
            }
        }
    }
}