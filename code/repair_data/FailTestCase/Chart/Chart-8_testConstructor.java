    public void testConstructor() {
        Locale savedLocale = Locale.getDefault();
        TimeZone savedZone = TimeZone.getDefault();
        Locale.setDefault(new Locale("da", "DK"));
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Copenhagen"));
        GregorianCalendar cal = (GregorianCalendar) Calendar.getInstance(
                TimeZone.getDefault(), Locale.getDefault());

        // first day of week is monday
        assertEquals(Calendar.MONDAY, cal.getFirstDayOfWeek());
        cal.set(2007, Calendar.AUGUST, 26, 1, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date t = cal.getTime();
        Week w = new Week(t, TimeZone.getTimeZone("Europe/Copenhagen"));
        assertEquals(34, w.getWeek());

        Locale.setDefault(Locale.US);
        TimeZone.setDefault(TimeZone.getTimeZone("US/Detroit"));
        cal = (GregorianCalendar) Calendar.getInstance(TimeZone.getDefault());
        // first day of week is Sunday
        assertEquals(Calendar.SUNDAY, cal.getFirstDayOfWeek());
        cal.set(2007, Calendar.AUGUST, 26, 1, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);

        t = cal.getTime();
        w = new Week(t, TimeZone.getTimeZone("Europe/Copenhagen"));
        assertEquals(35, w.getWeek());
        w = new Week(t, TimeZone.getTimeZone("Europe/Copenhagen"),
                new Locale("da", "DK"));
        assertEquals(34, w.getWeek());

        Locale.setDefault(savedLocale);
        TimeZone.setDefault(savedZone);
    }