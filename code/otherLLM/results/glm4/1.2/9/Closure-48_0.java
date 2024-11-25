void maybeDeclareQualifiedName(NodeTraversal t, JSDocInfo info,
    Node n, Node parent, Node rhsValue) {
  Node ownerNode = n.getFirstChild();
  String ownerName = ownerNode != null ? ownerNode.getQualifiedName() : null;
  String qName = n.getQualifiedName();
  String propName = n.getLastChild().getString();

  Preconditions.checkArgument(qName != null && ownerName != null);

  // Existing code for determining type for properties

  if ("prototype".equals(propName)) {
    Var qVar = scope.getVar(qName);
    if (qVar != null) {
      ObjectType qVarType = ObjectType.cast(qVar.getType());
      if (qVarType != null && rhsValue != null && rhsValue.isObjectLit()) {
        typeRegistry.resetImplicitPrototype(
            rhsValue.getJSType(), qVarType.getImplicitPrototype());
      } else if (!qVar.isTypeInferred()) {
        return; // Previous version had a return statement here
      }
      if (qVar.getScope() == scope) {
        scope.undeclare(qVar);
      }
    }
  }

  JSType valueType = rhsValue != null ? rhsValue.getJSType() : null;
  if (valueType == null) {
    if (parent.isExprResult()) {
      stubDeclarations.add(new StubDeclaration(
          n,
          t.getInput() != null && t.getInput().isExtern(),
          ownerName));
    }
    return; // Ensure we exit the method after adding a stub declaration
  }

  boolean inferred = true;
  if (info != null) {
    inferred = !(info.hasType()
        || info.hasEnumParameterType()
        || (info.isConstant() && valueType != null
            && !valueType.isUnknownType())
        || FunctionTypeBuilder.isFunctionTypeDeclaration(info));
  }

  if (!inferred && rhsValue != null && rhsValue.isFunction()) {
    inferred = !(info != null || !scope.isDeclared(qName, false));
  }

  if (inferred) {
    ObjectType ownerType = getObjectSlot(ownerName);
    if (ownerType != null) {
      boolean isExtern = t.getInput() != null && t.getInput().isExtern();
      if ((!ownerType.hasOwnProperty(propName) ||
           ownerType.isPropertyTypeInferred(propName)) &&
          ((isExtern && !ownerType.isNativeObjectType()) ||
           !ownerType.isInstanceType())) {
        ownerType.defineDeclaredProperty(propName, valueType, n);
      }
    }
  } else {
    defineSlot(n, parent, valueType, inferred); // Existing code for declaration
  }

  if (rhsValue != null && rhsValue.isTrue()) { // Existing code for delegate proxy method properties
    FunctionType ownerType = JSType.toMaybeFunctionType(getObjectSlot(ownerName));
    if (ownerType != null) {
      JSType ownerTypeOfThis = ownerType.getTypeOfThis();
      String delegateName = codingConvention.getDelegateSuperclassName();
      JSType delegateType = delegateName == null ? null : typeRegistry.getType(delegateName);
      if (delegateType != null && ownerTypeOfThis.isSubtype(delegateType)) {
        defineSlot(n, parent, getNativeType(BOOLEAN_TYPE), true);
      }
    }
  }
}