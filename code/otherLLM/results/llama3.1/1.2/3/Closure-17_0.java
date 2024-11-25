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
            knownType = rValue.getJSType();
          } else if (rValue.isOr()) {
            // Check for a very specific JS idiom:
            // var x = x || TYPE;
            Node firstClause = rValue.getFirstChild();
            Node secondClause = firstClause.getNext();
            boolean namesMatch = firstClause.isName()
                && lValue.isName()
                && firstClause.getString().equals(lValue.getString());
            if (namesMatch && secondClause.getJSType() != null
                && !secondClause.getJSType().isUnknownType()) {
              knownType = secondClause.getJSType();
            }
          } else if (rValue.isObjectLit()) {
            // Check for a case where an annotated constant is assigned an object literal with specific types.
            JSType typeCast = DeferredSetType(rValue, null);
            if (typeCast != null && !typeCast.isUnknownType()) {
              knownType = typeCast;
            }
          }
        }
      }
    }
  }

  // If the inferred type is not yet computed for rValue, try to compute it.
  if (knownType == null && info.hasType() && rValue != null) {
    DeferredSetType(rValue, getDeclaredTypeInAnnotation(sourceName, lValue, info));
    knownType = rValue.getJSType();
  }

  return knownType != null ? knownType : getDeclaredTypeInAnnotation(sourceName, lValue, info);
}