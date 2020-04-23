package AE_ExtensionStarter

import AE_ExtensionStarter.vcsRoots.AE_ExtensionStarter
import jetbrains.buildServer.configs.kotlin.v2018_2.BuildType
import jetbrains.buildServer.configs.kotlin.v2018_2.VcsRoot
import jetbrains.buildServer.configs.kotlin.v2018_2.buildFeatures.commitStatusPublisher
import import jetbrains.buildServer.configs.kotlin.v2018_2.triggers.finishBuildTrigger
import jetbrains.buildServer.configs.kotlin.v2018_2.FailureAction

fun BuildType.publishCommitStatus() {
    features {
        commitStatusPublisher {
            vcsRootExtId = "${AE_ExtensionStarter.id}"
            publisher = bitbucketServer {
                url = "%env.BITBUCKET_SERVER%"
                userName = "%env.BITBUCKET_USERNAME%"
                password = "%env.BITBUCKET_PASSWORD%"
            }
        }
    }
}

fun BuildType.withDefaults() {
    vcs {
        root(AE_ExtensionStarter)
        cleanCheckout = true
    }

    requirements {
        matches("env.AGENT_OS", "Linux")
    }
}

fun BuildType.triggerAfter(buildType: BuildType, branchFilterSpec: String = BranchFilterSpec.branchFilterSpec) {
    buildNumberPattern = "%dep." + buildType.extId + ".system.build.number%"

    triggers {
        finishBuildTrigger {
            buildTypeExtId = buildType.extId
            successfulOnly = true
            branchFilter = branchFilterSpec
        }
    }
}

fun BuildType.runAfter(buildTypes: List<BuildType>) {
    buildTypes.forEach {
        this.dependencies.dependency(it) {
            snapshot {
                onDependencyFailure = FailureAction.FAIL_TO_START
            }
        }
    }
}

fun BuildType.runAfter(buildType: BuildType) {
    runAfter(listOf(buildType))
}