void defineSlot(Node n, Node parent, JSType type, boolean inferred) {
  Preconditions.checkArgument(inferred || type != null);

  // Only allow declarations of NAMEs and qualfied names.
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

  // Check if already declared, emit warning if redeclaration with a different type
  if (scopeToDeclareIn.isDeclared(variableName, false)) {
    Var oldVar = scopeToDeclareIn.getVar(variableName);
    if (oldVar != null && (type != null && !type.equals(oldVar.getType()))) {
      // Since your project might have its own error handling, replace the error string below as appropriate
      JSError error = JSError.make(n, "Type mismatch for variable '" + variableName + "': expected " + oldVar.getType() + " but got " + type);
      compiler.report(error);
    }
    validator.expectUndeclaredVariable(
        sourceName, n, parent, oldVar, variableName, type);
  } else {
    if (!inferred) {
      setDeferredType(n, type);
    }
    CompilerInput input = compiler.getInput(sourceName);
    scopeToDeclareIn.declare(variableName, n, type, input, inferred);

    if (shouldDeclareOnGlobalThis) {
      ObjectType globalThis = typeRegistry.getNativeObjectType(JSTypeNative.GLOBAL_THIS);
      boolean isExtern = input.isExtern();
      if (inferred) {
        globalThis.defineInferredProperty(variableName, type == null ? getNativeType(JSTypeNative.NO_TYPE) : type, isExtern);
      } else {
        globalThis.defineDeclaredProperty(variableName, type, isExtern);
      }
    }

    if (scopeToDeclareIn.isGlobal() && type instanceof FunctionType) {
      FunctionType fnType = (FunctionType) type;
      if (fnType.isConstructor() || fnType.isInterface()) {
        FunctionType superClassCtor = fnType.getSuperClassConstructor();
        scopeToDeclareIn.declare(variableName + ".prototype", n, fnType.getPrototype(), compiler.getInput(sourceName),
            /* declared iff there's an explicit supertype */
            superClassCtor == null || superClassCtor.getInstanceType().equals(getNativeType(OBJECT_TYPE)));
      }
    }
  }
}
