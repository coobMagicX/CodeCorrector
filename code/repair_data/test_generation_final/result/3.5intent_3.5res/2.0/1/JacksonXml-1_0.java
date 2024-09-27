public JsonToken nextToken() throws IOException {
    _binaryValue = null;
    if (_nextToken != null) {
        JsonToken t = _nextToken;
        _currToken = t;
        _nextToken = null;
        switch (t) {
            case START_OBJECT:
                _parsingContext = _parsingContext.createChildObjectContext(-1, -1);
                break;
            case START_ARRAY:
                _parsingContext = _parsingContext.createChildArrayContext(-1, -1);
                break;
            case END_OBJECT:
            case END_ARRAY:
                _parsingContext = _parsingContext.getParent();
                _namesToWrap = _parsingContext.getNamesToWrap();
                break;
            case FIELD_NAME:
                _parsingContext.setCurrentName(_xmlTokens.getLocalName());
                break;
            default: // VALUE_STRING, VALUE_NULL
                // should be fine as is?
        }
        return t;
    }
    int token = _xmlTokens.next();

    // Need to have a loop just because we may have to eat/convert
    // a start-element that indicates an array element.
    while (token == XmlTokenStream.XML_START_ELEMENT) {
        // If we thought we might get leaf, no such luck
        if (_mayBeLeaf) {
            // leave _mayBeLeaf set, as we start a new context
            _nextToken = JsonToken.FIELD_NAME;
            _parsingContext = _parsingContext.createChildObjectContext(-1, -1);
            return (_currToken = JsonToken.START_OBJECT);
        }
        if (_parsingContext.inArray()) {
            // Yup: in array, so this element could be verified; but it won't be
            // reported anyway, and we need to process following event.
            token = _xmlTokens.next();
            _mayBeLeaf = true;
            continue;
        }
        String name = _xmlTokens.getLocalName();
        _parsingContext.setCurrentName(name);

        // Ok: virtual wrapping can be done by simply repeating current START_ELEMENT.
        // Couple of ways to do it; but start by making _xmlTokens replay the thing...
        if (_namesToWrap != null && _namesToWrap.contains(name)) {
            _xmlTokens.repeatStartElement();
        }

        _mayBeLeaf = true;
        // Ok: in array context we need to skip reporting field names.
        // But what's the best way to find next token?
        return (_currToken = JsonToken.FIELD_NAME);
    }

    // Ok; beyond start element, what do we get?
    switch (token) {
        case XmlTokenStream.XML_END_ELEMENT:
            // Simple, except that if this is a leaf, need to suppress end:
            if (_mayBeLeaf) {
                _mayBeLeaf = false;
                // 06-Jan-2015, tatu: as per [dataformat-xml#180], need to
                //    expose as empty Object, not null
                return (_currToken = JsonToken.VALUE_NULL);
            }
            _currToken = _parsingContext.inArray() ? JsonToken.END_ARRAY : JsonToken.END_OBJECT;
            _parsingContext = _parsingContext.getParent();
            _namesToWrap = _parsingContext.getNamesToWrap();
            return _currToken;

        case XmlTokenStream.XML_ATTRIBUTE_NAME:
            // If there was a chance of leaf node, no more...
            if (_mayBeLeaf) {
                _mayBeLeaf = false;
                _nextToken = JsonToken.FIELD_NAME;
                _currText = _xmlTokens.getText();
                _parsingContext = _parsingContext.createChildObjectContext(-1, -1);
                return (_currToken = JsonToken.START_OBJECT);
            }
            _parsingContext.setCurrentName(_xmlTokens.getLocalName());
            return (_currToken = JsonToken.FIELD_NAME);
        case XmlTokenStream.XML_ATTRIBUTE_VALUE:
            _currText = _xmlTokens.getText();
            return (_currToken = JsonToken.VALUE_STRING);
        case XmlTokenStream.XML_TEXT:
            _currText = _xmlTokens.getText();
            if (_mayBeLeaf) {
                _mayBeLeaf = false;
                /* One more refinement (pronunced like "hack") is that if
                 * we had an empty String (or all white space), and we are
                 * deserializing an array, we better hide the empty text.
                 */
                // Also: must skip following END_ELEMENT
                _xmlTokens.skipEndElement();
                if (_parsingContext.inArray()) {
                    if (_isEmpty(_currText)) {
                        // 06-Jan-2015, tatu: as per [dataformat-xml#180], need to
                        //    expose as empty Object, not null (or, worse, as used to
                        //    be done, by swallowing the token)
                        _currToken = JsonToken.END_ARRAY;
                        _parsingContext = _parsingContext.getParent();
                        _namesToWrap = _parsingContext.getNamesToWrap();
                        return _currToken;
                    }
                }
                return (_currToken = JsonToken.VALUE_STRING);
            } else {
                // [dataformat-xml#177]: empty text may also need to be skipped
                if (_parsingContext.inObject()
                        && (_currToken != JsonToken.FIELD_NAME) && _isEmpty(_currText)) {
                    _currToken = JsonToken.END_OBJECT;
                    _parsingContext = _parsingContext.getParent();
                    _namesToWrap = _parsingContext.getNamesToWrap();
                    return _currToken;
                }
            }
            // If not a leaf (or otherwise ignorable), need to transform into property...
            _parsingContext.setCurrentName(_cfgNameForTextElement);
            _nextToken = JsonToken.VALUE_STRING;
            return (_currToken = JsonToken.FIELD_NAME);
        case XmlTokenStream.XML_END:
            return (_currToken = null);
    }

    // should never get here
    _throwInternal();
    return null;
}

public void addVirtualWrapping(Set<String> namesToWrap) {
    /* 17-Sep-2012, tatu: Not 100% sure why, but this is necessary to avoid
     *   problems with Lists-in-Lists properties
     */
    String name = _xmlTokens.getLocalName();
    if (name != null && namesToWrap.contains(name)) {
        _xmlTokens.repeatStartElement();
    }
    _namesToWrap = namesToWrap;
    _parsingContext.setNamesToWrap(namesToWrap);
}