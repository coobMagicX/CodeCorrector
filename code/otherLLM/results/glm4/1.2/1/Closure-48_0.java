void maybeDeclareQualifiedName(NodeTraversal t, JSDocInfo info,
    Node n, Node parent, Node rhsValue) {
  Node ownerNode = n.getFirstChild();
  String ownerName = ownerNode.getQualifiedName();
  String qName = n.getQualifiedName();
  String propName = n.getLastChild().getString();
  Preconditions.checkArgument(qName != null && ownerName != null);

  JSType valueType = getDeclaredType(t.getSourceName(), info, n, rhsValue);
  if (valueType == null && rhsValue != null) {
    valueType = rhsValue.getJSType();
  }
  if ("prototype".equals(propName)) {
    Var qVar = scope.getVar(qName);
    if (qVar != null) {
      ObjectType qVarType = ObjectType.cast(qVar.getType());
      if (qVarType != null && qVarType.hasProperty(propName)) {
        // Avoid redefining the prototype property
        return;
      }
      defineSlot(n, parent, valueType, false);
    }
  }

  boolean inferred = true;
  if (info != null) {
    inferred = !(info.hasType()
        || info.hasEnumParameterType()
        || (info.isConstant() && valueType != null
            && !valueType.isUnknownType())
        || FunctionTypeBuilder.isFunctionTypeDeclaration(info));
  }
  
  if (inferred) {
    inferred = !(rhsValue != null &&
        rhsValue.isFunction() &&
        (info != null || !scope.isDeclared(qName, false)));
  }

  if (!inferred) {
    ObjectType ownerType = getObjectSlot(ownerName);
    if (ownerType != null && ownerType.hasProperty(propName)) {
      // Property is already declared, skip declaration
      return;
    }
    boolean isExtern = t.getInput() != null && t.getInput().isExtern();
    if ((isExtern && !ownerType.isNativeObjectType()) ||
        !ownerType.isInstanceType()) {
      ownerType.defineDeclaredProperty(propName, valueType, n);
    }
    defineSlot(n, parent, valueType, inferred);
  } else if (rhsValue != null && rhsValue.isTrue()) {
    FunctionType ownerType = JSType.toMaybeFunctionType(getObjectSlot(ownerName));
    if (ownerType != null) {
      JSType ownerTypeOfThis = ownerType.getTypeOfThis();
      String delegateName = codingConvention.getDelegateSuperclassName();
      JSType delegateType = delegateName == null ? null : typeRegistry.getType(delegateName);
      if (delegateType != null && ownerTypeOfThis.isSubtype(delegateType)) {
        defineSlot(n, parent, getNativeType(BOOLEAN_TYPE), true);
      }
    }
  } else {
    // Handle the case where rhsValue is not true
    FunctionType functionNodeType = JSType.toMaybeFunctionType(n.getJSType());
    if (functionNodeType != null) {
      declareArguments(n); // Declare arguments of the function
    }
  }

  resolveTypes(); // Ensure types are resolved and attached to nodes
}