package AE_ExtensionStarter

object BranchFilterSpec {
    /** A filter string for all branches */
    val allBranches: String = """
    +:*
    """.trimIndent()

    /**
     * A filter string for the master branch
     */
    val masterBranch: String = """
    +:master
    """.trimIndent()

    /** A filter string for release branches */
    val releaseBranches: String = """
    +:master
    +:release/*
    """.trimIndent()

    /** A filter string for pull request branches */
    val pullRequests: String = """
    +:pull-requests/*
    """.trimIndent()

    /** A filter string for the default branch */
    val defaultBranch: String = """
    +:<default>
    """.trimIndent()
}