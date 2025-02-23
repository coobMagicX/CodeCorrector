public void collect(JSModule module, Scope scope, Node n) {
    Node parent = n.getParent();

    String name = null; // Initialize name to null
    boolean isSet = false;
    Name.Type type = Name.Type.OTHER;
    boolean isPropAssign = false;

    switch (n.getType()) {
        case Token.GETTER_DEF:
        case Token.SETTER_DEF:
        case Token.STRING_KEY:
            // This may be a key in an object literal declaration.
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
                    throw new IllegalStateException("Unexpected node type: " + n.getType());
            }
            break;
        case Token.NAME:
            name = n.getString();
            if (parent != null) {
                switch (parent.getType()) {
                    case Token.VAR:
                    case Token.CONST:
                    case Token.LET:
                        isSet = true;
                        Node rvalue = n.getFirstChild();
                        type = rvalue == null ? Name.Type.OTHER : getValueType(rvalue);
                        break;
                    case Token.ASSIGN:
                        isSet = parent.getFirstChild() == n;
                        if (isSet) {
                            type = getValueType(n.getNext());
                        }
                        break;
                    case Token.INC:
                    case Token.DEC:
                        isSet = true;
                        type = Name.Type.OTHER;
                        break;
                    case Token.CATCH:
                        if (parent.getFirstChild() == n) {
                            type = Name.Type.OTHER;
                            isSet = true; // As this is the definition point
                        }
                        break;
                    default:
                        if (NodeUtil.isAssignmentOp(parent) &&
                            parent.getFirstChild() == n) {
                            isSet = true;
                            type = Name.Type.OTHER;
                        }
                }
            }
            break;
        case Token.GETPROP:
            name = n.getQualifiedName();
            if (name == null) {
                return;
            }
            if (parent != null && NodeUtil.isAssignmentOp(parent) &&
                parent.getFirstChild() == n) {
                isSet = true;
                type = getValueType(n.getNext());
                isPropAssign = true;
            }
            break;
        default:
            return;
    }

    // Continue only if name is properly initialized and not null
    if (name == null || !isGlobalNameReference(name, scope)) {
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
