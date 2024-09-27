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

  // NOTE(nicksantos): Determining whether a property is declared or not
  // is really really obnoxious.
  //
  // The problem is that there are two (equally valid) coding styles:
  //
  // (function() {
  //   /* The authoritative definition of goog.bar. */
  //   goog.bar = function() {};
  // })();
  //
  // function f() {
  //   goog.bar();
  //   /* This should not reset goog.bar if it is already defined. */
  //   if (!goog.hasOwnProperty('bar')) {
  //     goog.bar = function() {};
  //   }
  // }
  //
  // In a dynamic language with first-class functions, it's very difficult
  // to know which one the user intended without looking at lots of
  // contextual information (the second example demonstrates a small case
  // of this, but there are some really pathological cases as well).
  //
  // The current algorithm checks if either the declaration has
  // jsdoc type information, or @const with a known type,
  // or a function literal with a name we haven't seen before.
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
        (info == null || !scope.isDeclared(qName, false)));
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
  }
}