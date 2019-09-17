package AE_ExtensionStarter.vcsRoots

import jetbrains.buildServer.configs.kotlin.v2018_2.vcs.GitVcsRoot

object AE_ExtensionStarter : GitVcsRoot({
    uuid = "27c6f3a5-e223-4103-886e-716f25ef7442"
    id("AE_ExtensionStarter")
    name = "AE_ExtensionStarter"
    url = "ssh://git@bitbucket.corp.appdynamics.com:7999/ae/extension-starter.git"
    pushUrl = "ssh://git@bitbucket.corp.appdynamics.com:7999/ae/extension-starter.git"
    authMethod = uploadedKey {
        uploadedKey = "TeamCity BitBucket Key"
    }
    agentCleanPolicy = AgentCleanPolicy.ALWAYS
    branchSpec = """
    +:refs/heads/(master)
    +:refs/(pull-requests/*)/from
    """.trimIndent()
})