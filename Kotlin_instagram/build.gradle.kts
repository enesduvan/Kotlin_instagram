// Project level build.gradle.kts
plugins {
    id("com.android.application") version "9.0.1" apply false // Versiyonu kendi projenize göre (örn 8.7.0) güncelleyebilirsiniz
    id("org.jetbrains.kotlin.android") version "2.2.10" apply false
    id("com.google.gms.google-services") version "4.4.4" apply false
    id("androidx.navigation.safeargs.kotlin") version "2.9.6" apply false
}