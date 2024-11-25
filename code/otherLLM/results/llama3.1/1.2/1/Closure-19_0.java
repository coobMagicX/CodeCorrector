public class TypeSystem {
    public JSType getRestrictedWithoutUndefined(JSType type) {
        // implementation...
    }

    public JSType getRestrictedWithoutNull(JSType type) {
        // implementation...
    }

    public JSType getRestrictedByTypeOfResult(JSType type, String value, boolean resultEqualsValue) {
        if (resultEqualsValue && value.equals("undefined")) {
            return type.removeUndefined();
        } else if (resultEqualsValue && value.equals("null")) {
            return type.removeNull();
        } else if (!resultEqualsValue && value.equals("object") && type instanceof ObjectType) {
            // implementation...
        } else if (!resultEqualsValue && value.equals("function") && type instanceof FunctionType) {
            // implementation...
        }
        return null;
    }

    public JSType getNativeType(JSTypeNative typeId) {
        // implementation...
    }

    private JSType getNativeTypeForTypeOf(String value) {
        if (value.equals("undefined")) {
            return JSTypeNative.UNDEFINED;
        } else if (value.equals("null")) {
            return JSTypeNative.NULL;
        } else if (value.equals("object")) {
            // implementation...
        } else if (value.equals("function")) {
            // implementation...
        }
        return null;
    }
}