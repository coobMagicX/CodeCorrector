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
  //   /* Reset goog.bar to a no-op. */
  //   goog.bar = function() {};
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
  }
}

void defineSlot(Node n, Node parent, JSType type, boolean inferred) {
  String variableName = n.getString();
  Preconditions.checkArgument(!variableName.isEmpty());

  boolean isGlobalVar = n.isName() && scope.isGlobal();
  boolean shouldDeclareOnGlobalThis =
      isGlobalVar &&
      (parent.isVar() ||
       parent.isFunction());

  // If n is a property, then we should really declare it in the
  // scope where the root object appears. This helps out people
  // who declare "global" names in an anonymous namespace.
  Scope scopeToDeclareIn = scope;
  if (n.isGetProp() && !scope.isGlobal() &&
      isQnameRootedInGlobalScope(n)) {
    Scope globalScope = scope.getGlobalScope();

    // don't try to declare in the global scope if there's
    // already a symbol there with this name.
    if (!globalScope.isDeclared(variableName, false)) {
      scopeToDeclareIn = scope.getGlobalScope();
    }
  }

  // declared in closest scope?
  CompilerInput input = compiler.getInput(inputId);
  if (scopeToDeclareIn.isDeclared(variableName, false)) {
    Var oldVar = scopeToDeclareIn.getVar(variableName);
    validator.expectUndeclaredVariable(
        sourceName, input, n, parent, oldVar, variableName, type);
  } else {
    if (!inferred) {
      setDeferredType(n, type);
    }

    // The input may be null if we are working with a AST snippet.
    boolean isExtern = n.isFromExterns();
    Var newVar =
        scopeToDeclareIn.declare(variableName, n, type, input, inferred);

    // We need to do some additional work for constructors and interfaces.
    FunctionType fnType = JSType.toMaybeFunctionType(type);
    if (fnType != null &&
        // We don't want to look at empty function types.
        !type.isEmptyType()) {
      if ((fnType.isConstructor() || fnType.isInterface()) &&
          !fnType.equals(getNativeType(U2U_CONSTRUCTOR_TYPE))) {
        // Declare var.prototype in the scope chain.
        FunctionType superClassCtor = fnType.getSuperClassConstructor();
        ObjectType.Property prototypeSlot = fnType.getSlot("prototype");

        String prototypeName = variableName + ".prototype";

        // There are some rare cases where the prototype will already
        // be declared. See TypedScopeCreatorTest#testBogusPrototypeInit.
        // Fortunately, other warnings will complain if this happens.
        if (scopeToDeclareIn.getOwnSlot(prototypeName) == null) {
          // When we declare the function prototype implicitly, we
          // want to make sure that the function and its prototype
          // are declared at the same node. We also want to make sure
          // that the if a symbol has both a Var and a JSType, they have
          // the same node.
          //
          // This consistency is helpful to users of SymbolTable,
          // because everything gets declared at the same place.
          prototypeSlot.setNode(n);

          scopeToDeclareIn.declare(prototypeName,
              n, prototypeSlot.getType(), input,
              /* declared iff there's an explicit supertype */
              superClassCtor == null ||
              superClassCtor.getInstanceType().equals(
                  getNativeType(OBJECT_TYPE)));
        }

        // Make sure the variable is initialized to something if
        // it constructs itself.
        if (newVar.getInitialValue() == null &&
            !isExtern &&
            // We want to make sure that when we declare a new instance
            // type (with @constructor) that there's actually a ctor for it.
            // This doesn't apply to structural constructors
            // (like function(new:Array). Checking the constructed
            // type against the variable name is a sufficient check for
            // this.
            variableName.equals(
                fnType.getInstanceType().getReferenceName())) {
          compiler.report(
              JSError.make(sourceName, n,
                  fnType.isConstructor() ?
                      CTOR_INITIALIZER : IFACE_INITIALIZER,
                  variableName));
        }
      }
    }
  }

  if (shouldDeclareOnGlobalThis) {
    ObjectType globalThis =
        typeRegistry.getNativeObjectType(GLOBAL_THIS);
    if (inferred) {
      globalThis.defineInferredProperty(variableName,
          type == null ?
          getNativeType(JSTypeNative.NO_TYPE) :
          type,
          n);
    } else {
      globalThis.defineDeclaredProperty(variableName, type, n);
    }
  }

  if (isGlobalVar && "Window".equals(variableName)
      && type != null
      && type.isFunctionType()
      && type.isConstructor()) {
    FunctionType globalThisCtor =
        typeRegistry.getNativeObjectType(GLOBAL_THIS).getConstructor();
    globalThisCtor.getInstanceType().clearCachedValues();
    globalThisCtor.getPrototype().clearCachedValues();
    globalThisCtor
        .setPrototypeBasedOn((type.toMaybeFunctionType()).getInstanceType());
  }
}