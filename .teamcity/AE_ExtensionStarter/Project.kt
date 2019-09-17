package AE_ExtensionStarter

import AE_ExtensionStarter.buildTypes.*
import AE_ExtensionStarter.vcsRoots.AE_ExtensionStarter
import jetbrains.buildServer.configs.kotlin.v2018_2.Project
import jetbrains.buildServer.configs.kotlin.v2018_2.projectFeatures.VersionedSettings.BuildSettingsMode.PREFER_SETTINGS_FROM_VCS
import jetbrains.buildServer.configs.kotlin.v2018_2.projectFeatures.VersionedSettings.Format.KOTLIN
import jetbrains.buildServer.configs.kotlin.v2018_2.projectFeatures.VersionedSettings.Mode.ENABLED
import jetbrains.buildServer.configs.kotlin.v2018_2.projectFeatures.versionedSettings

object Project : Project({
    uuid = "69830454-445f-4465-ae8d-923d61970299"
    id("AE_ExtensionStarter")
    parentId("AE")
    name = "AE_ExtensionStarter"

    vcsRoot(AE_ExtensionStarter)
    buildType(AE_ExtensionStarter_Build)
    buildType(AE_ExtensionStarter_Setup)
    buildType(AE_ExtensionStarter_IntegrationTests)
    buildType(AE_ExtensionStarter_Stop)

    features {
        versionedSettings {
            mode = ENABLED
            buildSettingsMode = PREFER_SETTINGS_FROM_VCS
            rootExtId = "${AE_ExtensionStarter.id}"
            showChanges = true
            settingsFormat = KOTLIN
            storeSecureParamsOutsideOfVcs = true
        }
    }

     buildTypesOrder = arrayListOf(
         AE_ExtensionStarter_Build,
         AE_ExtensionStarter_Setup,
         AE_ExtensionStarter_IntegrationTests,
         AE_ExtensionStarter_Stop
    )
})