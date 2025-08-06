
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
}

kotlin {
    jvm()
    
    sourceSets {
        commonMain.dependencies {
            implementation(compose.components.resources)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
        }
    }
}


compose.desktop {
    application {
        mainClass = "org.cdy.pimage.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Pimage"
            packageVersion = "1.0.0"
            description="预览图片并将窗口置顶"
            copyright ="© 2024 一粒焦炭"
            vendor="一粒焦炭"

            windows {
                dirChooser = true
                upgradeUuid = "66e0b2fa-988b-4425-96a8-838410c63ead"
                iconFile.set(project.file("src/jvmMain/composeResources/drawable/icon.ico"))
                // 使用正确的函数签名配置文件关联
                fileAssociation(
                    mimeType = "image/jpeg",
                    extension = "jpg",
                    description = "JPEG 图像文件",
                    iconFile = project.file("src/jvmMain/composeResources/drawable/icon.ico")
                )
                fileAssociation(
                    mimeType = "image/jpeg",
                    extension = "jpeg",
                    description = "JPEG 图像文件",
                    iconFile = project.file("src/jvmMain/composeResources/drawable/icon.ico")
                )

                fileAssociation(
                    mimeType = "image/png",
                    extension = "png",
                    description = "PNG 图像文件",
                    iconFile = project.file("src/jvmMain/composeResources/drawable/icon.ico")
                )

                fileAssociation(
                    mimeType = "image/gif",
                    extension = "gif",
                    description = "GIF 动画文件",
                    iconFile = project.file("src/jvmMain/composeResources/drawable/icon.ico")
                )
            }
        }
    }
}

tasks.register("pkg") {
    group = "pkg" // 或者其他自定义组名，如 "Installer"
    description = "Builds the Msi installer for the release version."
    dependsOn(":composeApp:packageReleaseMsi")
}
