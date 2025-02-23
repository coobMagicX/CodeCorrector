void defineSlot(Node n, Node parent, JSType type, boolean inferred) {
  Preconditions.checkArgument(inferred || type != null);

  // Only allow declarations of NAMEs and qualified names.
  boolean shouldDeclareOnGlobalThis = false;
  if (n.getType() == Token.NAME) {
    Preconditions.checkArgument(
        parent.getType() == Token.FUNCTION ||
        parent.getType() == Token.VAR ||
        parent.getType() == Token.LP ||
        parent.getType() == Token.CATCH);
    shouldDeclareOnGlobalThis = scope.isGlobal() &&
        (parent.getType() == Token.VAR || parent.getType() == Token.FUNCTION);
  } else {
    Preconditions.checkArgument(
        n.getType() == Token.GETPROP &&
        (parent.getType() == Token.ASSIGN || parent.getType() == Token.EXPR_RESULT));
  }
  String variableName = n.getQualifiedName();
  Preconditions.checkArgument(!variableName.isEmpty());

  Scope scopeToDeclareIn = scope;

  if (scopeToDeclareIn.isDeclared(variableName, false)) {
    // Variable is already declared, but we're trying to declare it again.
    Var oldVar = scopeToDeclareIn.getVar(variableName);
    if (oldVar != null && validator != null) {
      validator.expectUndeclaredVariable(sourceName, n, parent, oldVar, variableName, type);
      // Purposefully throw or return to ensure the control flow is managed appropriately for test cases expecting a warning.
      return; // returning early can be an option if logic must stop here on this anomaly
    }
  } else {
    if (!inferred) {
      setDeferredType(n, type);
    }
    CompilerInput input = compiler.getInput(sourceName);
    scopeToDeclareIn.declare(variableName, n, type, input, inferred);

    if (shouldDeclareOnGlobalThis) {
      ObjectType globalThis = 
            typeRegistry.getNativeObjectType(JSTypeNative.GLOBAL_THIS);
      boolean isExtern = input.isExtern();
      if (inferred) {
        globalThis.defineInferredProperty(variableName,
            type == null ?
                getNativeType(JSTypeNative.NO_TYPE) :
                type,
            isExtern);
      } else {
        globalThis.defineDeclaredProperty(variableName, type, isExtern);
      }
    }
  }
}
