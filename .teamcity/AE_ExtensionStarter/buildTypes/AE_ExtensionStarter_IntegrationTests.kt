package AE_ExtensionStarter.buildTypes

import AE_ExtensionStarter.publishCommitStatus
import AE_ExtensionStarter.vcsRoots.AE_ExtensionStarter
import jetbrains.buildServer.configs.kotlin.v2018_2.BuildType
import jetbrains.buildServer.configs.kotlin.v2018_2.FailureAction
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.exec
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.v2018_2.triggers.vcs

object AE_ExtensionStarter_IntegrationTests : BuildType({
    uuid = "b98b39c6-99ed-4333-a600-a65b35cbde19"
    name = "Run Integration Tests"

    vcs {
        root(AE_ExtensionStarter)
        cleanCheckout = true
    }

    steps {
        maven {
            goals = "clean verify -DskipITs=false"
            mavenVersion = defaultProvidedVersion()
            jdkHome = "%env.JDK_18%"
        }
    }

    triggers {
        vcs {
        }
    }

    dependencies {
        dependency(AE_ExtensionStarter_Setup) {
            snapshot {
                onDependencyFailure = FailureAction.FAIL_TO_START
                runOnSameAgent = true
            }
        }
    }

    publishCommitStatus()
})