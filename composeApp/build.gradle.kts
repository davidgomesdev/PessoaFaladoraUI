import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.serialization)
}

kotlin {
    jvm()

    js {
        browser()
        binaries.executable()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.content.regotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.napier)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jsMain.dependencies {
            implementation(libs.ktor.client.js)
            implementation(devNpm("copy-webpack-plugin", "12.0.2"))
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
            implementation(libs.ktor.client.okhttp)
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "pessoafaladora.composeapp.generated.resources"
    generateResClass = always
}

val backendStaticDir = rootProject.layout.projectDirectory.dir("../PessoaFaladora/src/main/resources/web/static")
val backendUiResourcesDir = rootProject.layout.projectDirectory.dir("../PessoaFaladora/uiResources/composeResources")


val copyWebpackToBackend by tasks.registering(Copy::class) {
    group = "deployment"
    description =
        "Copies the JS production webpack bundle (top-level files only) to the PessoaFaladora backend static resources"
    dependsOn("jsBrowserProductionWebpack")
    from(layout.buildDirectory.dir("kotlin-webpack/js/productionExecutable")) {
        // index.html is already in template (with templating) and composeResources is copied in the other task
        val webpackFilesToNotCopy = listOf("index.html", "composeResources")
        exclude { webpackFilesToNotCopy.contains(it.name) }
    }
    into(backendStaticDir)
}

val copyResourcesToBackend by tasks.registering(Copy::class) {
    group = "deployment"
    description = "Copies compiled UI resources to the PessoaFaladora backend uiResources directory"
    dependsOn("jsBrowserProductionWebpack")
    from(layout.buildDirectory.dir("kotlin-webpack/js/productionExecutable/composeResources"))
    into(backendUiResourcesDir)
}

tasks.register("deployToBackend") {
    group = "deployment"
    description = "Builds the JS bundle and deploys all assets to the PessoaFaladora backend"
    dependsOn(copyWebpackToBackend, copyResourcesToBackend)
}

compose.desktop {
    application {
        mainClass = "me.davidgomesdev.pessoafaladora.ui.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "me.davidgomesdev.pessoafaladora.ui"
            packageVersion = "1.0.0"
        }
    }
}
