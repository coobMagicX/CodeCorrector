void read(Tokeniser t, CharacterReader r) {
    char c = r.consume();
    switch (c) {
        case '>':
            if (t.tagPending.selfClosing) {
                t.tagPending.attributes.append(c);
            } else {
                t.tagPending.selfClosing = true;
                t.emitTagPending();
                t.transition(Data);
            }
            break;
        case eof:
            t.eofError(this);
            t.transition(Data);
            break;
        default:
            t.error(this);
            t.transition(BeforeAttributeName);
    }
}