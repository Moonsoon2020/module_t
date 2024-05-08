plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.t.module_t"
    compileSdk = 34


    defaultConfig {
        applicationId = "com.t.module_t"
        minSdk = 29
        targetSdk = 34
        versionCode = 2
        versionName = "1.0.0.2"

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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation("androidx.coordinatorlayout:coordinatorlayout:1.2.0")
    implementation ("androidx.recyclerview:recyclerview:1.3.2")
    implementation ("com.github.javafaker:javafaker:1.0.2")
    implementation ("androidx.core:core:1.13.1")
    implementation ("com.github.bumptech.glide:glide:4.14.2")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.navigation:navigation-fragment:2.7.7")
    implementation("androidx.navigation:navigation-ui:2.7.7")
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-messaging")
    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
    implementation("com.google.android.gms:play-services-base:18.4.0")
    implementation("com.google.firebase:firebase-storage")
    implementation("androidx.test.services:storage:1.4.2")
    implementation ("org.apache.tika:tika-core:1.27")
    implementation("com.google.firebase:firebase-auth")
    implementation ("org.greenrobot:eventbus:3.2.0")
    implementation("com.google.firebase:firebase-database")
    implementation("com.google.gms:google-services:4.4.1")
    implementation("androidx.mediarouter:mediarouter:1.7.0")
    implementation ("com.google.firebase:firebase-messaging-directboot:24.0.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

}