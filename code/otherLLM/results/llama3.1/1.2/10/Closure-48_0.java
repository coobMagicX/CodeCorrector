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

  // Function prototypes are special.
  // It's a common JS idiom to do:
  // F.prototype = { ... };
  // So if F does not have an explicitly declared super type,
  // allow F.prototype to be redefined arbitrarily.
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
        // If the programmer has declared that F inherits from Super,
        // and they assign F.prototype to some arbitrary expression,
        // there's not much we can do. We just ignore the expression,
        // and hope they've annotated their code in a way to tell us
        // what props are going to be on that prototype.
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
  }
}