import sys
import random

# Usage example: py generateInput.py 100 20

N = int(sys.argv[1])
L = int(sys.argv[2])

file = open("static.txt", "w")
file.write(str(N) + '\n' + str(L) + '\n');

for i in range(N):
    file.write(str(random.uniform(0, 1)) + '\n')
