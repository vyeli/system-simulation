import sys
import numpy as np

# Usage example: py generateInput.py 100 20

N = int(sys.argv[1])
L = int(sys.argv[2])

file1 = open("static.txt", "w")
file1.write(str(N) + '\n' + str(L) + '\n');

file2 = open("dynamic.txt", 'w')


for i in range(N):
    file1.write(str(0.25) + '\n')
    file2.write(str(i) + " " + str(np.random.uniform(0, L)) + ' ' + str(np.random.uniform(0, L)) + '\n')


