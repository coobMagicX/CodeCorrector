private JSType getDeclaredType(String sourceName, JSDocInfo info,
    Node lValue, @Nullable Node rValue) {
  if (info != null && info.hasType()) {
    return getDeclaredTypeInAnnotation(sourceName, lValue, info);
  } else if (rValue != null && rValue.isFunction() &&
      shouldUseFunctionLiteralType(
          JSType.toMaybeFunctionType(rValue.getJSType()), info, lValue)) {
    return rValue.getJSType();
  } else if (info != null) {
    if (info.hasEnumParameterType()) {
      if (rValue != null && rValue.isObjectLit()) {
        // Attempt to create an enum type from the object literal
        JSType rValueType = rValue.getJSType();
        if (rValueType != null && !rValueType.isUnknownType() &&
            rValueType.isEnumType()) {
          return rValueType;
        } else {
          // If it's not a known enum type, use createEnumTypeFromNodes
          return createEnumTypeFromNodes(rValue, lValue.getQualifiedName(), info, lValue);
        }
      } else {
        // If rValue is not an object literal, create an enum type from nodes
        return createEnumTypeFromNodes(rValue, lValue.getQualifiedName(), info, lValue);
      }
    } else if (info.isConstructor() || info.isInterface()) {
      return createFunctionTypeFromNodes(
          rValue, lValue.getQualifiedName(), info, lValue);
    } else {
      // Check if this is a constant and has a known type
      if (info.isConstant()) {
        JSType knownType = null;
        if (rValue != null) {
          if (rValue.getJSType() != null && !rValue.getJSType().isUnknownType()) {
            return rValue.getJSType();
          } else if (rValue.isOr()) {
            Node firstClause = rValue.getFirstChild();
            Node secondClause = firstClause.getNext();
            boolean namesMatch = firstClause.isName()
                && lValue.isName()
                && firstClause.getString().equals(lValue.getString());
            if (namesMatch && secondClause.getJSType() != null
                && !secondClause.getJSType().isUnknownType()) {
              return secondClause.getJSType();
            }
          }
        }
      }
    }
  }

  return getDeclaredTypeInAnnotation(sourceName, lValue, info);
}