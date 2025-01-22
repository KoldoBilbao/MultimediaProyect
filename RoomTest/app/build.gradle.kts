plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.roomtest"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.roomtest"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    // Dependencias estándar de Android
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    // Dependencias para pruebas
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Room dependencies (usando annotationProcessor para Room en Java)
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler) // Usar annotationProcessor para Room en Java

    // Retrofit for networking
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)

    // OkHttp for logging (opcional pero útil para depuración)
    implementation(libs.okhttp.logging)

    // Gson para convertir JSON a objetos Java
    implementation(libs.gson)

    implementation (libs.glide)
    annotationProcessor (libs.compiler)

    implementation (libs.mpandroidchart)


}
