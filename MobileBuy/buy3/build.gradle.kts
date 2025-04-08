plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    signing
    `maven-publish`
}

version = "2025.4.0"

android {
    namespace = "com.shopify.buy3"
    compileSdk = 35

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        buildConfigField("String", "BUY_SDK_VERSION", "\"${project.version}\"")
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
            withSourcesJar()
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)

    implementation(
        platform(libs.okhttp.bom)
    )
    implementation(libs.okhttp.core)
    implementation(libs.okhttp.logging)

    implementation(libs.gson)
    implementation(libs.timber)

    testImplementation(libs.junit)
    testImplementation(libs.mockito)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.truth)
    testImplementation(libs.okhttp.mockwebserver)

    androidTestImplementation(libs.androidx.junit)
}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "com.shopify.mobilebuysdk"
            artifactId = "buy3"

            pom {
                name = "Mobile Buy SDK"
                description = "Shopify's Mobile Buy SDK makes it simple to sell physical products inside your mobile app."
                url = "https://github.com/Shopify/mobile-buy-sdk-android"
                developers {
                    developer {
                        name = "Shopify Inc."
                    }
                }
                licenses {
                    license {
                        name = "MIT"
                        url = "https://opensource.org/licenses/MIT"
                    }
                }
                scm {
                    url = "https://github.com/Shopify/mobile-buy-sdk-android"
                }
            }

            afterEvaluate {
                from(components["release"])
            }
        }
    }
    repositories {
        maven {
            name = "GitHub"

            url = uri("https://maven.pkg.github.com/Shopify/mobile-buy-sdk-android")

            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
        maven {
            name = "OSSRH-Staging"

            url = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")

            val ossrhUsername: String? by project
            val ossrhPassword: String? by project

            credentials {
                username = ossrhUsername
                password = ossrhPassword
            }
       }
    }
}

signing {
    val signingKeyId: String? by project
    val signingKey: String? by project
    val signingPassword: String? by project

    useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)

    sign(publishing.publications["release"])
}
