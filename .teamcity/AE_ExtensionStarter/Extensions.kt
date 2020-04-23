package AE_ExtensionStarter

import AE_ExtensionStarter.vcsRoots.AE_ExtensionStarter
import jetbrains.buildServer.configs.kotlin.v2018_2.BuildType
import jetbrains.buildServer.configs.kotlin.v2018_2.VcsRoot
import jetbrains.buildServer.configs.kotlin.v2018_2.buildFeatures.commitStatusPublisher
import jetbrains.buildServer.configs.kotlin.v2018_2.triggers.finishBuildTrigger
import jetbrains.buildServer.configs.kotlin.v2018_2.FailureAction
import jetbrains.buildServer.configs.kotlin.v2018_2.BuildTypeSettings

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

/*fun BuildType.triggerAfter(buildType: BuildType, branchFilterSpec: String = BranchFilterSpec.allBranches) {

    triggers {
        finishBuildTrigger {
            buildType(buildType)
            successfulOnly = true
            branchFilter = branchFilterSpec
        }
    }
}*/

fun BuildTypeSettings.triggerAfter(buildJob: BuildTypeSettings, branches: String = BranchFilterSpec.masterBranch) {
    triggers {
        finishBuildTrigger {
            buildType = "${buildJob.id}"
            successfulOnly = true
            branchFilter = branches
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