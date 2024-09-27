private void updateScopeForTypeChange(
    FlowScope scope, Node left, JSType leftType, JSType resultType) {
  Preconditions.checkNotNull(resultType);
  switch (left.getType()) {
    case Token.NAME:
      String varName = left.getString();
      Var var = syntacticScope.getVar(varName);
      boolean isVarDeclaration = left.hasChildren();

      // When looking at VAR initializers for declared VARs, we tend
      // to use the declared type over the type it's being
      // initialized to in the global scope.
      //
      // For example,
      // /** @param {number} */ var f = goog.abstractMethod;
      // it's obvious that the programmer wants you to use
      // the declared function signature, not the inferred signature.
      //
      // Or,
      // /** @type {Object.<string>} */ var x = {};
      // the one-time anonymous object on the right side
      // is as narrow as it can possibly be, but we need to make
      // sure we back-infer the <string> element constraint on
      // the left hand side, so we use the left hand side.

      boolean isVarTypeBetter = !isVarDeclaration || var == null || var.isTypeInferred();
          // Makes it easier to check for NPEs.

      // TODO(nicksantos): This might be a better check once we have
      // back-inference of object/array constraints.  It will probably
      // introduce more type warnings.  It uses the result type iff it's
      // strictly narrower than the declared var type.
      //
      //boolean isVarTypeBetter = isVarDeclaration &&
      //    (varType.restrictByNotNullOrUndefined().isSubtype(resultType)
      //     || !resultType.isSubtype(varType));

      if (isVarTypeBetter) {
        redeclareSimpleVar(scope, left, resultType);
      }
      left.setJSType(isVarDeclaration || leftType == null ?
          resultType : null);

      if (var != null && var.isTypeInferred()) {
        JSType oldType = var.getType();
        var.setType(oldType == null ?
            resultType : oldType.getLeastSupertype(resultType));
      }
      break;
    case Token.GETPROP:
      String qualifiedName = left.getQualifiedName();
      if (qualifiedName != null) {
        scope.inferQualifiedSlot(left, qualifiedName,
            leftType == null ? unknownType : leftType,
            resultType);
      }

      left.setJSType(resultType);
      ensurePropertyDefined(left, resultType);
      break;
    default:
      throw new RuntimeException("Invalid token type: " + left.getType());
  }
}

private void ensurePropertyDefined(Node getprop, JSType rightType) {
  String propName = getprop.getLastChild().getString();
  Node obj = getprop.getFirstChild();
  JSType nodeType = getJSType(obj);
  ObjectType objectType = ObjectType.cast(
      nodeType.restrictByNotNullOrUndefined());
  boolean propCreationInConstructor = obj.isThis() &&
      getJSType(syntacticScope.getRootNode()).isConstructor();

  if (objectType == null) {
    registry.registerPropertyOnType(propName, nodeType);
  } else {
    if (nodeType.isStruct() && !objectType.hasProperty(propName)) {
      // In general, we don't want to define a property on a struct object,
      // b/c TypeCheck will later check for improper property creation on
      // structs. There are two exceptions.
      // 1) If it's a property created inside the constructor, on the newly
      //    created instance, allow it.
      // 2) If it's a prototype property, allow it. For example:
      //    Foo.prototype.bar = baz;
      //    where Foo.prototype is a struct and the assignment happens at the
      //    top level and the constructor Foo is defined in the same file.
      boolean staticPropCreation = false;
      Node maybeAssignStm = getprop.getParent().getParent();
      if (syntacticScope.isGlobal() &&
          NodeUtil.isPrototypePropertyDeclaration(maybeAssignStm)) {
        String propCreationFilename = maybeAssignStm.getSourceFileName();
        Node ctor = objectType.getOwnerFunction().getSource();
        if (ctor != null &&
            ctor.getSourceFileName().equals(propCreationFilename)) {
          staticPropCreation = true;
        }
      }
      if (!propCreationInConstructor && !staticPropCreation) {
        return; // Early return to avoid creating the property below.
      }
    }

    if (ensurePropertyDeclaredHelper(getprop, objectType)) {
      return;
    }

    if (!objectType.isPropertyTypeDeclared(propName)) {
      // We do not want a "stray" assign to define an inferred property
      // for every object of this type in the program. So we use a heuristic
      // approach to determine whether to infer the property.
      //
      // 1) If the property is already defined, join it with the previously
      //    inferred type.
      // 2) If this isn't an instance object, define it.
      // 3) If the property of an object is being assigned in the constructor,
      //    define it.
      // 4) If this is a stub, define it.
      // 5) Otherwise, do not define the type, but declare it in the registry
      //    so that we can use it for missing property checks.
      if (objectType.hasProperty(propName) || !objectType.isInstanceType()) {
        if ("prototype".equals(propName)) {
          objectType.defineDeclaredProperty(propName, rightType, getprop);
        } else {
          objectType.defineInferredProperty(propName, rightType, getprop);
        }
      } else if (propCreationInConstructor) {
        objectType.defineInferredProperty(propName, rightType, getprop);
      } else {
        registry.registerPropertyOnType(propName, objectType);
      }
    }
  }
}