public Document clean(Document dirtyDocument) {
    Validate.notNull(dirtyDocument);

    Document clean = Document.createShell(dirtyDocument.baseUri());
    copySafeNodes(dirtyDocument.body(), clean.body());

    clean.body().select("frameset").unwrap(); // unwrap framesets from the clean document

    return clean;
}