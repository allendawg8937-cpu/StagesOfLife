
package com.example

import android.app.Application
import com.google.firebase.FirebaseApp

/**
 * Application used by Robolectric tests.
 *
 * Initializes Firebase before MainActivity accesses
 * Firebase Authentication, Firestore, or App Check.
 */
class TestFirebaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this)
        }
    }
}
