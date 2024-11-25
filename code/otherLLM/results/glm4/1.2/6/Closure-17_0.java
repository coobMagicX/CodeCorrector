private JSType getDeclaredType(String sourceName, JSDocInfo info,
    Node lValue, @Nullable Node rValue) {
  if (info != null && info.hasType()) {
    return getDeclaredTypeInAnnotation(sourceName, lValue, info);
  } else if (rValue != null && rValue.isFunction() &&
      shouldUseFunctionLiteralType(
          JSType.toMaybeFunctionType(rValue.getJSType()), info, lValue)) {
    return rValue.getJSType();
  } else if (info != null) {
    // Ensure that the type-cast logic is correctly applied
    if (rValue != null && rValue.isTypeCast()) {
      JSType castedType = rValue.getTypeCast().getJSType();
      if (castedType != null && !castedType.isUnknownType()) {
        return castedType;
      }
    }

    // Check for the special OR idiom
    if (rValue != null && rValue.isOr() && info.isConstant() && lValue.isName() && info.hasEnumParameterType()) {
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

    // Check for enum and typedef types
    identifyNameNode(lValue, rValue, info);

    if (info.hasEnumParameterType()) {
      if (rValue != null && rValue.isObjectLit()) {
        return rValue.getJSType();
      } else {
        return createEnumTypeFromNodes(
            rValue, lValue.getQualifiedName(), info, lValue);
      }
    } else if (info.isConstructor() || info.isInterface()) {
      return createFunctionTypeFromNodes(
          rValue, lValue.getQualifiedName(), info, lValue);
    } else {
      // Check if this is constant, and if it has a known type.
      if (info.isConstant()) {
        JSType knownType = null;
        if (rValue != null) {
          if (!rValue.getJSType().isUnknownType()) {
            return rValue.getJSType();
          }
          // Additional checks could be added here if needed
        }
      }
    }
  }

  return getDeclaredTypeInAnnotation(sourceName, lValue, info);
}