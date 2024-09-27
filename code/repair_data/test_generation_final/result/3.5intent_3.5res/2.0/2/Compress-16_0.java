public ArchiveInputStream createArchiveInputStream(final InputStream in)
throws ArchiveException {
    if (in == null) {
        throw new IllegalArgumentException("Stream must not be null.");
    }

    if (!in.markSupported()) {
        throw new IllegalArgumentException("Mark is not supported.");
    }

    final byte[] signature = new byte[12];
    in.mark(signature.length);
    try {
        int signatureLength = in.read(signature);
        in.reset();
        if (ZipArchiveInputStream.matches(signature, signatureLength)) {
            return new ZipArchiveInputStream(in);
        } else if (JarArchiveInputStream.matches(signature, signatureLength)) {
            return new JarArchiveInputStream(in);
        } else if (ArArchiveInputStream.matches(signature, signatureLength)) {
            return new ArArchiveInputStream(in);
        } else if (CpioArchiveInputStream.matches(signature, signatureLength)) {
            return new CpioArchiveInputStream(in);
        } else if (isDumpArchiveFile(in)) {
            return new DumpArchiveInputStream(in);
        } else if (isTarArchiveFile(in)) {
            return new TarArchiveInputStream(in);
        }
    } catch (IOException e) {
        throw new ArchiveException("Could not use reset and mark operations.", e);
    }

    throw new ArchiveException("No Archiver found for the stream signature");
}

private boolean isArchiveFile(InputStream in) throws IOException {
    final byte[] signature = new byte[12];
    in.mark(signature.length);
    int signatureLength = in.read(signature);
    in.reset();
    return (ZipArchiveInputStream.matches(signature, signatureLength)
            || JarArchiveInputStream.matches(signature, signatureLength)
            || ArArchiveInputStream.matches(signature, signatureLength)
            || CpioArchiveInputStream.matches(signature, signatureLength));
}

private boolean isDumpArchiveFile(InputStream in) throws IOException {
    final byte[] signature = new byte[32];
    in.mark(signature.length);
    int signatureLength = in.read(signature);
    in.reset();
    return DumpArchiveInputStream.matches(signature, signatureLength);
}

private boolean isTarArchiveFile(InputStream in) throws IOException {
    final byte[] signature = new byte[512];
    in.mark(signature.length);
    int signatureLength = in.read(signature);
    in.reset();
    return TarArchiveInputStream.matches(signature, signatureLength);
}