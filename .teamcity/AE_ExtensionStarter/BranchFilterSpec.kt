package AE_ExtensionStarter

object BranchFilterSpec {
    val allBranches = """
                +:*
                """.trimIndent()

    val releaseBranches = """
                +:master
                +:release/*
                """.trimIndent()

    val pullRequests = """
                +:pull-requests/*
                """.trimIndent()
}