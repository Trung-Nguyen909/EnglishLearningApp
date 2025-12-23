plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.englishlearningapp"
    compileSdk = 36 // Lưu ý: SDK 36 là bản Preview (Android 16), nếu lỗi có thể hạ xuống 34 hoặc 35

    defaultConfig {
        applicationId = "com.example.englishlearningapp"
        minSdk = 24
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    // >>> THÊM ĐOẠN NÀY ĐỂ SỬA LỖI MERGE RESOURCES <<<
    // ... Bên trong khối android { ... }

    packaging {
        resources {
            // Chú ý: Dùng dấu nháy kép " " thay vì nháy đơn ' '
            excludes += "META-INF/INDEX.LIST"
            excludes += "META-INF/DEPENDENCIES"
            excludes += "META-INF/LICENSE"
            excludes += "META-INF/LICENSE.txt"
            excludes += "META-INF/license.txt"
            excludes += "META-INF/NOTICE"
            excludes += "META-INF/NOTICE.txt"
            excludes += "META-INF/notice.txt"
            excludes += "META-INF/ASL2.0"
            excludes += "META-INF/*.kotlin_module"
            excludes += "META-INF/io.netty.versions.properties"
        }
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // Kiểm tra lại dòng này: Đây thường là plugin, không phải implementation.
    // Nếu app chạy bình thường thì giữ nguyên, nếu lỗi thì xem lại document của Firebase.
    implementation(libs.firebase.appdistribution.gradle)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Các thư viện UI & Logic
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // Retrofit & Networking
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")

    // Calendar & Date
    implementation("com.github.prolificinteractive:material-calendarview:2.0.1")
    implementation("com.jakewharton.threetenabp:threetenabp:1.3.1")

    // Image Loading
    implementation("com.github.bumptech.glide:glide:4.16.0")
}