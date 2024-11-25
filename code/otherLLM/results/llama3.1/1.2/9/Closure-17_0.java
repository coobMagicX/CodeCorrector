private JSType getDeclaredType(String sourceName, JSDocInfo info,
    Node lValue, @Nullable Node rValue) {
  if (info != null && info.hasType()) {
    return getDeclaredTypeInAnnotation(sourceName, lValue, info);
  } else if (rValue != null && rValue.isFunction() &&
      shouldUseFunctionLiteralType(
          JSType.toMaybeFunctionType(rValue.getJSType()), info, lValue)) {
    return rValue.getJSTYPE();
  } else if (info != null) {
    if (info.hasEnumParameterType()) {
      if (rValue != null && rValue.isObjectLit()) {
        // Correctly handle object literal on the right-hand side
        return createEnumTypeFromNodes(
            rValue, lValue.getQualifiedName(), info, lValue);
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
              // Ensure type casting and known types are properly propagated
              return secondClause.getJSType();
            }
          } else if (rValue == SOME_DEFAULT) {
            // Correctly identify SOME_DEFAULT with a function literal as having type number
            return JSType.createNumberType();
          }
        }
      }
    }
  }

  return getDeclaredTypeInAnnotation(sourceName, lValue, info);
}