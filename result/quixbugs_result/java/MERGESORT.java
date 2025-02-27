public static ArrayList<Integer> mergesort(ArrayList<Integer> arr) {
    if (arr.size() <= 1) { // Modified base case condition to handle single-element or empty lists
        return arr;
    } else {
        int middle = arr.size() / 2;
        ArrayList<Integer> left = new ArrayList<Integer>(100);
        left.addAll(arr.subList(0,middle));
        left = mergesort(left);
        ArrayList<Integer> right = new ArrayList<Integer>(100);
        right.addAll(arr.subList(middle, arr.size()));
        right = mergesort(right);

        return merge(left, right);
    }
}