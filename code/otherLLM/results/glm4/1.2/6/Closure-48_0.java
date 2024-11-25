void maybeDeclareQualifiedName(NodeTraversal t, JSDocInfo info,
    Node n, Node parent, Node rhsValue) {
  Node ownerNode = n.getFirstChild();
  String ownerName = ownerNode.getQualifiedName();
  String qName = n.getQualifiedName();
  String propName = n.getLastChild().getString();
  Preconditions.checkArgument(qName != null && ownerName != null);

  // Precedence of type information on GETPROPs
  // ...

  JSType valueType = getDeclaredType(t.getSourceName(), info, n, rhsValue);
  if (valueType == null && rhsValue != null) {
    valueType = rhsValue.getJSType();
  }

  if ("prototype".equals(propName)) {
    Var qVar = scope.getVar(qName);
    if (qVar != null) {
      ObjectType qVarType = ObjectType.cast(qVar.getType());
      if (qVarType != null &&
          rhsValue != null &&
          rhsValue.isObjectLit()) {
        typeRegistry.resetImplicitPrototype(
            rhsValue.getJSType(), qVarType.getImplicitPrototype());
      } else if (!qVar.isTypeInferred()) {
        return;
      }
      if (qVar.getScope() == scope) {
        scope.undeclare(qVar);
      }
    }
  }

  if (valueType == null) {
    if (parent.isExprResult()) {
      stubDeclarations.add(new StubDeclaration(
          n,
          t.getInput() != null && t.getInput().isExtern(),
          ownerName));
    }
    return;
  }

  boolean inferred = true;
  if (info != null) {
    // Determining declaration for #1 + #3 + #4
    inferred = !(info.hasType()
        || info.hasEnumParameterType()
        || (info.isConstant() && valueType != null
            && !valueType.isUnknownType())
        || FunctionTypeBuilder.isFunctionTypeDeclaration(info));
  }

  if (inferred) {
    // Determining declaration for #2
    inferred = !(rhsValue != null &&
        rhsValue.isFunction() &&
        (info != null || !scope.isDeclared(qName, false)));
  }

  if (!inferred) {
    ObjectType ownerType = getObjectSlot(ownerName);
    if (ownerType != null && (!ownerType.hasOwnProperty(propName) ||
          ownerType.isPropertyTypeInferred(propName))) {
      FunctionType functionType = typeRegistry.getNativeFunctionType(valueType);
      declareNativeFunctionType(scope, functionType.getTypeId());
      ownerType.defineDeclaredProperty(propName, valueType, n);
    }
  } else if (rhsValue != null && rhsValue.isTrue()) {
    FunctionType ownerType =
        JSType.toMaybeFunctionType(getObjectSlot(ownerName));
    if (ownerType != null) {
      JSType ownerTypeOfThis = ownerType.getTypeOfThis();
      String delegateName = codingConvention.getDelegateSuperclassName();
      JSType delegateType = delegateName == null ?
          null : typeRegistry.getType(delegateName);
      if (delegateType != null &&
          ownerTypeOfThis.isSubtype(delegateType)) {
        defineSlot(n, parent, getNativeType(BOOLEAN_TYPE), true);
      }
    }
  }
}