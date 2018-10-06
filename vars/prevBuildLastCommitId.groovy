def call() {
    def prev = currentBuild.previousBuild
    def items = null
    def result = null
    if (prev != null && prev.changeSets != null && prev.changeSets.size() && prev.changeSets[prev.changeSets.size() - 1].items.length) {
        items = prev.changeSets[prev.changeSets.size() - 1].items
        result = items[items.length - 1].commitId
    }
    return result
}