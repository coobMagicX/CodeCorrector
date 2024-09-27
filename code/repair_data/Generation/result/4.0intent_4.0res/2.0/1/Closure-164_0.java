public boolean isSubtype(JSType other) {
  if (!(other instanceof ArrowType)) {
    return false;
  }

  ArrowType that = (ArrowType) other;

  // This is described in Draft 2 of the ES4 spec,
  // Section 3.4.7: Subtyping Function Types.

  // this.returnType <: that.returnType (covariant)
  if (!this.returnType.isSubtype(that.returnType)) {
    return false;
  }

  Node thisParam = parameters.getFirstChild();
  Node thatParam = that.parameters.getFirstChild();
  while (thisParam != null || thatParam != null) { // Changed to OR to handle different arities
    if (thisParam != null && thatParam != null) {
      JSType thisParamType = thisParam.getJSType();
      JSType thatParamType = thatParam.getJSType();
      if (thisParamType != null && (thatParamType == null || !thatParamType.isSubtype(thisParamType))) {
        return false;
      }
    } else if (thisParam != null) { // means thatParam is null
      if (!thisParam.isOptional()) { // Check if thisParam is not optional
        return false;
      }
    } // No need to check the else case for thatParam != null and thisParam == null as per spec

    boolean thisIsVarArgs = thisParam != null && thisParam.isVarArgs();
    boolean thatIsVarArgs = thatParam != null && thatParam.isVarArgs();

    if (!thisIsVarArgs && thisParam != null) {
      thisParam = thisParam.getNext();
    }
    if (!thatIsVarArgs && thatParam != null) {
      thatParam = thatParam.getNext();
    }

    if (thisIsVarArgs && thatIsVarArgs) {
      thisParam = null;
      thatParam = null;
    }
  }

  return true;
}