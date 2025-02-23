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
  
  // Built-in functions with no side effects
  if (nameNode.getType() == Token.NAME) {
    String name = nameNode.getString();
    if (BUILTIN_FUNCTIONS_WITHOUT_SIDEEFFECTS.contains(name)) {
      return false;
    }
  } else if (nameNode.getType() == Token.GETPROP) {
    String propertyName = nameNode.getLastChild().getString();
    Node objectNode = nameNode.getFirstChild();

    if (callNode.hasOneChild()
        && OBJECT_METHODS_WITHOUT_SIDEEFFECTS.contains(propertyName)) {
      return false;
    }

    if (callNode.isOnlyModifiesThisCall()
        && evaluatesToLocalValue(objectNode)) {
      return false;
    }
    
    if (objectNode.getType() == Token.NAME && objectNode.getString().equals("Math")) {
      // Explicitly list safe Math functions
      if (propertyName.equals("abs") || propertyName.equals("sin") || propertyName.equals("cos") ||
          propertyName.equals("tan") || propertyName.equals("exp") || propertyName.equals("log") ||
          propertyName.equals("sqrt") || propertyName.equals("ceil") || propertyName.equals("floor") ||
          propertyName.equals("round") || propertyName.equals("max") || propertyName.equals("min")) {
        return false;
      }
    }

    // Handle RegExp and String methods safely assuming no side effects
    if (compiler != null && !compiler.hasRegExpGlobalReferences()) {
      if (objectNode.getType() == Token.REGEXP
          && REGEXP_METHODS.contains(propertyName)) {
        return false;
      } else if (objectNode.getType() == Token.STRING
          && STRING_REGEXP_METHODS.contains(propertyName)) {
        Node param = nameNode.getNext();
        if (param != null && (param.getType() == Token.STRING || param.getType() == Token.REGEXP)) {
          return false;
        }
      }
    }
  }

  return true;
}
