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
      (parent.getType() == Token.VAR ||
       parent.getType() == Token.FUNCTION);
  } else {
    Preconditions.checkArgument(
      n.getType() == Token.GETPROP &&
      (parent.getType() == Token.ASSIGN ||
       parent.getType() == Token.EXPR_RESULT));
  }
  String variableName = n.getQualifiedName();
  Preconditions.checkArgument(!variableName.isEmpty());

  Scope scopeToDeclareIn = scope;

  if (scopeToDeclareIn.isDeclared(variableName, false)) {
    Var oldVar = scopeToDeclareIn.getVar(variableName);
    validator.expectUndeclaredVariable(
        sourceName, n, parent, oldVar, variableName, type);
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
            type == null ? getNativeType(JSTypeNative.NO_TYPE) : type,
            isExtern);
      } else {
        globalThis.defineDeclaredProperty(variableName, type, isExtern);
      }
    }

    if (scope.isGlobal() && type instanceof FunctionType) { // This handles global scope and FunctionType
      FunctionType fnType = (FunctionType) type;
      if (fnType.isConstructor() || fnType.isInterface()) {
        FunctionType superClassCtor = fnType.getSuperClassConstructor();
        scopeToDeclareIn.declare(variableName + ".prototype", n,
            fnType.getPrototype(), compiler.getInput(sourceName),
            /* declared iff there's an explicit supertype */
            superClassCtor == null ||
            superClassCtor.getInstanceType().equals(
                getNativeType(OBJECT_TYPE)));
      }
    }
  }
}
