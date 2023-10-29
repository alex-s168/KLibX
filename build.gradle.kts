plugins {
    kotlin("multiplatform") version "1.8.21"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        hostOs == "Mac OS X" -> macosX64("native")
        hostOs == "Linux" -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    nativeTarget.apply {
        compilations.getByName("main") {
            cinterops {
                val hacks by creating {
                    defFile(project.file("src/c/hacks.def"))
                    compilerOpts("-Isrc/c/")
                }
            }
        }
    }

    
    sourceSets {
        val nativeMain by getting
        val nativeTest by getting
    }
}