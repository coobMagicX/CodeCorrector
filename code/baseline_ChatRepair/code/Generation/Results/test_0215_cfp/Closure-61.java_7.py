import java.util.Set;

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
    Node objectNode = nameNode.getFirstChild();
    Node lastChild = nameNode.getLastChild();
    if (callNode.hasOneChild() && OBJECT_METHODS_WITHOUT_SIDEEFFECTS.contains(lastChild.getString())) {
      return false;
    }

    if (callNode.isOnlyModifiesThisCall() && evaluatesToLocalValue(objectNode)) {
      return false;
    }

    // Assuming a set of no side-effect methods for Math calls
    if ("Math".equals(objectNode.getString()) && MATH_METHODS_WITHOUT_SIDEEFFECTS.contains(lastChild.getString())) {
      return false;
    }

    if (compiler != null && !compiler.hasRegExpGlobalReferences()) {
      if (objectNode.getType() == Token.REGEXP && REGEXP_METHODS.contains(lastChild.getString())) {
        return false;
      } else if (objectNode.getType() == Token.STRING &&
                 STRING_REGEXP_METHODS.contains(lastChild.getString())) {
        Node param = nameNode.getNext();
        if (param != null && (param.getType() == Token.STRING || param.getType() == Token.REGEXP))
          return false;
      }
    }
  }

  return true;
}
