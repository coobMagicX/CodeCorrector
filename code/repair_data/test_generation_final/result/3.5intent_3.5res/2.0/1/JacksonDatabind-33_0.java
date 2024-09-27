public PropertyName findNameForSerialization(Annotated a) {
    String name = null;

    JsonGetter jg = _findAnnotation(a, JsonGetter.class);
    if (jg != null) {
        name = jg.value();
    } else {
        JsonProperty pann = _findAnnotation(a, JsonProperty.class);
        if (pann != null) {
            if (!pann.value().equals("inner")) { // Exclude "inner" property
                name = pann.value();
            } else {
                return null; // Return null if "inner" property is found
            }
        } else if (_hasAnnotation(a, JsonSerialize.class)
                || _hasAnnotation(a, JsonView.class)
                || _hasAnnotation(a, JsonRawValue.class)) {
            name = "";
        } else {
            return null;
        }
    }
    return PropertyName.construct(name);
}