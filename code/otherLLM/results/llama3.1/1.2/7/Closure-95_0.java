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

  // If n is a property, then we should really declare it in the
  // scope where the root object appears. This helps out people
  // who declare "global" names in an anonymous namespace.
  Scope scopeToDeclareIn = getScopeForProperty(n);

  // don't try to declare in the global scope if there's
  // already a symbol there with this name.

  // declared in closest scope?
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
            type == null ?
                getNativeType(JSTypeNative.NO_TYPE) :
                type,
            isExtern);
      } else {
        globalThis.defineDeclaredProperty(variableName, type, isExtern);
      }
    }

    // If we're in the global scope, also declare var.prototype
    // in the scope chain.
    if (scopeToDeclareIn.isGlobal() && type instanceof FunctionType) {
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

// Helper function to get the scope where a property should be declared
Scope getScopeForProperty(Node n) {
  // If n is a property, return the scope of its root object
  if (n.getType() == Token.GETPROP) {
    Node rootObject = ...; // Get the root object of n
    Scope scope = rootObject.getScope();
    return scope;
  } else {
    // For other types, just use the current scope
    return scope;
  }
}