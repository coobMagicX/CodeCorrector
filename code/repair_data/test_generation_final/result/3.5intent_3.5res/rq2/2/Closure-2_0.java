private void checkInterfaceConflictProperties(NodeTraversal t, Node n,
    String functionName, HashMap<String, ObjectType> properties,
    HashMap<String, ObjectType> currentProperties,
    ObjectType interfaceType) {
  ObjectType implicitProto = interfaceType.getImplicitPrototype();
  Set<String> currentPropertyNames;
  // This can be the case if interfaceType is proxy to a non-existent
  // object (which is a bad type annotation, but shouldn't crash).
  currentPropertyNames = implicitProto.getOwnPropertyNames();
  for (String name : currentPropertyNames) {
    ObjectType oType = properties.get(name);
    if (oType != null) {
      if (!interfaceType.getPropertyType(name).isEquivalentTo(
          oType.getPropertyType(name))) {
        compiler.report(
            t.makeError(n, INCOMPATIBLE_EXTENDED_PROPERTY_TYPE,
                functionName, name, oType.toString(),
                interfaceType.toString()));
      }
    }
    currentProperties.put(name, interfaceType);
  }
  for (ObjectType iType : interfaceType.getCtorExtendedInterfaces()) {
    checkInterfaceConflictProperties(t, n, functionName, properties,
        currentProperties, iType);
  }
} 

public boolean shouldTraverse(
  NodeTraversal t, Node n, Node parent) {
  checkNoTypeCheckSection(n, true);
  switch (n.getType()) {
    case Token.FUNCTION:
      // normal type checking
      final Scope outerScope = t.getScope();
      final String functionPrivateName = n.getFirstChild().getString();
      if (functionPrivateName != null && functionPrivateName.length() > 0 &&
          outerScope.isDeclared(functionPrivateName, false) &&
          // Ideally, we would want to check whether the type in the scope
          // differs from the type being defined, but then the extern
          // redeclarations of built-in types generates spurious warnings.
          !(outerScope.getVar(
              functionPrivateName).getType() instanceof FunctionType)) {
        report(t, n, FUNCTION_MASKS_VARIABLE, functionPrivateName);
      }

      // TODO(user): Only traverse the function's body. The function's
      // name and arguments are traversed by the scope creator, and ideally
      // should not be traversed by the type checker.
      break;
  }
  return true;
}