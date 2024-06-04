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
    val lifecycleVersion = "2.8.1"
    val compose = platform("androidx.compose:compose-bom:2024.05.00")

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:$lifecycleVersion")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation(compose)
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3:1.2.1")
    implementation("androidx.navigation:navigation-compose:2.7.7")

    implementation(project(mapOf("path" to ":voicepingintent")))

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(compose)
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}

tasks.register("rename") {
    doLast {
        android.applicationVariants.all {
            val variant = this
            variant.outputs
                .map { it as com.android.build.gradle.internal.api.BaseVariantOutputImpl }
                .forEach { output ->
                    // AAB rename
                    val originalPath = output.outputFile.parentFile.path
                    println("originalPath:${originalPath}")
                    val outputFile = File("$originalPath/${variant.applicationId}-${variant.buildType.name}-${versionCode()}-${getGitHash.invoke()}.apk")
                    println("outputFile:${outputFile.absolutePath}")
                    output.outputFile.renameTo(outputFile)

                    val aabFile = File("${project.projectDir}/release/app-release.aab")
                    if (aabFile.exists() && System.currentTimeMillis() - aabFile.lastModified() > 600_000) { // 10 minutes
                        println("Please rebuild the aabFile, as it is created for more than 10 minutes. You might have old build that doesn't reflect your latest code.")
                        return@forEach
                    }
                    try {
                        val targetAabFile = File("${project.projectDir}/release/${variant.applicationId}-${versionCode()}-${getGitHash.invoke()}.aab")
                        val success = aabFile.renameTo(targetAabFile)
                        if (success) {
                            println("Success renaming abb. Path:${targetAabFile.absolutePath}")
                        }
                    } catch (_: Exception) { }

                    // APK rename
                    val apkOriginalPath = output.outputFile.parentFile.path
                    println("originalPath:${apkOriginalPath}")
                    val apkOutputFile = File("$apkOriginalPath/${variant.applicationId}-${variant.buildType.name}-${versionCode()}-${getGitHash.invoke()}.apk")
                    if (!apkOutputFile.exists()) {
                        println("${apkOutputFile.absolutePath} does not exist. Skipping apk rename")
                        return@forEach
                    }
                    println("outputFile:${apkOutputFile.absolutePath}")
                    output.outputFile.renameTo(apkOutputFile)

                    val apkFile = File("${project.projectDir}/app/build/outputs/apk/release/app-release.apk")
                    if (apkFile.exists() && System.currentTimeMillis() - apkFile.lastModified() > 600_000) { // 10 minutes
                        println("Please rebuild the apkFile, as it is created for more than 10 minutes. You might have old build that doesn't reflect your latest code.")
                        return@forEach
                    }
                    try {
                        val targetAabFile = File("${project.projectDir}/app/build/outputs/apk/release/${variant.applicationId}-${versionCode()}-${getGitHash.invoke()}.apk")
                        val success = apkFile.renameTo(targetAabFile)
                        if (success) {
                            println("Success renaming apk. Path:${targetAabFile.absolutePath}")
                        }
                    } catch (_: Exception) { }
                }
        }
    }
}