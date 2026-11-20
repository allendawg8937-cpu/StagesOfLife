
#!/usr/bin/env bash
set -euo pipefail

FILE="app/src/main/java/com/example/MainActivity.kt"
BACKUP="${FILE}.backup"

# Save the current file.
cp "$FILE" "$BACKUP"

# Restore the complete screen implementation from the previous version.
git show 0bcf6e13c80e9e592569e4dccb7e2607d0b9f6fd:"$FILE" > "$FILE"

# Apply Firebase initialization and lifecycle fixes.
python3 - <<'PY'
from pathlib import Path

path = Path("app/src/main/java/com/example/MainActivity.kt")
source = path.read_text()

# Add FirebaseApp import.
if "import com.google.firebase.FirebaseApp" not in source:
    source = source.replace(
        "import com.google.firebase.Firebase\n",
        "import com.google.firebase.Firebase\n"
        "import com.google.firebase.FirebaseApp\n"
    )

# Replace MainActivity with the corrected implementation.
start = source.index("class MainActivity : FragmentActivity()")
end = source.index("\n@Composable", start)

activity = '''class MainActivity : FragmentActivity() {

    private lateinit var auth: FirebaseAuth

    private var appIconLaunchSignal by mutableIntStateOf(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase before accessing Authentication.
        val firebaseApp = FirebaseApp.getApps(this).firstOrNull()
            ?: FirebaseApp.initializeApp(this)

        checkNotNull(firebaseApp) {
            "Firebase is not configured. Check app/google-services.json."
        }

        // Configure App Check before Firebase Authentication.
        configureAppCheck(this, intent)

        auth = Firebase.auth

        window.setBackgroundDrawableResource(
            android.R.color.transparent
        )

        enableEdgeToEdge()

        setContent {
            WonderingWorldRoot(
                appIconLaunchSignal = appIconLaunchSignal
            )
        }
    }

    override fun onStart() {
        super.onStart()

        if (!::auth.isInitialized) return

        val currentUser = auth.currentUser

        if (currentUser != null) {
            android.util.Log.d(
                "StagesOfLife",
                "Firebase authentication session active"
            )
        } else {
            android.util.Log.d(
                "StagesOfLife",
                "No authenticated user session"
            )
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)

        // Navigate to the login screen when the app icon is tapped.
        appIconLaunchSignal++
    }
}
'''

source = source[:start] + activity + source[end:]

# Verify that the incomplete placeholders are not present.
for placeholder in (
    "Keep all remaining original LoginScreen arguments",
    "Keep all original SignUpScreen arguments",
    "Keep your original MainDashboardScreen implementation",
    "Keep your original FriendsAndDiscoveryScreen implementation"
):
    if placeholder in source:
        raise SystemExit(f"Incomplete placeholder detected: {placeholder}")

path.write_text(source)
print("Complete MainActivity.kt restored and Firebase changes applied.")
PY

# Display the changed file summary.
git diff --stat -- "$FILE"

echo "Next: review the file, then commit and push."
git add app/src/main/java/com/example/MainActivity.kt
git commit -m "Restore complete app screens and fix Firebase initialization"
git push origin mainimport android.os.Bundle
import androidx.activity.ComponentActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase before accessing Authentication.
        val firebaseApp = FirebaseApp.getApps(this).firstOrNull()
            ?: FirebaseApp.initializeApp(this)

        if (firebaseApp == null) {
            throw IllegalStateException(
                "Firebase is not configured. Check app/google-services.json."
            )
        }

        auth = Firebase.auth

        // KEEP YOUR EXISTING setContent AND APP STARTUP CODE HERE.
    }

    override fun onStart() {
        super.onStart()

        if (!::auth.isInitialized) return

        val currentUser = auth.currentUser

        if (currentUser != null) {
            // User is signed in.
        } else {
            // User is signed out.
        }
    }
}