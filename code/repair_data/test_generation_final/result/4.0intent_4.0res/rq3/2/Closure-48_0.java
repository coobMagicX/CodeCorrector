void maybeDeclareQualifiedName(NodeTraversal t, JSDocInfo info, Node n, Node parent, Node rhsValue) {
    Node ownerNode = n.getFirstChild();
    String ownerName = ownerNode.getQualifiedName();
    String qName = n.getQualifiedName();
    String propName = n.getLastChild().getString();
    Preconditions.checkArgument(qName != null && ownerName != null);

    Scope scope = t.getScope();
    JSTypeRegistry typeRegistry = t.getCompiler().getTypeRegistry();

    // Determining type for #1 + #2 + #3 + #4
    JSType valueType = getDeclaredType(t.getSourceName(), info, n, rhsValue);
    if (valueType == null && rhsValue != null) {
        // Determining type for #5
        valueType = rhsValue.getJSType();
    }

    if ("prototype".equals(propName)) {
        Var qVar = scope.getVar(qName);
        if (qVar != null) {
            ObjectType qVarType = ObjectType.cast(qVar.getType());
            if (qVarType != null && rhsValue != null && rhsValue.isObjectLit()) {
                typeRegistry.resetImplicitPrototype(rhsValue.getJSType(), qVarType.getImplicitPrototype());
            } else if (!qVar.isTypeInferred()) {
                return;
            }
            if (qVar.getScope() == scope) {
                scope.undeclare(qVar);
            }
        }
    }

    if (valueType == null) {
        if (parent.isExprResult()) {
            t.getCompiler().report(JSError.make(n, "Could not determine type for the expression result."));
        }
        return;
    }

    boolean inferred = true;
    if (info != null) {
        inferred = !(info.hasType() || info.hasEnumParameterType() || 
                     (info.isConstant() && valueType != null && !valueType.isUnknownType()) ||
                     (rhsValue != null && rhsValue.isFunction() && FunctionTypeBuilder.isFunctionTypeDeclaration(info)));
    }

    if (!inferred) {
        ObjectType ownerType = getObjectSlot(ownerName);
        if (ownerType != null) {
            boolean isExtern = t.getInput() != null && t.getInput().isExtern();
            if ((!ownerType.hasOwnProperty(propName) || ownerType.isPropertyTypeInferred(propName)) &&
                ((isExtern && !ownerType.isNativeObjectType()) || !ownerType.isInstanceType())) {
                ownerType.defineDeclaredProperty(propName, valueType, n);
            }
        }
        defineSlot(n, parent, valueType, inferred);
    } else if (rhsValue != null && rhsValue.isTrue()) {
        FunctionType ownerType = JSType.toMaybeFunctionType(getObjectSlot(ownerName));
        if (ownerType != null) {
            JSType ownerTypeOfThis = ownerType.getTypeOfThis();
            String delegateName = codingConvention.getDelegateSuperclassName();
            JSType delegateType = delegateName == null ? null : typeRegistry.getType(delegateName);
            if (delegateType != null && ownerTypeOfThis.isSubtype(delegateType)) {
                defineSlot(n, parent, typeRegistry.getNativeType(JSTypeNative.BOOLEAN_TYPE), true);
            }
        }
    }
}