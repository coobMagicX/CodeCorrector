private void visitNew(NodeTraversal t, Node n) {
  Node constructor = n.getFirstChild();
  JSType type = getJSType(constructor).restrictByNotNullOrUndefined();
  if (type.isConstructor() || type.isEmptyType() || type.isUnknownType()) {
    FunctionType fnType = type.toMaybeFunctionType();
    if (fnType != null) {
      visitParameterList(t, n, fnType);
      ensureTyped(t, n, fnType.getInstanceType());
    } else {
      ensureTyped(t, n);
    }
  } else if (constructor.isQualifiedName()) {
    // Check if the constructor is a qualified name
    // and compare it with the parent class/interface
    JSType parentType = getJSType(constructor.getParent());
    if (parentType.isFunctionType()) {
      FunctionType parentFnType = parentType.toMaybeFunctionType();
      if (parentFnType.isInterfaceDefinition()) {
        // Check if the constructor implements the same interface as the parent
        if (implementsInterface(constructor, parentFnType)) {
          visitParameterList(t, n, parentFnType);
          ensureTyped(t, n, parentFnType.getInstanceType());
          return;
        }
      }
    }
    report(t, n, NOT_A_CONSTRUCTOR);
    ensureTyped(t, n);
  } else {
    report(t, n, NOT_A_CONSTRUCTOR);
    ensureTyped(t, n);
  }
}

private boolean implementsInterface(Node constructor, FunctionType interfaceType) {
  // Check if the constructor implements the same interface as the parent
  String interfaceName = interfaceType.getReferenceName();
  if (constructor.isQualifiedName()) {
    String constructorName = constructor.getQualifiedName();
    return constructorName.equals(interfaceName);
  }
  return false;
}