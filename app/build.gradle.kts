plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.codycactus"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.codycactus"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation ("com.microsoft.cognitiveservices.speech:client-sdk:1.37.0")
    implementation ("androidx.room:room-rxjava2:2.4.3")
    implementation ("io.reactivex.rxjava2:rxandroid:2.1.1")
    implementation ("androidx.lifecycle:lifecycle-reactivestreams:2.5.1")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.room.runtime)
    implementation(libs.core)

    testImplementation(libs.junit)
    testImplementation(project(":app"))
    testImplementation(project(":app"))
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    annotationProcessor(libs.room.compiler)

    // Room components
    implementation ("androidx.room:room-runtime:2.4.0")
    annotationProcessor ("androidx.room:room-compiler:2.4.0")
    testImplementation ("androidx.room:room-testing:2.4.0")

    // JUnit
    testImplementation ("junit:junit:4.13.2")

    // AndroidX Testing
    testImplementation ("androidx.test:core:1.4.0")
    testImplementation ("androidx.test.ext:junit:1.1.3")
    testImplementation ("androidx.test:runner:1.4.0")
    testImplementation ("androidx.test:rules:1.4.0")

    // Robolectric (to run tests on Android without emulator)
    testImplementation ("org.robolectric:robolectric:4.7.3")

    // LiveData testing utilities
    testImplementation ("androidx.arch.core:core-testing:2.1.0")
    androidTestImplementation("androidx.arch.core:core-testing:2.1.0")
    // Espresso
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.4.0")
}