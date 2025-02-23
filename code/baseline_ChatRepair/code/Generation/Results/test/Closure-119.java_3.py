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
      // Existing handling for object literal keys
      break;
    case Token.NAME:
      // Check if inside a catch block
      if (parent != null && parent.getType() == Token.CATCH) {
        name = n.getString();
        isSet = true;
        type = Name.Type.OTHER;
        handleSetFromLocal(module, scope, n, parent, name);
        return;
      }

      // Existing handling for variables
      break;
    case Token.GETPROP:
      // Existing handling for property access
      break;
    default:
      return;
  }

  // Remaining processing
  // We are only interested in global names.
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
