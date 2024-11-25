// Get native type for given enum value
JSTypeNative enumValue = JSTypeNative.STRING;
JSType nativeType = getNativeType(enumValue);
System.out.println("Native type: " + nativeType);

// Apply restriction based on typeof result
String value = "function";
boolean equalsValue = true;
JSType restrictedType = getRestrictedByTypeOfResult(nativeType, value, equalsValue);
System.out.println("Restricted type: " + restrictedType);

// Directly get the native JavaScript type for a given string value
value = "string";
nativeType = getNativeTypeForTypeOf(value);
System.out.println("Native JavaScript type: " + nativeType);