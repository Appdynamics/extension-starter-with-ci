package AE_ExtensionStarter.buildTypes

import AE_ExtensionStarter.publishCommitStatus
import AE_ExtensionStarter.vcsRoots.AE_ExtensionStarter
import jetbrains.buildServer.configs.kotlin.v2018_2.BuildType
import jetbrains.buildServer.configs.kotlin.v2018_2.FailureAction
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.exec
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.v2018_2.triggers.vcs

object AE_ExtensionStarter_Setup : BuildType({
    uuid = "f5aff066-5523-4ab7-8b77-bf8108ceb282"
    name = "Setup docker containers"

    vcs {
        root(AE_ExtensionStarter)
        cleanCheckout = true
    }

    steps {
        exec {
            path = "make"
            arguments = "dockerRun sleep"
        }
    }

    triggers {
        vcs {
        }
    }

    dependencies {
        dependency(AE_ExtensionStarter_Build) {
            snapshot{
                onDependencyFailure = FailureAction.FAIL_TO_START
                runOnSameAgent = true
            }
            artifacts {
                artifactRules = """
                +:target/ExtensionStarterMonitor-*.zip => target/
            """.trimIndent()
            }
        }
    }

    publishCommitStatus()
})