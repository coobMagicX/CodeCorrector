def kth(arr, k):
    pivot = arr[0]
    below = [x for x in arr if (x < pivot)]
    above = [x for x in arr if (x > pivot)]
    num_less = len(below)
    num_lessoreq = len(arr) - num_less  # Fix: subtract num_less from the length of arr
    if k < num_less:
        return kth(below, k)
    elif k >= num_lessoreq:
        return kth(above, k - num_lessoreq)  # Fix: subtract num_lessoreq from k
    else:
        return pivot