public Object generateId(Object forPojo) {
    if (forPojo == null) {
        return null; // or any other appropriate value
    }

    // 04-Jun-2016, tatu: As per [databind#1255], need to consider possibility of
    // id being generated for "alwaysAsId", but not being written as POJO; regardless,
    // need to use existing id if there is one:
    if (generator != null) {
        id = generator.generateId(forPojo);
        return id;
    } else {
        return null; // or any other appropriate value
    }
}