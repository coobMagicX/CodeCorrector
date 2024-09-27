public void escape(Writer writer, String str) throws IOException {
    int len = str.length();
    for (int i = 0; i < len; i++) {
        char c = str.charAt(i);
        String entityName = this.entityName(c);
        if (entityName == null) {
            if (c > 0x7F) {
                writer.write("&#");
                writer.write(Integer.toString(c, 10));
                writer.write(';');
            } else {
                writer.write(Character.toString(c)); // Use Character.toString() to write the character
            }
        } else {
            writer.write('&');
            writer.write(entityName);
            writer.write(';');
        }
    }
}

public String entityName(char c) {
    String entityName = null;
    if (c < 128) {
        entityName = BASIC_ARRAY[c];
    }
    if (entityName == null && c <= 255) {
        entityName = ISO8859_1_ARRAY[c - 128];
    }
    if (entityName == null) {
        entityName = HTML40_ARRAY[c];
    }
    return entityName;
}

static final String[] BASIC_ARRAY = new String[128];
static final String[] ISO8859_1_ARRAY = new String[128];
static final String[] HTML40_ARRAY = new String[256];

static {
    fillWithHtml40Entities(new Entities());

    for (int i = 0; i < 128; i++) {
        BASIC_ARRAY[i] = null;
        ISO8859_1_ARRAY[i] = null;
    }
}

static void fillWithHtml40Entities(Entities entities) {
    entities.addEntities(BASIC_ARRAY);
    entities.addEntities(ISO8859_1_ARRAY);
    entities.addEntities(HTML40_ARRAY);
}