static boolean functionCallHasSideEffects(
    Node callNode, @Nullable AbstractCompiler compiler) {
  if (callNode.getType() != Token.CALL) {
    throw new IllegalStateException(
        "Expected CALL node, got " + Token.name(callNode.getType()));
  }

  if (callNode.isNoSideEffectsCall()) {
    return false;
  }

  Node nameNode = callNode.getFirstChild();

  // Built-in functions with no side effects.
  if (nameNode.getType() == Token.NAME) {
    String name = nameNode.getString();
    if (BUILTIN_FUNCTIONS_WITHOUT_SIDEEFFECTS.contains(name)) {
      return false;
    }
  } else if (nameNode.getType() == Token.GETPROP) {
    if (callNode.hasOneChild()
        && OBJECT_METHODS_WITHOUT_SIDEEFFECTS.contains(
              nameNode.getLastChild().getString())) {
      return false;
    }

    if (callNode.isOnlyModifiesThisCall()
        && evaluatesToLocalValue(nameNode.getFirstChild())) {
      return false;
    }

    // Functions in the "Math" namespace have no side effects.
    if (nameNode.getType() == Token.NAME) {
      String functionName = nameNode.getString();
      if (functionName.startsWith("Math.")) {
        switch (functionName) {
          case "Math.sin":
          case "Math.cos":
          case "Math.tan":
          // ... add other Math methods with no side effects here
            return false;
          default:
            break;
        }
      }
    }
  }

  return true;
}