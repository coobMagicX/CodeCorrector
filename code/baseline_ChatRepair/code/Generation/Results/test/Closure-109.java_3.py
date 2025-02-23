private Node parseContextTypeExpression(JsDocToken token) {
    switch (token) {
        case BANG:
            return parseNonNullableType