void maybeDeclareQualifiedName(NodeTraversal t, JSDocInfo info,
    Node n, Node parent, Node rhsValue) {
  Node ownerNode = n.getFirstChild();
  String ownerName = ownerNode.getQualifiedName();
  String qName = n.getQualifiedName();
  String propName = n.getLastChild().getString();
  Preconditions.checkArgument(qName != null && ownerName != null);

  // Precedence of type information on GETPROPs:
  // 1) @type annnotation / @enum annotation
  // 2) ASSIGN to FUNCTION literal
  // 3) @param/@return annotation (with no function literal)
  // 4) ASSIGN to something marked @const
  // 5) ASSIGN to anything else
  //
  // 1, 3, and 4 are declarations, 5 is inferred, and 2 is a declaration iff
  // the function has jsdoc or has not been declared before.
  //
  // FUNCTION literals are special because TypedScopeCreator is very smart
  // about getting as much type information as possible for them.

  // Determining type for #1 + #2 + #3 + #4
  JSType valueType = getDeclaredType(t.getSourceName(), info, n, rhsValue);
  if (valueType == null && rhsValue != null) {
    // Determining type for #5
    valueType = rhsValue.getJSType();
  }

  if (!isDeclaration(valueType)) {
    if (rhsValue != null && rhsValue.isFunction()) {
      FunctionType functionType = JSType.toMaybeFunctionType(rhsValue.getJSType());
      if (functionType != null) {
        valueType = functionType;
      }
    }
  }

  if (!isPropertyDeclared(ownerName, propName)) {
    defineSlot(n, parent, valueType, false);
  } else if (rhsValue != null && rhsValue.isTrue()) {
    // We declare these for delegate proxy method properties.
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
  } else {
    // The property is already declared, no need to declare it again.
  }

  // Check for function arguments declaration
  if (n.isFunction()) {
    declareArguments(n);
  }
}

private boolean isDeclaration(JSType type) {
  return type != null && (type.hasType() || type.hasEnumParameterType() ||
      (type.isConstant() && !type.isUnknownType()) || FunctionTypeBuilder.isFunctionTypeDeclaration(type));
}

private boolean isPropertyDeclared(String ownerName, String propName) {
  ObjectType ownerType = getObjectSlot(ownerName);
  return ownerType != null && ownerType.hasOwnProperty(propName);
}