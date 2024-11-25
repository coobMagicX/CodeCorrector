private void visitNew(NodeTraversal t, Node n) {
  Node constructor = n.getFirstChild();
  if (constructor == null || !isConstructorName(constructor)) {
    report(t, n, NOT_A_CONSTRUCTOR);
    return;
  }

  JSType type = getJSType(constructor).restrictByNotNullOrUndefined();
  if (type.isConstructor() || type.isEmptyType() || type.isUnknownType()) {
    FunctionType fnType = type.toMaybeFunctionType();
    if (fnType != null) {
      visitParameterList(t, n, fnType);
      ensureTyped(t, n, fnType.getInstanceType());
    } else {
      report(t, n, TYPE_ERROR_IN_CONSTRUCTOR);
      ensureTyped(t, n);
    }
  } else {
    report(t, n, NOT_A_CONSTRUCTOR);
    ensureTyped(t, n);
  }
}

// Helper method to determine if the given Node is a constructor name
private boolean isConstructorName(Node node) {
  String functionName = node.getString();
  // Assuming that constructor names are known and can be checked against a set or list
  Set<String> knownConstructors = new HashSet<>(Arrays.asList("String", "Number", "Object"));
  return knownConstructors.contains(functionName);
}