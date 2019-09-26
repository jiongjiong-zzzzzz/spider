lines = []
count = 0
with open('new_3.txt', 'r') as file_to_read:
    while True:
        line = file_to_read.readline()
        if not line:
            break
        line = line.strip('\n')
        line = line.split(':')
        list_1 = line[1].split(',')
        a = list(filter(None, list_1))  # 只能过滤空字符和None
        print(line[0],end=","),print(a)
        for num in a:
                count = count +int(num)
        print(count)
print(count)