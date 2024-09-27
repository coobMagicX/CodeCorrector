void maybeDeclareQualifiedName(NodeTraversal t, JSDocInfo info,
    Node n, Node parent, Node rhsValue) {
  Node ownerNode = n.getFirstChild();
  String ownerName = ownerNode.getQualifiedName();
  String qName = n.getQualifiedName();
  String propName = n.getLastChild().getString();
  Preconditions.checkArgument(qName != null && ownerName != null);

  // Precedence of type information on GETPROPs:
  // 1) @type annotation / @enum annotation
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
    inferred = !(info.hasType()
        || info.hasEnumParameterType()
        || (info.isConstant() && valueType != null
            && !valueType.isUnknownType())
        || FunctionTypeBuilder.isFunctionTypeDeclaration(info));
  }

  // Handling redefinition of function with fewer arguments than expected.
  if (rhsValue != null && rhsValue.isFunction() && !scope.isDeclared(qName, true)) {
    throw new IllegalStateException("Function " + qName + " is called with fewer arguments than expected and is being redefined.");
  }

  if (!inferred) {
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

    defineSlot(n, parent, valueType, inferred);
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