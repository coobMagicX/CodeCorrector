public UTF8StreamJsonParser(IOContext ctxt, int features, InputStream in,
    ObjectCodec codec, BytesToNameCanonicalizer sym,
    byte[] inputBuffer, int start, int end,
    boolean bufferRecyclable)
{
    super(ctxt, features);
    _inputStream = in;
    _objectCodec = codec;
    _symbols = sym;
    _inputBuffer = inputBuffer;
    _inputPtr = start;
    _inputEnd = end;
    _bufferRecyclable = bufferRecyclable;
    // Adjusting the initial position to handle the offset correctly
    _byteOffset = start;
}