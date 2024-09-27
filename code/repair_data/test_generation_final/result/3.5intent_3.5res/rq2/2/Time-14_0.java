public int[] add(ReadablePartial partial, int fieldIndex, int[] values, int valueToAdd) {
    // overridden as superclass algorithm can't handle
    // 2004-02-29 + 48 months -> 2008-02-29 type dates
    if (valueToAdd == 0) {
        return values;
    }
    
    // month is largest field and being added to, such as month-day
    if (DateTimeUtils.isContiguous(partial)) {
        long instant = 0L;
        for (int i = 0, isize = partial.size(); i < isize; i++) {
            instant = partial.getFieldType(i).getField(iChronology).set(instant, values[i]);
        }
        instant = add(instant, valueToAdd);
        return iChronology.get(partial, instant);
    } else {
        return super.add(partial, fieldIndex, values, valueToAdd);
    }
}

public long add(long instant, int valueToAdd) {
    return FieldUtils.safeAdd(instant, valueToAdd);
}

public int[] get(ReadablePartial partial, long instant) {
    int size = partial.size();
    int[] values = new int[size];
    for (int i = 0; i < size; i++) {
        values[i] = partial.getFieldType(i).getField(iChronology).get(instant);
    }
    return values;
}

public int[] set(ReadablePartial partial, int fieldIndex, int[] values, int newValue) {
    int size = partial.size();
    int[] newValues = new int[size];
    System.arraycopy(values, 0, newValues, 0, size);
    newValues[fieldIndex] = newValue;
    return newValues;
}

public long roundFloor(long instant) {
    return FieldUtils.getMillisOfSecond(instant);
}

public boolean isContiguous(ReadablePartial partial) {
    int size = partial.size();
    if (size <= 0) {
        return true;
    }
    DateTimeFieldType lastType = partial.getFieldType(size - 1);
    DurationField lastField = lastType.getField(iChronology);
    for (int i = 0; i < size; i++) {
        DateTimeFieldType type = partial.getFieldType(i);
        if (type.getField(iChronology).getRangeDurationField().getType() != lastField.getType()) {
            return false;
        }
        lastField = type.getField(iChronology);
    }
    return true;
}

public long getMillis(int[] values) {
    long instant = 0L;
    for (int i = 0, isize = values.length; i < isize; i++) {
        instant = partial.getFieldType(i).getField(iChronology).set(instant, values[i]);
    }
    return instant;
}

public long getInstant(ReadablePartial partial) {
    return partial.getChronology().set(partial, 0L);
}

public int[] add(ReadablePartial partial, int fieldIndex, int[] values, int valueToAdd) {
    int[] newValues = set(partial, fieldIndex, values, add(getInstant(partial), valueToAdd));
    return newValues;
}

public int[] add(ReadablePartial partial, int[] values, int valueToAdd) {
    int size = partial.size();
    if (size == 0) {
        return values;
    }
    int[] newValues = values.clone();
    long instant = getMillis(newValues);
    for (int i = 0; i < size; i++) {
        DateTimeFieldType type = partial.getFieldType(i);
        int[] result = type.getField(iChronology).add(partial, i, newValues, valueToAdd);
        instant = type.getField(iChronology).set(instant, result[i]);
    }
    return iChronology.get(partial, instant);
}

public int[] addWrapPartial(ReadablePartial partial, int fieldIndex, int[] values, int valueToAdd) {
    int current = values[fieldIndex];
    int wrapped = FieldUtils.getWrappedValue(current, valueToAdd, getMinimumValue(partial), getMaximumValue(partial));
    return set(partial, fieldIndex, values, wrapped);
}

public int[] addWrapField(ReadablePartial partial, int fieldIndex, int[] values, int valueToAdd) {
    int current = values[fieldIndex];
    int wrapped = FieldUtils.getWrappedValue(current, valueToAdd, getMinimumValue(partial), getMaximumValue(partial));
    return set(partial, fieldIndex, values, wrapped);
}

public int[] add(ReadablePartial partial, int fieldIndex, int[] values, int valueToAdd) {
    int size = partial.size();
    if (size == 0) {
        return values;
    }
    if (size == 1) {
        return addWrapField(partial, fieldIndex, values, valueToAdd);
    }
    if (size <= fieldIndex + 1 || partial.getFieldType(fieldIndex + 1).getRangeDurationField() == null) {
        return addWrapPartial(partial, fieldIndex, values, valueToAdd);
    }
    long instant = 0L;
    for (int i = 0; i < size; i++) {
        instant = partial.getFieldType(i).getField(iChronology).set(instant, values[i]);
    }
    instant = add(instant, fieldIndex, values, valueToAdd);
    return iChronology.get(partial, instant);
}

public long add(long instant, int fieldIndex, int[] values, int valueToAdd) {
    DateTimeField field = partial.getFieldType(fieldIndex).getField(iChronology);
    long localInstant = 0L;
    for (int i = 0; i < values.length; i++) {
        localInstant = field.set(localInstant, values[i]);
    }
    localInstant = field.add(localInstant, valueToAdd);
    return set(instant, fieldIndex, values, localInstant);
}

public long set(long instant, int fieldIndex, int[] values, long value) {
    DateTimeField field = partial.getFieldType(fieldIndex).getField(iChronology);
    for (int i = 0; i < values.length; i++) {
        instant = field.set(instant, values[i]);
    }
    return field.set(instant, value);
}

public int getMinimumValue(ReadablePartial partial) {
    return partial.getChronology().year().getMinimumValue();
}

public int getMaximumValue(ReadablePartial partial) {
    return partial.getChronology().year().getMaximumValue();
}