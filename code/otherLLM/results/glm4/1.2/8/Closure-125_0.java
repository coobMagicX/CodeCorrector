private void visitNew(NodeTraversal t, Node n) {
  // Retrieve the first child of the node to check for a constructor call
  Node constructor = n.getFirstChild();
  if (constructor == null || !constructor.isConstructor()) {
    report(t, n, NOT_A_CONSTRUCTOR);
    ensureTyped(t, n); // Ensure typing is done regardless of constructor status
    return;
  }

  // Get the type of the node and restrict it by not-null or undefined
  JSType type = getJSType(constructor).restrictByNotNullOrUndefined();

  // Check if the type is a constructor, an empty type, or an unknown type
  if (type.isConstructor() || type.isEmptyType() || type.isUnknownType()) {
    FunctionType fnType = type.toMaybeFunctionType();
    if (fnType != null) {
      visitParameterList(t, n, fnType); // Visit the parameter list of the constructor
      ensureTyped(t, n, fnType.getInstanceType()); // Ensure typed with instance type of function type
    } else {
      report(t, n, NOT_A_CONSTRUCTOR); // Report an error if it is not a constructor call
      ensureTyped(t, n); // Ensure typing is done regardless of constructor status
    }
  } else {
    report(t, n, NOT_A_CONSTRUCTOR);
    ensureTyped(t, n); // Ensure typing is done regardless of constructor status
  }
}