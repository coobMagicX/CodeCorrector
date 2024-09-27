    public void test_getOffsetFromLocal_Moscow_Autumn() {
        doTest_getOffsetFromLocal(10, 28, 0, 0, "2007-10-28T00:00:00.000+04:00", ZONE_MOSCOW);
        doTest_getOffsetFromLocal(10, 28, 0,30, "2007-10-28T00:30:00.000+04:00", ZONE_MOSCOW);
        doTest_getOffsetFromLocal(10, 28, 1, 0, "2007-10-28T01:00:00.000+04:00", ZONE_MOSCOW);
        doTest_getOffsetFromLocal(10, 28, 1,30, "2007-10-28T01:30:00.000+04:00", ZONE_MOSCOW);
        
        doTest_getOffsetFromLocal(10, 28, 2, 0, "2007-10-28T02:00:00.000+04:00", ZONE_MOSCOW);
        doTest_getOffsetFromLocal(10, 28, 2,30, "2007-10-28T02:30:00.000+04:00", ZONE_MOSCOW);
        doTest_getOffsetFromLocal(10, 28, 2,30,59,999, "2007-10-28T02:30:59.999+04:00", ZONE_MOSCOW);
        doTest_getOffsetFromLocal(10, 28, 2,59,59,998, "2007-10-28T02:59:59.998+04:00", ZONE_MOSCOW);
        doTest_getOffsetFromLocal(10, 28, 2,59,59,999, "2007-10-28T02:59:59.999+04:00", ZONE_MOSCOW);
        
        doTest_getOffsetFromLocal(10, 28, 3, 0, "2007-10-28T03:00:00.000+03:00", ZONE_MOSCOW);
        doTest_getOffsetFromLocal(10, 28, 3,30, "2007-10-28T03:30:00.000+03:00", ZONE_MOSCOW);
        doTest_getOffsetFromLocal(10, 28, 4, 0, "2007-10-28T04:00:00.000+03:00", ZONE_MOSCOW);
        doTest_getOffsetFromLocal(10, 28, 5, 0, "2007-10-28T05:00:00.000+03:00", ZONE_MOSCOW);
        doTest_getOffsetFromLocal(10, 28, 6, 0, "2007-10-28T06:00:00.000+03:00", ZONE_MOSCOW);
        doTest_getOffsetFromLocal(10, 28, 7, 0, "2007-10-28T07:00:00.000+03:00", ZONE_MOSCOW);
        doTest_getOffsetFromLocal(10, 28, 8, 0, "2007-10-28T08:00:00.000+03:00", ZONE_MOSCOW);
    }