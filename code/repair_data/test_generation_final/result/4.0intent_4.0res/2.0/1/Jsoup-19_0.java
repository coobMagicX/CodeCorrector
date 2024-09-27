private boolean testValidProtocol(Element el, Attribute attr, Set<Protocol> protocols) {
    // Try to resolve relative URLs to absolute, and optionally update the attribute so output HTML has absolute URLs.
    // Relative links without a base URI get removed.
    String value = preserveRelativeLinks ? attr.getValue() : el.absUrl(attr.getKey());

    if (!preserveRelativeLinks) {
        attr.setValue(value);
    }

    // Convert the value to lowercase for case-insensitive comparison
    String valueLower = value.toLowerCase();

    for (Protocol protocol : protocols) {
        // Ensure the protocol is followed by ":", and compare in lowercase
        String prot = protocol.toString().toLowerCase() + ":";
        if (valueLower.startsWith(prot)) {
            return true;
        }
    }
    return false;
}