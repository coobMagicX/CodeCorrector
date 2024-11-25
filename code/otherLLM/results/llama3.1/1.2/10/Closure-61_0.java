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
      return false; // No need to check for method calls here
    } else { 
      int refCount = getNameReferenceCount(callNode, name);
      return refCount > 0;
    }
  } else if (nameNode.getType() == Token.GETPROP) {
    // ... rest of the function remains the same ...
}