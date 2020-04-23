package AE_ExtensionStarter.buildTypes

import AE_ExtensionStarter.publishCommitStatus
import AE_ExtensionStarter.runAfter
import AE_ExtensionStarter.triggerAfter
import AE_ExtensionStarter.vcsRoots.AE_ExtensionStarter
import AE_ExtensionStarter.withDefaults
import jetbrains.buildServer.configs.kotlin.v2018_2.BuildStep
import jetbrains.buildServer.configs.kotlin.v2018_2.BuildType
import jetbrains.buildServer.configs.kotlin.v2018_2.FailureAction
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.exec
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.v2018_2.triggers.vcs

object AE_ExtensionStarter_IntegrationTests : BuildType({
    uuid = "b98b39c6-99ed-4333-a600-a65b35cbde19"
    name = "Run Integration Tests"

    withDefaults()

    steps {
        exec {
            path = "make"
            arguments = "dockerRun sleep"
        }
        maven {
            goals = "clean verify -DskipITs=false"
            mavenVersion = defaultProvidedVersion()
            jdkHome = "%env.JDK_18%"
            userSettingsSelection = "teamcity-settings"
        }
        exec {
            path = "make"
            arguments = "dockerStop"
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

    //runAfter(AE_ExtensionStarter_Build)
    triggerAfter(AE_ExtensionStarter_Build)

    artifactRules = """
        /opt/buildAgent/work/machine-agent-logs => target/
""".trimIndent()

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