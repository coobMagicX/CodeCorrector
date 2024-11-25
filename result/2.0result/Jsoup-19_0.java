private boolean testValidProtocol(Element el, Attribute attr, Set<Protocol> protocols) {
    // try to resolve relative urls to abs, and optionally update the attribute so output html has abs.
    // rels without a baseuri get removed
    String value = el.absUrl(attr.getKey());
    if (value.isEmpty()) {
        value = attr.getValue(); // Use original attribute value if absUrl is empty
    }
    if (!preserveRelativeLinks)
        attr.setValue(value);

    String lowerValue = value.toLowerCase();
    for (Protocol protocol : protocols) {
        String prot = protocol.toString().toLowerCase() + ":";
        if (lowerValue.startsWith(prot)) {
            return true;
        }
    }
    return false;
}