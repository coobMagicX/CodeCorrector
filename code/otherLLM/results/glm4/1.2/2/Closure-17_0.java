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
          if (rValue.getJSType() != null && !rValue.getJSType().isUnknownType()) {
            // If rValue has a type-cast, we use the type in the type-cast.
            // If rValue's type was already computed during scope creation,
            // then we can safely use that.
            return rValue.getJSType();
          } else if (rValue.isOr() && !info.hasType()) {
            // Check for a very specific JS idiom:
            // var x = x || TYPE;
            // This is used by Closure's base namespace for esoteric
            // reasons. Only consider this case if no type has been explicitly set.
            Node firstClause = rValue.getFirstChild();
            Node secondClause = firstClause.getNext();
            boolean namesMatch = firstClause.isName()
                && lValue.isName()
                && firstClause.getString().equals(lValue.getString());
            if (namesMatch) {
              knownType = secondClause.getJSType();
            }
          }
        }
        // If we found a known type, return it; otherwise, continue looking for the type.
        if (knownType != null && !knownType.isUnknownType()) {
          return knownType;
        }
      }
    }
  }
  // If no explicit type has been found, use default inference or return UnknownType.
  return JSType.UNKNOWN_TYPE;
}