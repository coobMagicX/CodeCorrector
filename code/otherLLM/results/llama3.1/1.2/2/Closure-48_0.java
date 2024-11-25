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
  // Function prototypes are special.
  // It's a common JS idiom to do:
  // F.prototype = { ... };
  // So if F does not have an explicitly declared super type,
  // allow F.prototype to be redefined arbitrarily.
  if ("prototype".equals(propName)) {
    Var qVar = scope.getVar(qName);
    if (qVar != null) {
      // If the programmer has declared that F inherits from Super,
      // and they assign F.prototype to an object literal,
      // then they are responsible for making sure that the object literal's
      // implicit prototype is set up appropriately. We just obey
      // the @extends tag.
      ObjectType qVarType = getObjectSlot(qVar.getName());
      if (qVarType != null) {
        JSType ownerTypeOfThis = qVarType.getTypeOfThis();
        String delegateName = codingConvention.getDelegateSuperclassName();
        JSType delegateType = delegateName == null ? null : typeRegistry.getType(delegateName);
        if (delegateType != null && ownerTypeOfThis.isSubtype(delegateType)) {
          defineSlot(n, parent, getNativeType(BOOLEAN_TYPE), true);
        }
      }
    }
  }

  // Determine declaration for #1 + #3 + #4
  boolean inferred = !(info.hasType()
      || info.hasEnumParameterType()
      || (info.isConstant() && valueType != null
          && !valueType.isUnknownType())
      || FunctionTypeBuilder.isFunctionTypeDeclaration(info));

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
  } else {
    // Determine declaration for #2
    boolean hasJsDoc = info != null && (info.hasType() || FunctionTypeBuilder.isFunctionTypeDeclaration(info));
    boolean isFunctionLiteral = rhsValue != null && rhsValue.isFunction();
    inferred = !hasJsDoc || isFunctionLiteral || !scope.isDeclared(qName, false);

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
    }
  }
}