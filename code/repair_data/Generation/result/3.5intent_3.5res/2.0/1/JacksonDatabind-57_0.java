public <T> MappingIterator<T> readValues(byte[] src, int offset, int length) throws IOException, JsonProcessingException {
    if (_dataFormatReaders != null) {
        return _detectBindAndReadValues(_dataFormatReaders.findFormat(src, offset, length), false);
    }
    return _bindAndReadValues(_considerFilter(_parserFactory.createParser(src), true));
}

private <T> MappingIterator<T> _detectBindAndReadValues(DataFormatReaders.Match match, boolean forceWrap) throws IOException, JsonProcessingException {
    if (match == null) {
        throw new JsonParseException("No data format matches content of file", JsonLocation.NA);
    }
    return _bindAndReadValues(match.createParser(_context, forceWrap));
}

private <T> MappingIterator<T> _bindAndReadValues(JsonParser p, ObjectReader reader) throws IOException, JsonProcessingException {
    if (p.getCodec() == null) {
        p.setCodec(_objectMapper);
    }
    return reader.readValues(p);
}

private <T> MappingIterator<T> _bindAndReadValues(JsonParser p, boolean forceWrap) throws IOException, JsonProcessingException {
    if (p.getCodec() == null) {
        p.setCodec(_objectMapper);
    }
    if (forceWrap) {
        p = _objectMapper.getFactory().createParser(new JsonParserSequence(p, true));
    }
    return _objectMapper.readValues(p);
}

private JsonParser _considerFilter(JsonParser p, boolean filterNeeded) {
    if (_filter == null || !filterNeeded) {
        return p;
    }
    return new FilteringParserDelegate(p, _filter, false, false);
}