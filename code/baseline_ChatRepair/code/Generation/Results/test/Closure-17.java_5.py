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
      return resolveConstantType(rValue, lValue);
    }
  }

  if (rValue != null && rValue.isFunction() && shouldUseFunctionLiteralType(
        JSType.toMaybeFunctionType(rValue.getJSType()), info, lValue)) {
    return rValue.getJSType();
  }

  // If nothing else, return any type.
  return getNativeType(JSTypeNative.UNKNOWN_TYPE);
}

private JSType resolveConstantType(@Nullable Node rValue, Node lValue) {
  if (rValue != null && rValue.getJSType() != null && !rValue.getJSType().isUnknownType()) {
    return rValue.getJSType();
  }

  if (rValue != null && rValue.isOr()) {
    // Check for the specific idiom: var x = x || TYPE;
    Node firstClause = rValue.getFirstChild();
    Node secondClause = firstClause.getNext();
    boolean namesMatch = firstClause.isName() && lValue.isName() && firstClause.getString().equals(lValue.getString());
    if (namesMatch && secondClause.getJSType() != null && !secondClause.getJSType().isUnknownType()) {
      return secondClause.getJSType();
    }
  }

  // Return unknown type if constant type cannot be resolved from the current assignments and definitions.
  return getNativeType(JSTypeNative.UNKNOWN_TYPE);
}
