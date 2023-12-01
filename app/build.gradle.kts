import java.io.ByteArrayOutputStream
import java.lang.ProcessBuilder.Redirect

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

val getGitHash: () -> String = {
    val process = ProcessBuilder("git", "rev-parse", "--short=1", "HEAD")
        .redirectOutput(Redirect.PIPE)
        .start()

    val stdout = ByteArrayOutputStream()
    process.inputStream.copyTo(stdout)
    process.waitFor()

    if (process.exitValue() == 0) {
        stdout.toString().trim()
    } else {
        throw (Error("getGitHash error"))
    }
}

val versionCode: () -> Int = {
    val process = ProcessBuilder("git", "rev-list", "--count", "HEAD")
        .redirectOutput(ProcessBuilder.Redirect.PIPE)
        .start()

    val stdout = ByteArrayOutputStream()
    process.inputStream.copyTo(stdout)
    process.waitFor()

    if (process.exitValue() == 0) {
        stdout.toString().trim().toInt()
    } else {
        throw (Error("getVersionCode error"))
    }
}

android {
    namespace = "voiceping.intent.demo"
    compileSdk = 34
    version = versionCode

    defaultConfig {
        applicationId = "voiceping.intent.demo"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    signingConfigs {
        create("release") {
            keyAlias = "voiceping_intent_demo"
            keyPassword = "voiceping_intent_demo"
            storeFile = file("../store/keystore.jks")
            storePassword = "voiceping_intent_demo"
            // organization:  voiceping_intent_demo
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
            isDebuggable = false
        }
    }
    buildFeatures {
        compose = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    val lifecycle_version = "2.6.2"

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:$lifecycle_version")
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3:1.1.2")
    implementation("androidx.navigation:navigation-compose:2.7.4")

    implementation(project(mapOf("path" to ":voicepingintent")))

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}

tasks.whenTaskAdded {
    doLast {
        android.applicationVariants.all { variant ->
            variant.outputs.all { output ->
                val originalPath = output.outputFile.parentFile.path
                val outputFile = File("$originalPath/${variant.applicationId}-${variant.buildType.name}-${versionCode.invoke()}-${getGitHash.invoke()}.apk")
                output.outputFile.renameTo(outputFile)
            }
        }
    }
}