package com.jonasgerdes.stoppelmap.fcm

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging

fun subscribeToNewsMessages() {
    //TODO: Only for testing purposes -> refactor this
    FirebaseMessaging.getInstance().subscribeToTopic("news").addOnCompleteListener {
        if (it.isSuccessful) {
            Log.d("Firebase", "Subscription to news successful")
        } else {
            Log.d("Firebase", "Subscription to news failed: ${it.exception?.message}")
        }
    }
}