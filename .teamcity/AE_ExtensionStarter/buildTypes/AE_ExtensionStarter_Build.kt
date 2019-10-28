package AE_ExtensionStarter.buildTypes

import AE_ExtensionStarter.publishCommitStatus
import AE_ExtensionStarter.vcsRoots.AE_ExtensionStarter
import AE_ExtensionStarter.withDefaults
import jetbrains.buildServer.configs.kotlin.v2018_2.BuildType
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.v2018_2.triggers.vcs

object AE_ExtensionStarter_Build : BuildType({
    uuid = "0b281445-b932-47b2-b6ba-d097e1563c41"
    name = "Extension Starter Build"

    withDefaults()

    steps {
        maven {
            goals = "dependency:purge-local-repository"
            mavenVersion = defaultProvidedVersion()
            jdkHome = "%env.JDK_18%"
        }
        maven {
            goals = "clean install -U"
            mavenVersion = defaultProvidedVersion()
            jdkHome = "%env.JDK_18%"
        }
    }

    triggers {
        vcs {
        }
    }

    artifactRules = """
    +:target/ExtensionStarterMonitor-*.zip => target/
""".trimIndent()

    publishCommitStatus()
})