private boolean testValidProtocol(Element el, Attribute attr, Set<Protocol> protocols) {
    String value = el.absUrl(attr.getKey());
    
    if (!preserveRelativeLinks) {
        attr.setValue(value);
    }
    
    for (Protocol protocol : protocols) {
        String prot = protocol.toString() + ":";
        if (value.toLowerCase().startsWith(prot) || prot.equals("cid") || prot.equals("data")) {
            return true;
        }
    }
    
    return false;
}