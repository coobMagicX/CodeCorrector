    public void testTruncateLang59() throws Exception {

        // Set TimeZone to Mountain Time
        TimeZone MST_MDT = TimeZone.getTimeZone("MST7MDT");
        TimeZone.setDefault(MST_MDT);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z");
        format.setTimeZone(MST_MDT);

        Date oct31_01MDT = new Date(1099206000000L); 

        Date oct31MDT             = new Date(oct31_01MDT.getTime()       - 3600000L); // - 1 hour
        Date oct31_01_02MDT       = new Date(oct31_01MDT.getTime()       + 120000L);  // + 2 minutes
        Date oct31_01_02_03MDT    = new Date(oct31_01_02MDT.getTime()    + 3000L);    // + 3 seconds
        Date oct31_01_02_03_04MDT = new Date(oct31_01_02_03MDT.getTime() + 4L);       // + 4 milliseconds

        assertEquals("Check 00:00:00.000", "2004-10-31 00:00:00.000 MDT", format.format(oct31MDT));
        assertEquals("Check 01:00:00.000", "2004-10-31 01:00:00.000 MDT", format.format(oct31_01MDT));
        assertEquals("Check 01:02:00.000", "2004-10-31 01:02:00.000 MDT", format.format(oct31_01_02MDT));
        assertEquals("Check 01:02:03.000", "2004-10-31 01:02:03.000 MDT", format.format(oct31_01_02_03MDT));
        assertEquals("Check 01:02:03.004", "2004-10-31 01:02:03.004 MDT", format.format(oct31_01_02_03_04MDT));

        // ------- Demonstrate Problem -------
        Calendar gval = Calendar.getInstance();
        gval.setTime(new Date(oct31_01MDT.getTime()));
        gval.set(Calendar.MINUTE, gval.get(Calendar.MINUTE)); // set minutes to the same value
        assertEquals("Demonstrate Problem", gval.getTime().getTime(), oct31_01MDT.getTime() + 3600000L);

        // ---------- Test Truncate ----------
        assertEquals("Truncate Calendar.MILLISECOND",
                oct31_01_02_03_04MDT, DateUtils.truncate(oct31_01_02_03_04MDT, Calendar.MILLISECOND));

        assertEquals("Truncate Calendar.SECOND",
                   oct31_01_02_03MDT, DateUtils.truncate(oct31_01_02_03_04MDT, Calendar.SECOND));

        assertEquals("Truncate Calendar.MINUTE",
                      oct31_01_02MDT, DateUtils.truncate(oct31_01_02_03_04MDT, Calendar.MINUTE));

        assertEquals("Truncate Calendar.HOUR_OF_DAY",
                         oct31_01MDT, DateUtils.truncate(oct31_01_02_03_04MDT, Calendar.HOUR_OF_DAY));

        assertEquals("Truncate Calendar.HOUR",
                         oct31_01MDT, DateUtils.truncate(oct31_01_02_03_04MDT, Calendar.HOUR));

        assertEquals("Truncate Calendar.DATE",
                            oct31MDT, DateUtils.truncate(oct31_01_02_03_04MDT, Calendar.DATE));


        // ---------- Test Round (down) ----------
        assertEquals("Round Calendar.MILLISECOND",
                oct31_01_02_03_04MDT, DateUtils.round(oct31_01_02_03_04MDT, Calendar.MILLISECOND));

        assertEquals("Round Calendar.SECOND",
                   oct31_01_02_03MDT, DateUtils.round(oct31_01_02_03_04MDT, Calendar.SECOND));

        assertEquals("Round Calendar.MINUTE",
                      oct31_01_02MDT, DateUtils.round(oct31_01_02_03_04MDT, Calendar.MINUTE));

        assertEquals("Round Calendar.HOUR_OF_DAY",
                         oct31_01MDT, DateUtils.round(oct31_01_02_03_04MDT, Calendar.HOUR_OF_DAY));

        assertEquals("Round Calendar.HOUR",
                         oct31_01MDT, DateUtils.round(oct31_01_02_03_04MDT, Calendar.HOUR));

        assertEquals("Round Calendar.DATE",
                            oct31MDT, DateUtils.round(oct31_01_02_03_04MDT, Calendar.DATE));

        // restore default time zone
        TimeZone.setDefault(defaultZone);
    }