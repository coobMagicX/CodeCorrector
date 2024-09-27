    public void testClassInstantiationWithParameterBeingNullThrowsNullPointerExceptionTwo() {

        ChecksumCalculatingInputStream checksumCalculatingInputStream = new ChecksumCalculatingInputStream(null,new ByteArrayInputStream(new byte[1]));


    }