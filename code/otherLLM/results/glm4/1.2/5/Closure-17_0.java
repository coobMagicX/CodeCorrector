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
        JSType enumType = createEnumTypeFromNodes(rValue, lValue.getQualifiedName(), info, lValue);
        if (enumType != null && !enumType.isUnknownType()) {
          return enumType;
        }
      }
    } else if (info.isConstructor() || info.isInterface()) {
      JSType functionType = createFunctionTypeFromNodes(
          rValue, lValue.getQualifiedName(), info, lValue);
      if (functionType != null && !functionType.isUnknownType()) {
        return functionType;
      }
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
            // This is used by Closure's base namespace for esoteric
            // reasons.
            Node firstClause = rValue.getFirstChild();
            Node secondClause = firstClause.getNext();
            boolean namesMatch = firstClause.isName()
                && lValue.isName()
                && firstClause.getString().equals(lValue.getString());
            if (namesMatch && secondClause.getJSType() != null
                && !secondClause.getJSType().isUnknownType()) {
              knownType = secondClause.getJSType();
            }
          }
        }
        if (knownType != null) {
          return knownType;
        }
      }
    }
  }

  // Ensure that the default return is a number type or an unknown type, not an object.
  JSType defaultValue = getDeclaredTypeInAnnotation(sourceName, lValue, info);
  if (!defaultValue.isUnknownType() && !defaultValue.isObject()) {
    return defaultValue;
  } else {
    return JSPrimitiveType.NUMBER;
  }
}