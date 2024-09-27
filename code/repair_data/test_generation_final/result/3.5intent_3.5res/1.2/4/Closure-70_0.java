private void declareArguments(Node functionNode) {
  Node astParameters = functionNode.getFirstChild().getNext();
  Node body = astParameters.getNext();
  FunctionType functionType = (FunctionType) functionNode.getJSType();
  if (functionType != null) {
    Node jsDocParameters = functionType.getParametersNode();
    if (jsDocParameters != null) {
      Node jsDocParameter = jsDocParameters.getFirstChild();
      for (Node astParameter : astParameters.children()) {
        if (jsDocParameter != null) {
          defineSlot(astParameter, functionNode,
              jsDocParameter.getJSType(), true);
          jsDocParameter = jsDocParameter.getNext();
        } else {
          defineSlot(astParameter, functionNode, null, true);
        }
      }
    }
  }
} // end declareArguments

void defineSlot(Node n, Node parent, JSType type, boolean inferred) {
  Preconditions.checkArgument(n.isName());
  String variableName = n.getString();
  
  boolean isGlobalVar = n.getType() == Token.NAME && scope.isGlobal();
  boolean shouldDeclareOnGlobalThis =
      isGlobalVar &&
      (parent.getType() == Token.VAR ||
       parent.getType() == Token.FUNCTION);

  // If n is a property, then we should really declare it in the
  // scope where the root object appears. This helps out people
  // who declare "global" names in an anonymous namespace.
  Scope scopeToDeclareIn = scope;
  if (n.getType() == Token.GETPROP && !scope.isGlobal() &&
      isQnameRootedInGlobalScope(n)) {
    Scope globalScope = scope.getGlobalScope();

    // don't try to declare in the global scope if there's
    // already a symbol there with this name.
    if (!globalScope.isDeclared(variableName, false)) {
      scopeToDeclareIn = scope.getGlobalScope();
    }
  }

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
    boolean isExtern = input.isExtern();
    Var newVar =
        scopeToDeclareIn.declare(variableName, n, type, input, inferred);

    if (shouldDeclareOnGlobalThis) {
      ObjectType globalThis =
          typeRegistry.getNativeObjectType(GLOBAL_THIS);
      if (inferred) {
        globalThis.defineInferredProperty(variableName,
            type == null ?
                getNativeType(JSTypeNative.NO_TYPE) :
                type,
            isExtern, n);
      } else {
        globalThis.defineDeclaredProperty(variableName, type, isExtern, n);
      }
    }

    if (type instanceof EnumType) {
      Node initialValue = newVar.getInitialValue();
      boolean isValidValue = initialValue != null &&
          (initialValue.getType() == Token.OBJECTLIT ||
           initialValue.isQualifiedName());
      if (!isValidValue) {
        compiler.report(JSError.make(sourceName, n, ENUM_INITIALIZER));
      }
    }

    // We need to do some additional work for constructors and interfaces.
    if (type instanceof FunctionType &&
        // We don't want to look at empty function types.
        !type.isEmptyType()) {
      FunctionType fnType = (FunctionType) type;
      if ((fnType.isConstructor() || fnType.isInterface()) &&
          !fnType.equals(getNativeType(U2U_CONSTRUCTOR_TYPE))) {
        // Declare var.prototype in the scope chain.
        FunctionType superClassCtor = fnType.getSuperClassConstructor();
        scopeToDeclareIn.declare(variableName + ".prototype", n,
            fnType.getPrototype(), input,
            /* declared iff there's an explicit supertype */
            superClassCtor == null ||
            superClassCtor.getInstanceType().equals(
                getNativeType(OBJECT_TYPE)));

        // Make sure the variable is initialized to something if
        // it constructs itself.
        if (newVar.getInitialValue() == null &&
            !isExtern &&
            // We want to make sure that when we declare a new instance
            // type (with @constructor) that there's actually a ctor for it.
            // This doesn't apply to structural constructors
            // (like function(new:Array). Checking the constructed
            // type against the variable name is a sufficient check for
            // this.
            variableName.equals(
                fnType.getInstanceType().getReferenceName())) {
          compiler.report(
              JSError.make(sourceName, n,
                  fnType.isConstructor() ?
                      CTOR_INITIALIZER : IFACE_INITIALIZER,
                  variableName));
        }
      }
    }
  }

  if (isGlobalVar && "Window".equals(variableName)
      && type instanceof FunctionType
      && type.isConstructor()) {
    FunctionType globalThisCtor =
        typeRegistry.getNativeObjectType(GLOBAL_THIS).getConstructor();
    globalThisCtor.getInstanceType().clearCachedValues();
    globalThisCtor.getPrototype().clearCachedValues();
    globalThisCtor
        .setPrototypeBasedOn(((FunctionType) type).getInstanceType());
  }
}