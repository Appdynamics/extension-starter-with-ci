package AE_ExtensionStarter.buildTypes

import AE_ExtensionStarter.publishCommitStatus
import AE_ExtensionStarter.vcsRoots.AE_ExtensionStarter
import jetbrains.buildServer.configs.kotlin.v2018_2.BuildType
import jetbrains.buildServer.configs.kotlin.v2018_2.FailureAction
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.exec
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.v2018_2.triggers.vcs

object AE_ExtensionStarter_Stop : BuildType({
    uuid = "7204c2b9-fc0e-4a7d-8f4e-4000050105e7"
    name = "Stop and remove all docker containers"

    vcs {
        root(AE_ExtensionStarter)
        cleanCheckout = true
    }

    steps {
        exec {
            path = "make"
            arguments = "dockerStop"
        }
    }

    triggers {
        vcs {
        }
    }

    dependencies {
        dependency(AE_ExtensionStarter_Setup) {
            snapshot {
                onDependencyFailure = FailureAction.IGNORE
                runOnSameAgent = true
            }
        }
        dependency(AE_ExtensionStarter_IntegrationTests) {
            snapshot {
                onDependencyCancel = FailureAction.IGNORE
                onDependencyFailure = FailureAction.IGNORE
                runOnSameAgent = true
            }
        }
    }

    publishCommitStatus()
})