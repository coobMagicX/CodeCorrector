public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
    if (_isInt) {
        visitIntFormat(visitor, typeHint, JsonParser.NumberType.INT);
    } else {
        Class<?> h = handledType();
        if (h == BigDecimal.class) {
            visitFloatFormat(visitor, typeHint, JsonParser.NumberType.BIG_DECIMAL);
        } else if (h == BigInteger.class) {
            visitIntFormat(visitor, typeHint, JsonParser.NumberType.BIG_INTEGER);
        } else {
            visitor.expectNumberFormat(typeHint);
        }
    }
}