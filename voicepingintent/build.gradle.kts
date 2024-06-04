plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("maven-publish")
}

val jitpackGroupId = "com.github.adityawibisana"
val jitpackArtifactId = "voiceping-intent"
val jitpackVersion = "0.0.10"

android {
    namespace = "com.smartwalkie.voicepingintent"
    compileSdk = 34

    defaultConfig {
        minSdk = 16

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    publishing {
        singleVariant("release") {
            // if you don't want sources/javadoc, remove these lines
            withSourcesJar()
            withJavadocJar()
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    val lifecycleVersion = "2.8.1"

    implementation("androidx.core:core-ktx:1.13.1")
    implementation ("com.google.code.gson:gson:2.10.1")
    kapt("androidx.lifecycle:lifecycle-common-java8:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}

publishing {
    publications {
        create<MavenPublication>("release") {
            groupId = jitpackGroupId
            artifactId = jitpackArtifactId
            version = jitpackVersion

            afterEvaluate {
                from(components["release"])
            }
        }
        create<MavenPublication>("debug") {
            groupId = jitpackGroupId
            artifactId = jitpackArtifactId
            version = jitpackVersion

            afterEvaluate {
                from(components["debug"])
            }
        }
    }
}
