void defineSlot(Node n, Node parent, JSType type, boolean inferred) {
  Preconditions.checkArgument(inferred || type != null);

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
  Preconditions.checkArgument(variableName != null && !variableName.isEmpty());

  Scope scopeToDeclareIn = scope;

  if (scopeToDeclareIn.isDeclared(variableName, false)) {
    Var oldVar = scopeToDeclareIn.getVar(variableName);
    if (oldVar != null && !oldVar.getType().isEquivalentTo(type)) {
      validator.expectUndeclaredVariable(
            sourceName, n, parent, oldVar, variableName, type);
      // Ensure warnings are generated correctly
      compiler.report(JSError.make(n, DIAGNOSTIC_GROUP, "Duplicate declaration or mismatch type"));
    }
  } else {
    if (!inferred) {
      setDeferredType(n, type);
    }
    CompilerInput input = compiler.getInput(sourceName);
    scopeToDeclareIn.declare(variableName, n, type, input, inferred);

    if (shouldDeclareOnGlobalThis) {
      ObjectType globalThis = typeRegistry.getNativeObjectType(JSTypeNative.GLOBAL_THIS);
      boolean isExtern = input.isExtern();
      if (inferred && type == null) {
        globalThis.defineInferredProperty(variableName, getNativeType(JSTypeNative.NO_TYPE), isExtern);
      } else {
        globalThis.defineDeclaredProperty(variableName, type, isExtern);
      }
    }

    if (scopeToDeclareIn.isGlobal() && type instanceof FunctionType) {
      FunctionType fnType = (FunctionType) type;
      if (fnType.isConstructor() || fnType.isInterface()) {
        FunctionType superClassCtor = fnType.getSuperClassConstructor();
        scopeToDeclareIn.declare(variableName + ".prototype", n, fnType.getPrototype(), input,
            superClassCtor == null || superClassCtor.getInstanceType().equals(getNativeType(OBJECT_TYPE)));
      }
    }
  }
}
