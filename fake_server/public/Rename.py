import os,sys
temp = os.walk(sys.argv[1], topdown=False)
for root, dirs, files in temp:
    for i in dirs:
        dir = os.path.join(root,i)
        os.rename(dir, dir.replace(" ","-").lower())

