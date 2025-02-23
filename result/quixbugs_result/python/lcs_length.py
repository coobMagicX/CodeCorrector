def lcs_length(s, t):
    from collections import Counter
    dp = Counter()

    # Initialize dp table
    for i in range(len(s)+1):
        dp[(i, 0)] = 0
    for j in range(len(t)+1):
        dp[(0, j)] = 0

    for i in range(1, len(s)+1):
        for j in range(1, len(t)+1):
            if s[i-1] == t[j-1]:
                dp[(i, j)] = dp[(i-1, j-1)] + 1
            else:
                dp[(i, j)] = max(dp[(i-1, j)], dp[(i, j-1)])

    return dp[(len(s), len(t))]