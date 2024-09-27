public PropertyName findNameForSerialization(Annotated a) {
    String name = null;

    JsonGetter jg = _findAnnotation(a, JsonGetter.class);
    if (jg != null) {
        name = jg.value();
    } else {
        JsonProperty pann = _findAnnotation(a, JsonProperty.class);
        if (pann != null) {
            name = pann.value();
        } else {
            if (_hasAnnotation(a, JsonUnwrapped.class)) {
                // If JsonUnwrapped is present, we treat it as an indication to use an empty name
                // which effectively unwraps the properties into the parent.
                name = "";
            } else if (_hasAnnotation(a, JsonSerialize.class)
                    || _hasAnnotation(a, JsonView.class)
                    || _hasAnnotation(a, JsonRawValue.class)) {
                name = "";
            } else if (_findAnnotation(a, JsonDeserialize.class) != null) {
                // Handling the case where JsonDeserialize is used, which implies property is being used for deserialization
                // but might not be annotated with JsonProperty or JsonGetter.
                name = a.getName();
            } else {
                return null;
            }
        }
    }
    return PropertyName.construct(name);
}