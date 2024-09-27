public boolean isCachable() {
    /* As per [databind#735] and the new test failure related to custom deserialization not being applied to InetAddress types.
     * The existence of value or key deserializer (only passed if annotated to use non-standard one) should prevent caching.
     * Also, custom deserializers for InetAddress types should be considered.
     * Updated logic: Include conditions to handle InetAddress deserialization.
     */
    return (_keyDeserializer == null) && (_valueDeserializer == null)
            && (_valueTypeDeserializer == null)
            && (_ignorableProperties == null)
            && (!_handledType.equals(InetAddress.class)); // Ensure caching doesn't apply to custom InetAddress deserializers.
}