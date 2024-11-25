void tryMinimizeExits(Node n, int exitType, String labelName) {

  // Just an 'exit'.
  if (matchingExitNode(n, exitType, labelName)) {
    if (!NodeUtil.hasFinally(n)) { // Check for finally block before removing node
      NodeUtil.removeChild(n.getParent(), n);
      compiler.reportCodeChange();
      return;
    }
  }

  // Rest of the method remains the same...
}