plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}
// Local Firebase Console configuration remains gitignored. No dependency on old projects.
// With only DEV configured, :app:assembleDevDebug works; other flavors require their own JSON.
if (listOf("dev", "staging", "prod").any { file("src/$it/google-services.json").exists() }) {
    apply(plugin = "com.google.gms.google-services")
    apply(plugin = "com.google.firebase.crashlytics")
}
val useEmulators = providers.gradleProperty("useFirebaseEmulators").orNull == "true"
android {
    namespace = "net.thinkbloxph.siraba"
    compileSdk = 36
    defaultConfig {
        applicationId = "net.thinkbloxph.siraba"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "0.1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("Boolean", "USE_FIREBASE_EMULATORS", "false")
    }
    flavorDimensions += "environment"
    productFlavors {
        create("dev") {
            dimension = "environment"
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
            buildConfigField("String", "ENVIRONMENT", "\"DEV\"")
            buildConfigField("Boolean", "USE_FIREBASE_EMULATORS", "$useEmulators")
        }
        create("staging") {
            dimension = "environment"
            applicationIdSuffix = ".staging"
            versionNameSuffix = "-staging"
            buildConfigField("String", "ENVIRONMENT", "\"STAGING\"")
        }
        create("prod") {
            dimension = "environment"
            buildConfigField("String", "ENVIRONMENT", "\"PROD\"")
        }
    }
    // Build type takes priority over the DEV flavor. Emulator routing must NEVER
    // be packaged into a release APK, including the devRelease variant.
    buildTypes {
        getByName("release") {
            buildConfigField("Boolean", "USE_FIREBASE_EMULATORS", "false")
        }
    }
    buildFeatures { compose = true; buildConfig = true }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
// DEV debug can opt in to local Firebase emulators.
// The release build-type override above forces emulator routing OFF for all releases.

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.preview)
    implementation(libs.compose.material3)
    implementation(libs.navigation.compose)
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.coroutines.android)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)
    // SB-P1-016: minimum 2nd-gen callable client SDK 22.1.1; older BoM maps 22.1.0.
    implementation("com.google.firebase:firebase-functions:22.1.1")
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.appcheck)
    debugImplementation(libs.firebase.appcheck.debug)
    releaseImplementation(libs.firebase.appcheck.playintegrity)
    debugImplementation(libs.compose.tooling)
    debugImplementation(libs.compose.test.manifest)
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.compose.test)
    androidTestImplementation("androidx.test:runner:1.6.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
}
