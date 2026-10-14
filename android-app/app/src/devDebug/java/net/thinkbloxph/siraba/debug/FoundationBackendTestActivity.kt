package net.thinkbloxph.siraba.debug

import android.app.Activity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.FirebaseFunctionsException
import net.thinkbloxph.siraba.BuildConfig

/** SB-P1-014 TEMPORARY DEV-DEBUG-ONLY diagnostic. Never ship or commit this file.
 * The screen accepts test credentials solely on-device. It does not store or log them.
 * No server writes, Firestore rule changes, booking creation, or payment calls.
 */
class FoundationBackendTestActivity : Activity() {
    private lateinit var status: TextView
    private lateinit var signInButton: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)

        if (!BuildConfig.DEBUG || BuildConfig.ENVIRONMENT != "DEV" || BuildConfig.USE_FIREBASE_EMULATORS) {
            Log.e("SiraBaBackendTest", "REFUSED: requires live DEV-debug build")
            finish()
            return
        }

        val app = FirebaseApp.getApps(this).firstOrNull()
        if (app?.options?.projectId != "siraba-dev") {
            Log.e("SiraBaBackendTest", "REFUSED: wrong Firebase project")
            finish()
            return
        }

        auth = FirebaseAuth.getInstance()
        // Prevent retaining a test session from any prior run.
        auth.signOut()

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(44, 45, 44, 45)
        }
        val heading = TextView(this).apply {
            text = "Sira Ba? | DEV backend validation"
            textSize = 23f
        }
        val explanation = TextView(this).apply {
            text = "Temporary test screen (DEV debug only). Enter your dedicated Firebase TEST account. No credentials will be logged or saved by this screen."
            textSize = 15f
            setPadding(0, 24, 0, 24)
        }
        val email = EditText(this).apply {
            hint = "DEV test email"
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            isSingleLine = true
            isSaveEnabled = false
        }
        val password = EditText(this).apply {
            hint = "DEV test password"
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            isSingleLine = true
            isSaveEnabled = false
        }
        signInButton = Button(this).apply { text = "Sign in and verify backend" }
        status = TextView(this).apply {
            text = "Not run yet. This test does not create bookings or take payments."
            textSize = 16f
            setPadding(0, 20, 0, 20)
        }
        val close = Button(this).apply {
            text = "Close and sign out"
            setOnClickListener {
                auth.signOut()
                finish()
            }
        }
        root.addView(heading)
        root.addView(explanation)
        root.addView(email)
        root.addView(password)
        root.addView(signInButton)
        root.addView(status)
        root.addView(close)
        setContentView(ScrollView(this).apply {
            addView(root)
            isFillViewport = true
        })

        signInButton.setOnClickListener {
            val enteredEmail = email.text.toString().trim()
            val enteredPassword = password.text.toString()
            if (enteredEmail.isBlank() || enteredPassword.isBlank()) {
                status.text = "Please enter the DEV test email and password."
                return@setOnClickListener
            }
            signInButton.isEnabled = false
            status.text = "Signing in to siraba-dev..."
            // Don't leave credential strings in editable view state while the request runs.
            email.text.clear()
            password.text.clear()
            auth.signOut()
            auth.signInWithEmailAndPassword(enteredEmail, enteredPassword)
                .addOnCompleteListener(this) { login ->
                    if (!login.isSuccessful || auth.currentUser == null) {
                        val err = login.exception
                        val code = (err as? FirebaseAuthException)?.errorCode
                            ?: err?.javaClass?.simpleName ?: "UNKNOWN"
                        status.text = "FAIL: Authentication ($code). Check test credentials and Email/Password provider."
                        Log.w("SiraBaBackendTest", "FAIL: auth code=$code")
                        auth.signOut()
                        signInButton.isEnabled = true
                    } else {
                        // First verify that both Firebase SDKs can actually obtain credentials.
                        // Never display, persist or log the token strings themselves.
                        status.text = "Firebase sign-in PASS. Checking ID token..."
                        auth.currentUser!!.getIdToken(true).addOnCompleteListener(this) { idTask ->
                            if (!idTask.isSuccessful || idTask.result?.token.isNullOrBlank()) {
                                val errorName = idTask.exception?.javaClass?.simpleName ?: "MISSING_TOKEN"
                                status.text = "FAIL: Firebase ID token ($errorName)."
                                Log.w("SiraBaBackendTest", "FAIL: Firebase ID token $errorName")
                                auth.signOut()
                                signInButton.isEnabled = true
                            } else {
                                status.text = "Firebase ID token PASS. Checking App Check..."
                                FirebaseAppCheck.getInstance().getAppCheckToken(true)
                                    .addOnCompleteListener(this) { appTask ->
                                        if (!appTask.isSuccessful || appTask.result?.token.isNullOrBlank()) {
                                            val errorName = appTask.exception?.javaClass?.simpleName ?: "MISSING_TOKEN"
                                            status.text = "FAIL: App Check token ($errorName)."
                                            Log.w("SiraBaBackendTest", "FAIL: App Check token $errorName")
                                            auth.signOut()
                                            signInButton.isEnabled = true
                                        } else {
                                            // Do not trust a nonempty string as proof of a valid App Check JWT.
                                            // Checking only JWT structure; server performs true signature validation.
                                            val checkResult = appTask.result!!
                                            val jwtSections = checkResult.token.split('.')
                                            val looksLikeJwt = jwtSections.size == 3 && jwtSections.all { part ->
                                                part.isNotEmpty() && part.all { ch ->
                                                    ch.isLetterOrDigit() || ch == '-' || ch == '_'
                                                }
                                            }
                                            val expired = checkResult.expireTimeMillis <= System.currentTimeMillis()
                                            if (!looksLikeJwt || expired) {
                                                status.text = "FAIL: App Check token not usable (JWT shape/expiry). Check debug registration and network."
                                                Log.w("SiraBaBackendTest", "FAIL: App Check JWT format; sections=${jwtSections.size} expired=$expired. Token withheld")
                                                auth.signOut()
                                                signInButton.isEnabled = true
                                            } else {
                                            Log.i("SiraBaBackendTest", "Firebase Auth PASS; refreshed App Check JWT shape PASS; invoking callable")
                                            status.text = "Firebase sign-in / ID token / App Check PASS. Calling server..."
                                            FirebaseFunctions.getInstance("asia-southeast1")
                            .getHttpsCallable("getFoundationStatus")
                            .call(emptyMap<String, Any>())
                            .addOnCompleteListener(this) { invocation ->
                                if (!invocation.isSuccessful) {
                                    val err = invocation.exception
                                    val code = (err as? FirebaseFunctionsException)?.code?.name
                                        ?: err?.javaClass?.simpleName ?: "UNKNOWN"
                                    status.text = "FAIL: Callable request ($code)."
                                    Log.w("SiraBaBackendTest", "FAIL: callable code=$code")
                                } else {
                                    @Suppress("UNCHECKED_CAST")
                                    val body = invocation.result?.data as? Map<String, Any?>
                                    @Suppress("UNCHECKED_CAST")
                                    val billing = body?.get("billing") as? Map<String, Any?>
                                    val passed = body != null && billing != null &&
                                        body["authenticated"] == true &&
                                        body["bookingEnabled"] == false &&
                                        billing["paymentsEnabled"] == false &&
                                        billing["paymentMode"] == "DISABLED" &&
                                        (billing["feeAmountMinor"] as? Number)?.toInt() == 0
                                    if (passed) {
                                        status.text = "PASS: Auth + App Check + callable.\n" +
                                            "authenticated=true\nbookingEnabled=false\n" +
                                            "paymentsEnabled=false\nfeeAmountMinor=0\n" +
                                            "Signed out automatically."
                                        Log.i("SiraBaBackendTest", "PASS: authenticated callable, booking OFF, payments OFF, fee ZERO")
                                    } else {
                                        status.text = "FAIL: Unexpected backend policy response. Signed out."
                                        Log.e("SiraBaBackendTest", "FAIL: unexpected backend response (values withheld)")
                                    }
                                }
                                auth.signOut()
                                signInButton.isEnabled = true
                            }
                                            }
                                        }
                                    }
                            }
                        }
                    }
                }
        }
    }

    override fun onDestroy() {
        if (::auth.isInitialized) auth.signOut()
        super.onDestroy()
    }
}
