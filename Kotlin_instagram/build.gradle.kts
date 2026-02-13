// Project level build.gradle.kts
plugins {
    id("com.android.application") version "8.2.2" apply false // Versiyonu kendi projenize göre (örn 8.7.0) güncelleyebilirsiniz
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    id("com.google.gms.google-services") version "4.4.4" apply false
    id("androidx.navigation.safeargs.kotlin") version "2.7.7" apply false
}