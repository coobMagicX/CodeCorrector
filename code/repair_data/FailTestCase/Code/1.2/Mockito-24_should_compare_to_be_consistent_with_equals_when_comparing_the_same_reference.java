    public void should_compare_to_be_consistent_with_equals_when_comparing_the_same_reference() {
        //given
        Date today    = mock(Date.class);

        //when
        Set<Date> set = new TreeSet<Date>();
        set.add(today);
        set.add(today);

        //then
        assertEquals(1, set.size());
    }