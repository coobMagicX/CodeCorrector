private JSType getDeclaredType(String sourceName, JSDocInfo info, Node lValue, @Nullable Node rValue) {
  if (info != null) {
    if (info.hasType()) {
      return getDeclaredTypeInAnnotation(sourceName, lValue, info);
    }
    
    if (info.hasEnumParameterType()) {
      if (rValue != null && rValue.isObjectLit()) {
        return rValue.getJSType();
      } else {
        return createEnumTypeFromNodes(rValue, lValue.getQualifiedName(), info, lValue);
      }
    }
    
    if (info.isConstructor() || info.isInterface()) {
      return createFunctionTypeFromNodes(rValue, lValue.getQualifiedName(), info, lValue);
    }
    
    if (info.isConstant()) {
      return getConstantJSType(rValue, lValue, info);
    }
  }

  if (rValue != null && rValue.isFunction() && shouldUseFunctionLiteralType(
          JSType.toMaybeFunctionType(rValue.getJSType()), info, lValue)) {
    return rValue.getJSType();
  }

  return info != null ? getDeclaredTypeInAnnotation(sourceName, lValue, info) : null;
}

private JSType getConstantJSType(@Nullable Node rValue, Node lValue, JSDocInfo info) {
  // Handling for constants and variable checks
  if (rValue != null) {
    if (rValue.getJSType() != null && !rValue.getJSType().isUnknownType()) {
      // Provide the type of the right value if not unknown
      return rValue.getJSType();
    } else if (rValue.isOr()) {
      // Check for pattern var x = x || TYPE;
      Node firstClause = rValue.getFirstChild();
      Node secondClause = firstClause.getNext();
      if (firstClause.isName() && lValue.isName() && firstClause.getString().equals(lValue.getString())) {
        if (secondClause.getJSType() != null && !secondClause.getJSType().isUnknownType()) {
          return secondClause.getJSType();
        }
      }
    }
  }

  // Return default unknown or any type
  return info != null && info.getType() != null ? info.getType() : getNativeType(JSTypeNative.UNKNOWN_TYPE);
}
