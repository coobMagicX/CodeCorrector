protected Object deserializeUsingPropertyBasedWithUnwrapped(JsonParser p, DeserializationContext ctxt)
    throws IOException
{
    final PropertyBasedCreator creator = _propertyBasedCreator;
    PropertyValueBuffer buffer = creator.startBuilding(p, ctxt, _objectIdReader);

    TokenBuffer tokens = new TokenBuffer(p, ctxt);
    tokens.writeStartObject();

    JsonToken t = p.getCurrentToken();
    Object bean = null;
    for (; t == JsonToken.FIELD_NAME; t = p.nextToken()) {
        String propName = p.getCurrentName();
        p.nextToken(); // to point to value
        // creator property?
        SettableBeanProperty creatorProp = creator.findCreatorProperty(propName);
        if (creatorProp != null) {
            // Last creator property to set?
            if (buffer.assignParameter(creatorProp, _deserializeWithErrorWrapping(p, ctxt, creatorProp))) {
                t = p.nextToken(); // to move to following FIELD_NAME/END_OBJECT
                try {
                    bean = creator.build(ctxt, buffer);
                    p.setCurrentValue(bean);
                } catch (Exception e) {
                    bean = wrapInstantiationProblem(e, ctxt);
                }
                if (bean.getClass() != _beanType.getRawClass()) {
                    ctxt.reportInputMismatch(creatorProp, "Cannot create polymorphic instances with unwrapped values");
                    return null;
                }
                // if so, need to copy all remaining tokens into buffer
                while (t == JsonToken.FIELD_NAME) {
                    tokens.writeFieldName(p.getCurrentName());
                    p.nextToken();
                    tokens.copyCurrentStructure(p);
                    t = p.nextToken();
                }
                tokens.writeEndObject();
                return _unwrappedPropertyHandler.processUnwrapped(p, ctxt, bean, tokens);
            }
            continue;
        }
        // Object Id property?
        if (buffer.readIdProperty(propName)) {
            continue;
        }
        // regular property? needs buffering
        SettableBeanProperty prop = _beanProperties.find(propName);
        if (prop != null) {
            buffer.bufferProperty(prop, _deserializeWithErrorWrapping(p, ctxt, prop));
            continue;
        }
        // Things marked as ignorable should not be passed to any setter
        if (_ignorableProps != null && _ignorableProps.contains(propName)) {
            handleIgnoredProperty(p, ctxt, handledType(), propName);
            continue;
        }
        // how about any setter? We'll get copies but...
        if (_anySetter == null) {
            tokens.writeFieldName(propName);
            tokens.copyCurrentStructure(p);
        } else {
            TokenBuffer b2 = TokenBuffer.asCopyOfValue(p);
            tokens.writeFieldName(propName);
            tokens.append(b2);
            try {
                buffer.bufferAnyProperty(_anySetter, propName, _anySetter.deserialize(b2.asParserOnFirstToken(), ctxt));
            } catch (Exception e) {
                wrapAndThrow(e, _beanType.getRawClass(), propName, ctxt);
            }
        }
    }

    // We hit END_OBJECT, so:
    if (bean == null) {
        try {
            bean = creator.build(ctxt, buffer);
        } catch (Exception e) {
            wrapInstantiationProblem(e, ctxt);
            return null; // never gets here
        }
    }
    return _unwrappedPropertyHandler.processUnwrapped(p, ctxt, bean, tokens);
}