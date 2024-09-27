public Object complete(JsonParser p, DeserializationContext ctxt,
                       PropertyValueBuffer buffer, PropertyBasedCreator creator)
    throws IOException
{
    // first things first: deserialize all data buffered:
    final int len = _properties.length;
    Object[] values = new Object[len];
    for (int i = 0; i < len; ++i) {
        String typeId = _typeIds[i];
        final ExtTypedProperty extProp = _properties[i];
        if (typeId == null) {
            if (_tokens[i] == null) {
                // Allow missing both type and property (may already have been set, too)
                continue;
            }
            if (!extProp.hasDefaultType()) {
                ctxt.reportInputMismatch(_beanType,
                        "Missing external type id property '%s'",
                        extProp.getTypePropertyName());
            } else {
                typeId = extProp.getDefaultTypeId();
            }
        } else if (_tokens[i] == null) {
            SettableBeanProperty prop = extProp.getProperty();
            ctxt.reportInputMismatch(_beanType,
                    "Missing property '%s' for external type id '%s'",
                    prop.getName(), _properties[i].getTypePropertyName());
        }
        // Handling Enums specifically for deserialization
        if (typeId.equals("ENUM_TYPE")) {
            JsonParser p2 = _tokens[i].asParser(p);
            p2.nextToken(); // Move to the value
            String enumValue = p2.getText();
            values[i] = Enum.valueOf(extProp.getProperty().getType().getRawClass(), enumValue);
        } else {
            values[i] = _deserialize(p, ctxt, i, typeId);
        }

        final SettableBeanProperty prop = extProp.getProperty();
        // If it's a creator property, fill in
        if (prop.getCreatorIndex() >= 0) {
            buffer.assignParameter(prop, values[i]);

            SettableBeanProperty typeProp = extProp.getTypeProperty();
            if ((typeProp != null) && (typeProp.getCreatorIndex() >= 0)) {
                buffer.assignParameter(typeProp, typeId);
            }
        }
    }
    Object bean = creator.build(ctxt, buffer);
    // Assign non-creator properties
    for (int i = 0; i < len; ++i) {
        SettableBeanProperty prop = _properties[i].getProperty();
        if (prop.getCreatorIndex() < 0) {
            prop.set(bean, values[i]);
        }
    }
    return bean;
}