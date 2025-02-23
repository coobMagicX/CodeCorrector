def max_sublist_sum(arr):
    max_ending_here = 0
    max_so_far = float('-inf')  # Initialize max_so_far to negative infinity

    for x in arr:
        max_ending_here = max_ending_here + x
        max_so_far = max(max_so_far, max_ending_here)
        
        # Adjust max_ending_here to zero if it drops below zero
        if max_ending_here < 0:
            max_ending_here = 0

    return max_so_far