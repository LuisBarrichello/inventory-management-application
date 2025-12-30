plugins {
    id("com.android.application") version "8.13.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.23" apply false
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
    id("com.google.devtools.ksp") version "1.9.23-1.0.19" apply false
    alias(libs.plugins.google.firebase.appdistribution) apply false
    alias(libs.plugins.google.gms.google.services) apply false
}