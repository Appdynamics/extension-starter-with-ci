package AE_ExtensionStarter.buildTypes

import AE_ExtensionStarter.publishCommitStatus
import AE_ExtensionStarter.vcsRoots.AE_ExtensionStarter
import AE_ExtensionStarter.withDefaults
import jetbrains.buildServer.configs.kotlin.v2018_2.BuildStep
import jetbrains.buildServer.configs.kotlin.v2018_2.BuildType
import jetbrains.buildServer.configs.kotlin.v2018_2.FailureAction
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.exec
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.v2018_2.triggers.vcs

object AE_ExtensionStarter_WorkbenchTest : BuildType({
    uuid = "4ba3cb17-9e3f-4966-ac96-7a546d426023"
    name = "Test Workbench mode"

    withDefaults()

    steps {
        exec {
            path = "make"
            arguments = "workbenchTest"
        }
        exec {
            executionMode = BuildStep.ExecutionMode.ALWAYS
            path = "make"
            arguments = "dockerClean"
        }
    }

    triggers {
        vcs {
        }
    }

    dependencies {
        dependency(AE_ExtensionStarter_Build) {
            snapshot {
                onDependencyFailure = FailureAction.FAIL_TO_START
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