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
    if (ownerType != null) {
      // Only declare this as an official property if it has not been
      // declared yet.
      boolean isExtern = t.getInput() != null && t.getInput().isExtern();
      if ((!ownerType.hasOwnProperty(propName) ||
           ownerType.isPropertyTypeInferred(propName)) &&
          ((isExtern && !ownerType.isNativeObjectType()) ||
           !ownerType.isInstanceType())) {
        // If the property is undeclared or inferred, declare it now.
        ownerType.defineDeclaredProperty(propName, valueType, n);
      }
    }

    // If the property is already declared, the error will be
    // caught when we try to declare it in the current scope.
    defineSlot(n, parent, valueType, inferred);
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

    // Add the following lines to check for function literal with a name we haven't seen before
    FirstOrderFunctionAnalyzer analyzer = new FirstOrderFunctionAnalyzer(compiler, data);
    if (analyzer.isFunctionLiteral(rhsValue)) {
        inferred = false;
    }
  }
}