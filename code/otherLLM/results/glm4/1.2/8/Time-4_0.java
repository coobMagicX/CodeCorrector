public class Partial {
    private Chronology iChronology;
    private DateTimeFieldType[] iTypes;
    private int[] iValues;

    // Assume these methods are already defined:
    public static Partial of(Chronology chronology, DateTimeFieldType[] types, int[] values) {
        // Constructor logic
    }

    public void validate(Chronology chronology, int[] values) throws IllegalArgumentException {
        if (chronology == null) {
            throw new IllegalArgumentException("Chronology must not be null");
        }
        // Additional validation logic for the array of values
    }

    public Partial with(DateTimeFieldType fieldType, int value) {
        if (fieldType == null) {
            throw new IllegalArgumentException("The field type must not be null");
        }
        int index = indexOf(fieldType);
        if (index == -1) {
            DateTimeFieldType[] newTypes = new DateTimeFieldType[iTypes.length + 1];
            int[] newValues = new int[newTypes.length];

            // Find correct insertion point to maintain largest-smallest order
            int i = 0;
            DurationField unitField = fieldType.getDurationType().getField(iChronology);
            if (unitField.isSupported()) {
                for (; i < iTypes.length; i++) {
                    DateTimeFieldType loopType = iTypes[i];
                    DurationField loopUnitField = loopType.getDurationType().getField(iChronology);
                    if (loopUnitField.isSupported()) {
                        int compare = unitField.compareTo(loopUnitField);
                        if (compare > 0) {
                            break;
                        } else if (compare == 0) {
                            DurationField rangeField = fieldType.getRangeDurationType().getField(iChronology);
                            DurationField loopRangeField = loopType.getRangeDurationType().getField(iChronology);
                            if (rangeField.compareTo(loopRangeField) > 0) {
                                break;
                            }
                        }
                    }
                }
            }
            System.arraycopy(iTypes, 0, newTypes, 0, i);
            System.arraycopy(iValues, 0, newValues, 0, i);
            newTypes[i] = fieldType;
            newValues[i] = value;
            System.arraycopy(iTypes, i, newTypes, i + 1, newTypes.length - i - 1);
            System.arraycopy(iValues, i, newValues, i + 1, newValues.length - i - 1);

            // Use public constructor to ensure full validation
            Partial newPartial = of(iChronology, newTypes, newValues);
            newPartial.validate(iChronology, newValues);
            return newPartial;
        }
        if (value == getValue(index)) {
            return this;
        }
        int[] newValues = iValues.clone(); // Clone to avoid modifying the original array
        newValues[index] = value;
        return of(this, newValues); // Assuming there is an appropriate constructor for Partial that takes a previous Partial and new values
    }

    private static int indexOf(DateTimeFieldType fieldType) {
        for (int i = 0; i < iTypes.length; i++) {
            if (iTypes[i] == fieldType) {
                return i;
            }
        }
        return -1;
    }

    private int getValue(int index) {
        // Assuming this method exists and retrieves the value from iValues
        return iValues[index];
    }

    public static Partial of(Partial previous, int[] newValues) {
        // Constructor logic based on previous partial and new values array
        return new Partial();
    }
}