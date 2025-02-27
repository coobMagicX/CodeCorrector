public static int binsearch(int[] arr, int x, int start, int end) {
    if (start == end) {
        return -1;
    }
    int mid = start + (end - start) / 2; // check this is floor division
    if (x < arr[mid]) {
        return binsearch(arr, x, start, mid);
    } else if (x > arr[mid]) {
        return binsearch(arr, x, mid + 1, end); // fix here
    } else {
        return mid;
    }
}