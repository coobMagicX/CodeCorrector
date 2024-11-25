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
          // Correctly handle the case where there are more AST parameters than JSDoc parameters
          defineSlot(astParameter, functionNode, null, true);
        }
      }
    }
  }
} // end declareArguments

void defineSlot(Node n, Node parent, String variableName,
       JSType type, boolean inferred) {
  Preconditions.checkArgument(!variableName.isEmpty());

  boolean isGlobalVar = n.getType() == Token.NAME && scope.isGlobal();
  boolean shouldDeclareOnGlobalThis =
      isGlobalVar &&
      (parent.getType() == Token.VAR ||
       parent.getType() == Token.FUNCTION);

  Scope scopeToDeclareIn = scope;
  if (n.getType() == Token.GETPROP && !scope.isGlobal() &&
      isQnameRootedInGlobalScope(n)) {
    Scope globalScope = scope.getGlobalScope();

    if (!globalScope.isDeclared(variableName, false)) {
      scopeToDeclareIn = scope.getGlobalScope();
    }
  }

  Var oldVar = null;
  if (scopeToDeclareIn.isDeclared(variableName, false)) {
    oldVar = scopeToDeclareIn.getVar(variableName);
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

    if (type instanceof FunctionType && !type.isEmptyType()) {
      FunctionType fnType = (FunctionType) type;
      if ((fnType.isConstructor() || fnType.isInterface()) &&
          !fnType.equals(getNativeType(U2U_CONSTRUCTOR_TYPE))) {
        scopeToDeclareIn.declare(variableName + ".prototype", n,
            fnType.getPrototype(), input,
            superClassCtor == null ||
            superClassCtor.getInstanceType().equals(
                getNativeType(OBJECT_TYPE)));

        if (newVar.getInitialValue() == null &&
            !isExtern &&
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

  if (oldVar != null && oldVar.getType() == VarType.FIELD) {
    // Handle the case where the variable was already declared as a field
    // This is an example of handling the situation; actual implementation may vary.
    handleFieldDeclarationConflict(n, oldVar);
  }
}