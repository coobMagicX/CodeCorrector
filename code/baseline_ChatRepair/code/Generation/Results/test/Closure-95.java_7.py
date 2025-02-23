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
  
  // Verify if variable is already declared in scope
  if (scopeToDeclareIn.isDeclared(variableName, false)) {
    Var oldVar = scopeToDeclareIn.getVar(variableName);
    // Check if the types don't match and warn the user
    if (oldVar.getType() != null && !oldVar.getType().isEquivalentTo(type)) {
      String warningMessage = "Type mismatch on redeclaration of: " + variableName;
      compiler.report(JSError.make(n, CheckTypes.TYPE_MISMATCH_WARNING, warningMessage));
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
      // Define the property inside the global object
      if (inferred) {
        globalThis.defineInferredProperty(variableName, type == null ? getNativeType(JSTypeNative.NO_TYPE) : type, isExtern);
      } else {
        globalThis.defineDeclaredProperty(variableName, type, isExtern);
      }
    }

    // If we're in the global scope, also declare var.prototype in the scope chain.
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
