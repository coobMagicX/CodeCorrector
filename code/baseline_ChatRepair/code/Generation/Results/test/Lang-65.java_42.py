private static void modify(Calendar val, int field, boolean round) {
    if (val.get(Calendar.YEAR) > 280000000) {
        throw new ArithmeticException("Calendar value too large for accurate calculations");
    }

    // truncating milliseconds, seconds, and minutes
    if (field >= Calendar.HOUR) {
      val.set(Calendar.MILLISECOND, 0);
    }
    if (field >= Calendar.MINUTE) {
      val.set(Calendar.SECOND, 0);
    }
    if (field >= Calendar.HOUR) {
      val.set(Calendar.MINUTE, 0);
    }
    
    boolean roundUp = false;
    final int[][] fields = {
        {Calendar.MILLISECOND},
        {Calendar.SECOND},
        {Calendar.MINUTE},
        {Calendar.HOUR_OF_DAY, Calendar.HOUR},
        {Calendar.DATE, Calendar.DAY_OF_MONTH, Calendar.AM_PM},
        {Calendar.MONTH, DateUtils.SEMI_MONTH},
        {Calendar.YEAR},
        {Calendar.ERA}
    };

    for (int[] fieldArray : fields) {
        for (int aField : fieldArray) {
            if (aField == field) {
                if (round && roundUp) {
                    val.add(aField, 1);
                }
                return;
            }
        }
    }
    
    throw new IllegalArgumentException("The field " + field + " is not supported");
}
