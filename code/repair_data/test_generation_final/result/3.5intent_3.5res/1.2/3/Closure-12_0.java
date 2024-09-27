private boolean hasExceptionHandler(Node cfgNode) {
    List<Node> nodesToCheck = new ArrayList<>();
    nodesToCheck.add(cfgNode);

    while (!nodesToCheck.isEmpty()) {
        Node currentNode = nodesToCheck.remove(0);

        if (currentNode instanceof TryCatchNode) {
            return true;
        }

        nodesToCheck.addAll(currentNode.getChildren());
    }

    return false;
}