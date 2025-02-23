public void collect(JSModule module, Scope scope, Node n) {
  Node parent = n.getParent();

  String name;
  boolean isSet = false;
  Name.Type type = Name.Type.OTHER;
  boolean isPropAssign = false;

  switch (n.getType()) {
    case Token.GETTER_DEF:
    case Token.SETTER_DEF:
    case Token.STRING_KEY:
      // handle object literal keys
      ...
      break;
    case Token.NAME:
      // handle variable references
      if (parent != null) {
        switch (parent.getType()) {
          case Token.CATCH:
            // When inside a catch clause, the name is defined within the catch scope.
            isSet = true;
            type = Name.Type.OTHER;
            break;
          case Token.VAR:
            // handling var declarations
            ...
            break;
          case Token.ASSIGN:
            // handling assignments
            ...
            break;
          ...
          default:
            // other cases
            ...
            break;
        }
      }
      name = n.getString();
      break;
    case Token.GETPROP:
      // handle property accesses
      ...
      break;
    default:
      return;
  }

  // Perform checks on global names and handle sets and gets accordingly
  if (!isGlobalNameReference(name, scope)) {
    return;
  }

  if (isSet) {
    if (isGlobalScope(scope)) {
      handleSetFromGlobal(module, scope, n, parent, name, isPropAssign, type);
    } else {
      handleSetFromLocal(module, scope, n, parent, name);
    }
  } else {
    handleGet(module, scope, n, parent, name);
  }
}
