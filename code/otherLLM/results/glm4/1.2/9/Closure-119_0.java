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
      // This may be a key in an object literal declaration.
      name = null;
      if (parent != null && parent.isObjectLit()) {
        name = getNameForObjLitKey(n);
      }
      if (name == null) {
        return;
      }
      isSet = true;
      switch (n.getType()) {
        case Token.STRING_KEY:
          type = getValueType(n.getFirstChild());
          break;
        case Token.GETTER_DEF:
          type = Name.Type.GET;
          break;
        case Token.SETTER_DEF:
          type = Name.Type.SET;
          break;
        default:
          throw new IllegalStateException("unexpected:" + n);
      }
      break;
    case Token.NAME:
      // This may be a variable get or set.
      if (parent != null) {
        switch (parent.getType()) {
          case Token.VAR:
            isSet = true;
            Node rvalue = n.getFirstChild();
            type = rvalue == null ? Name.Type.OTHER : getValueType(rvalue);
            break;
          case Token.ASSIGN:
            if (parent.getFirstChild() == n) {
              isSet = true;
              type = getValueType(n.getNext());
            }
            break;
          case Token.GETPROP:
            return;
          case Token.FUNCTION:
            Node gramps = parent.getParent();
            if (gramps == null || NodeUtil.isFunctionExpression(parent)) {
              return;
            }
            isSet = true;
            type = Name.Type.FUNCTION;
            break;
          case Token.INC:
          case Token.DEC:
            isSet = true;
            type = Name.Type.OTHER;
            break;
          default:
            if (NodeUtil.isAssignmentOp(parent) && parent.getFirstChild() == n) {
              isSet = true;
              type = Name.Type.OTHER;
            }
        }
      }
      name = n.getString();
      break;
    case Token.GETPROP:
      // This may be a namespaced name get or set.
      if (parent != null) {
        switch (parent.getType()) {
          case Token.ASSIGN:
            if (parent.getFirstChild() == n) {
              isSet = true;
              type = getValueType(n.getNext());
              isPropAssign = true;
            }
            break;
          case Token.INC:
          case Token.DEC:
            isSet = true;
            type = Name.Type.OTHER;
            break;
          case Token.GETPROP:
            return;
          default:
            if (NodeUtil.isAssignmentOp(parent) && parent.getFirstChild() == n) {
              isSet = true;
              type = Name.Type.OTHER;
            }
        }
      }
      name = n.getQualifiedName();
      if (name == null) {
        return;
      }
      break;
    default:
      return;
  }

  // We are only interested in global names.
  if (!isGlobalNameReference(name, scope)) {
    return;
  }

  // Process the name to be used for collecting
  if (name != null && TokenStream.isJSIdentifier(name)) {
    String finalName = name;
    if (n.getType() == Token.STRING_KEY) {
      finalName = getNameForObjLitKey(n);
    }
    if (finalName != null) {
      collectInternal(module, scope, n, finalName, isSet, type, isPropAssign);
    } else {
      return;
    }
  }
}

private void collectInternal(JSModule module, Scope scope, Node n, String name, boolean isSet, Name.Type type, boolean isPropAssign) {
  // Implementation for collecting details based on the node and its properties
  // ...
}