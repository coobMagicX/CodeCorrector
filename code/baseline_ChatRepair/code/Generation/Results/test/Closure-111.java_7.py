protected JSType caseTopType(JSType topType) {
  if (topType.isSubtypeOf(getNativeArrayType())) {
    return getNativeArrayType(); // or a specific JSType representing '