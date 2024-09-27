// Trim all trailing NULs and spaces
int trimmedEnd = end;
while (start < trimmedEnd) {
    byte currentByte = buffer[trimmedEnd - 1];
    if (currentByte == 0 || currentByte == ' ') {
        trimmedEnd--;
    } else {
        break;
    }
}
if (start == trimmedEnd) {
    throw new IllegalArgumentException(
        exceptionMessage(buffer, offset, length, start, buffer[start]));
}

// Parse the octal value
for (; start < trimmedEnd; start++) {
    byte currentByte = buffer[start];
    if (currentByte < '0' || currentByte > '7') {
        throw new IllegalArgumentException(
            exceptionMessage(buffer, offset, length, start, currentByte));
    }
    result = (result << 3) + (currentByte - '0'); // convert from ASCII
}

return result;