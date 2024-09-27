public <T> MappingIterator<T> readValues(byte[] src, int offset, int length)
throws IOException, JsonProcessingException
{
    if (_dataFormatReaders != null) {
        DataFormatReaders.Match match = _dataFormatReaders.findFormat(src, offset, length);
        if (!match.hasMatch()) {
            throw new JsonParseException(null, "No matching data format found");
        }
        return _detectBindAndReadValues(match, true);
    }
    JsonParser p = _parserFactory.createParser(src, offset, length);
    JsonParser filteredParser = _considerFilter(p, true);
    if (!filteredParser.hasCurrentToken()) {
        filteredParser.nextToken();
    }
    return _bindAndReadValues(filteredParser);
}