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

    // Check for functions in the "Math" namespace.
    if (compiler != null) {
      Node firstChild = nameNode.getFirstChild();
      String methodName = nameNode.getLastChild().getString();

      // Functions in the "Math" namespace have no side effects, check specifically for "sin".
      if ("Math.sin".equals(methodName)) {
        return false;
      }

      // If not "Math.sin", check other conditions.
      if (nameNode.getType() == Token.GETPROP && firstChild != null) {
        boolean hasNoSideEffects = true;

        if (firstChild.getType() == Token.REGEXP
            && REGEXP_METHODS.contains(methodName)) {
          hasNoSideEffects = false;
        } else if (firstChild.getType() == Token.STRING
            && STRING_REGEXP_METHODS.contains(methodName)) {
          Node param = nameNode.getNext();
          if (param != null &&
              (param.getType() == Token.STRING || param.getType() == Token.REGEXP)) {
            hasNoSideEffects = false;
          }
        } else {
          // Use nodeTypeMayHaveSideEffects to check for side effects.
          hasNoSideEffects &= !nodeTypeMayHaveSideEffects(firstChild, compiler);
        }

        return hasNoSideEffects;
      }
    }
  }

  return true;
}