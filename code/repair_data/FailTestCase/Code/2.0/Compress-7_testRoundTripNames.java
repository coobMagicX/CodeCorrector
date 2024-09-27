    public void testRoundTripNames(){
        checkName("");
        checkName("The quick brown fox\n");
        checkName("\177");
        // checkName("\0"); // does not work, because NUL is ignored
        // COMPRESS-114
        checkName("0302-0601-3±±±F06±W220±ZB±LALALA±±±±±±±±±±CAN±±DC±±±04±060302±MOE.model");
    }