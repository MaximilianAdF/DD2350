def WordHash(word: str) -> int:
    word = word[:3].lower()
    hash_value = 0
    base = 30  # Since we have 29 characters including å, ä, ö

    for i, char in enumerate(word):
        if 'a' <= char <= 'z':  # Map a-z
            hash_value+= (base**i)*(ord(char)-ord('a')+1)
        elif char == 'å':  # Map å to 27
            hash_value+= (base**i)*(27)
        elif char == 'ä':  # Map ä to 28
            hash_value+= (base**i)*(28)
        elif char == 'ö':  # Map ö to 29
            hash_value+= (base**i)*(29)
        else:
            raise ValueError(f"Unsupported character: {char}")

    return hash_value -1



def SortWords():
    prevtell = 0
    tell =0
    array = ["-1"] * 27000
    file1 = open('lab1/rawindex.txt', 'r')
    prevline = file1.readline().replace("\n","").split(" ")

    threeLetter=prevline[0][:3] #abc av ABCD

    array[WordHash(threeLetter)]= str(tell)
    print(array[WordHash(threeLetter)])

    while True:

        # Get next line from file
        tell += len(prevline[0]) + len(prevline[1])
        line = file1.readline().replace("\n","").split(" ")
        #check if its the end of file
        if (line[0] == ""):
            file2 = open('lab1/ListOfIndex.txt', 'w')
            for item in array:
        # Write the item followed by a newline character
                file2.write(str(item).ljust(64) + '\n')
            file2.close()
            file1.close()
            #end the program
            return

        #check if the same word
        if (threeLetter != line[0][:3]):
            array[WordHash(threeLetter)]= str(prevtell)
            prevtell+= tell
            threeLetter = line[0][:3]
        else:
            prevline = line

print (WordHash("a"))

SortWords()

