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
  String functionName = getFunctionName(nameNode);
  if (functionName != null && BUILTIN_FUNCTIONS_WITHOUT_SIDEEFFECTS.contains(functionName)) {
    return false;
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
    Node parent = nameNode.getParent();
    if (parent != null && parent.getType() == Token.CALL) {
      String parentFunctionName = getFunctionName(parent);
      if ("Math".equals(parentFunctionName)) {
        return false;
      }
    }

    if (compiler != null && !compiler.hasRegExpGlobalReferences()) {
      Node param = nameNode.getNext();
      if (param != null &&
          ((param.getType() == Token.REGEXP
            && REGEXP_METHODS.contains(nameNode.getLastChild().getString()))
           || (param.getType() == Token.STRING
               && STRING_REGEXP_METHODS.contains(
                   nameNode.getLastChild().getString())))) {
        return false;
      }
    }
  }

  return true;
}