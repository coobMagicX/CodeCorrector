public boolean handlePropertyValue(JsonParser p, DeserializationContext ctxt, String propName, Object bean) throws IOException {
    Object ob = _nameToPropertyIndex.get(propName);
    if (ob == null) {
        return false;
    }
    // 28-Nov-2016, tatu: For [databind#291], need separate handling
    if (ob instanceof List<?>) {
        Iterator<Integer> it = ((List<Integer>) ob).iterator();
        Integer index = it.next();

        ExtTypedProperty prop = _properties[index];
        // For now, let's assume it's same type (either type id OR value)
        // for all mappings, so we'll only check first one
        if (prop.hasTypePropertyName(propName)) {
            String typeId = p.getText();
            p.skipChildren();
            _typeIds[index] = typeId;
            while (it.hasNext()) {
                _typeIds[it.next()] = typeId;
            }
        } else {
            @SuppressWarnings("resource")
            TokenBuffer tokens = new TokenBuffer(p, ctxt);
            tokens.copyCurrentStructure(p);
            _tokens[index] = tokens;
            while (it.hasNext()) {
                _tokens[it.next()] = tokens;
            }
        }
        return true;
    }

    // Updated code: handle deserialization of enum values
    if (ob instanceof Integer) {
        int index = ((Integer) ob).intValue();
        ExtTypedProperty prop = _properties[index];
        boolean canDeserialize;
        if (prop.hasTypePropertyName(propName)) {
            JavaType enumType = prop.getProperty().getType();
            Object value = ctxt.readValue(p, enumType);
            _typeIds[index] = value.toString();
            p.skipChildren();
            canDeserialize = (bean != null) && (_tokens[index] != null);
        } else {
            @SuppressWarnings("resource")
            TokenBuffer tokens = new TokenBuffer(p, ctxt);
            tokens.copyCurrentStructure(p);
            _tokens[index] = tokens;
            canDeserialize = (bean != null) && (_typeIds[index] != null);
        }
        // Minor optimization: let's deserialize properties as soon as
        // we have all pertinent information:
        if (canDeserialize) {
            String typeId = _typeIds[index];
            // clear stored data, to avoid deserializing+setting twice:
            _typeIds[index] = null;
            _deserializeAndSet(p, ctxt, bean, index, typeId);
            _tokens[index] = null;
        }
        return true;
    }

    return false;
}