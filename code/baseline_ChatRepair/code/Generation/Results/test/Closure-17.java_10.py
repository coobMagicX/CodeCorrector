private JSType getDeclaredType(String sourceName, JSDocInfo info, Node lValue, @Nullable Node rValue) {
    if (info != null) {
        if (info.hasType()) {
            return getDeclaredTypeInAnnotation(sourceName, lValue, info);
        } else if (info.hasEnumParameterType()) {
            if (rValue != null && rValue.isObjectLit()) {
                return rValue.getJSType();
            } else {
                return createEnumTypeFromNodes(rValue, lValue.getQualifiedName(), info, lValue);
            }
        } else if (info.isConstructor() || info.isInterface()) {
            return createFunctionTypeFromNodes(rValue, lValue.getQualifiedName(), info, lValue);
        }
    }
    
    if (rValue != null && rValue.isFunction()) {
        JSType functionType = rValue.getJSType();
        if (info != null && shouldUseFunctionLiteralType(JSType.toMaybeFunctionType(functionType), info, lValue)) {
            return functionType;
        }
    }
    
    if (rValue != null && info != null && info.isConstant()) {
        JSType rValueType = rValue.getJSType();
        if (rValueType != null && !rValueType.isUnknownType()) {
            return rValueType;
        }

        // Handle specific JS idiom: var x = x || TYPE;
        if (rValue.isOr()) {
            Node firstClause = rValue.getFirstChild();
            Node secondClause = firstClause.getNext();
            if (firstClause.isName() && lValue.isName() && firstClause.getString().equals(lValue.getString())) {
                JSType secondClauseType = secondClause.getJSType();
                if (secondClauseType != null && !secondClauseType.isUnknownType()) {
                    return secondClauseType;
                }
            }
        }
    }

    // As a default fallback to a native type or to unknown to prevent returning inconsistent or null
    if (rValue != null && rValue.getJSType() != null) {
        return rValue.getJSType();  // Use right value's type if available
    } else {
        return getNativeType(JSTypeNative.UNKNOWN_TYPE);  // Fallback to unknown type
    }
}
