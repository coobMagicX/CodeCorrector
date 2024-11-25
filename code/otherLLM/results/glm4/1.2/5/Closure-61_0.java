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
    // Handling of "Math" namespace functions
    Node functionNameNode = nameNode.getLastChild();
    String functionName = functionNameNode.getString();

    // Check for Math function with no side effects.
    if ("sin".equals(functionName)) {
      return false; // Assuming that Math.sin should not have side effects.
    }

    // Rest of the code remains unchanged...
    
    if (callNode.hasOneChild()
        && OBJECT_METHODS_WITHOUT_SIDEEFFECTS.contains(
              functionNameNode.getString())) {
      return false;
    }

    if (callNode.isOnlyModifiesThisCall()
        && evaluatesToLocalValue(nameNode.getFirstChild())) {
      return false;
    }
  }

  // Rest of the code remains unchanged...
  
  return true;
}