def WordHash(word: str) -> int:
    hash_value = 0
    base = 29  # Since we have 29 characters including å, ä, ö

    for i, char in enumerate(word):
        if 'a' <= char <= 'z':  # Map a-z
            char_value = ord(char) - ord('a') +1
        elif char == 'å':  # Map å to 26
            char_value = 27
        elif char == 'ä':  # Map ä to 27
            char_value = 28
        elif char == 'ö':  # Map ö to 28
            char_value = 29
        else:
            raise ValueError(f"Unsupported character: {char}")

        # Calculate the hash, considering the character position
        hash_value +=  char_value *(base**(len(word)-i -1))

    return hash_value -1

def GetIndex(word):
    hash = WordHash(word)
    file1 = open('lab1/ListOfIndex.txt', 'r')
    file1.seek(66*hash)
    text = file1.readline().replace("\n","")
    print(len(text))
    print("this is text : " + text)
    file1.close()
    return int(text)

def GetWordsFromText(word,index): ## Tar ut 30+ ord + 30 från main texten
    Requestext= []


    file1 = open('lab1/rawindex.txt', 'r')
    file2 = open('lab1/korpus.txt', 'r')


    file1.seek(index)

    line = file1.readline().replace("\n","").split(" ")
    while line[0] != word:
        line = file1.readline().replace("\n","").split(" ")

    while line[0]==word:# ta ut rätt saker från korpus
        if (int(line[1])-30 < 0): #ett av de första orden
            file2.seek(0)
            Requestext.append(file2.read(30+len(word)+int(line[1])).replace("\n"," "))
            line = file1.readline().replace("\n","").split(" ")
        else:
            file2.seek(int(line[1])-30)
            Requestext.append(file2.read(60+len(word)).replace("\n"," "))
            line = file1.readline().replace("\n","").split(" ")
    file1.close()
    file2.close()
    return Requestext

def PrintRows(rows:list):
    for i in range(len(rows)):
        print(rows[i])


def Konkordans():
    word = input("Type word to serch: ").lower()

    index = GetIndex(word)

    if(index==-1): #checks if word exists
        print("Dose not exist")
        return

    rows = GetWordsFromText(word,index)

    if (len(rows)>25): # check number of occurences
        awnser = input("Ther are more than 25 ocurences print? Y/N: ").lower()
        print("The word "+ word + " occurs "+ str(len(rows)) + " times.")
        if(awnser=="y"):
            PrintRows(rows)
    else:
        print("The word "+ word + " occurs " + str(len(rows)) + " times.")
        PrintRows(rows)





Konkordans()


