static boolean functionCallHasSideEffects(Node callNode, @Nullable AbstractCompiler compiler) {
  if (callNode.getType() != Token.CALL) {
    throw new IllegalStateException("Expected CALL node, got " + Token.name(callNode.getType()));
  }

  if (callNode.isNoSideEffectsCall()) {
    return false;
  }

  Node nameNode = callNode.getFirstChild();

  // Built-in functions with no side effects
  if (nameNode.getType() == Token.NAME) {
    String name = nameNode.getString();
    if (BUILTIN_FUNCTIONS_WITHOUT_SIDEEFFECTS.contains(name)) {
      return false;
    }
  } else if (nameNode.getType() == Token.GETPROP) {
    if (callNode.hasOneChild() && OBJECT_METHODS_WITHOUT_SIDEEFFECTS.contains(nameNode.getLastChild().getString())) {
      return false;
    }

    if (callNode.isOnlyModifiesThisCall() && evaluatesToLocalValue(nameNode.getFirstChild())) {
      return false;
    }

    Node objectNode = nameNode.getFirstChild();
    String propertyName = nameNode.getLastChild().getString();
    if (objectNode.getType() == Token.NAME && "Math".equals(objectNode.getString())) {
      // Here list all known Math methods with no side effects
      if (Arrays.asList("abs", "sin", "cos", "tan", "exp", "log", "sqrt").contains(propertyName)) {
        return false;
      }
    }
  }

  return true;
}
