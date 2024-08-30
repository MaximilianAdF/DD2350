

# Using readline()






def SortWords():

    indexList = []
    file1 = open('lab1/rawindex.txt', 'r')
    file2 = open('lab1/ListOfIndex.txt', 'w')
    prevtell = file1.tell()
    prevline = file1.readline().replace("\n","").split(" ")

    print("prevline:", prevline)
    print(prevtell)
    if (len(prevline[0])>=3):
        threeLetter=prevline[0][:3] #abc av ABCD
    else:
        threeLetter=prevline[0]
    indexList.append(prevtell)

    while True:

        # Get next line from file
        tell = file1.tell()
        line = file1.readline().replace("\n","").split(" ")


        #check if its the end of file
        if (line[0] == ""):
            file2.write(threeLetter + " " + ",".join(str(x) for x in indexList) + "\n")

            file2.close()
            file1.close()
            #end the program
            break

            


        #check if the same word
        if (prevline[0] != line[0]):
            if(threeLetter == line[0][:3]):
                indexList.append(tell)
                #prevtell=file1.tell()
                prevline = line
            else:
                file2.write(threeLetter + " " + ",".join(str(x) for x in indexList) + "\n")
                indexList.clear()
                indexList.append(tell)
                #prevtell=file1.tell()
                threeLetter = line[0][:3]
                prevline = line
        else:
            prevline = line


def FindWordIndexes(word): ## gör bättre
    word = word[:3]
    file1 = open('lab1/ListOfIndex.txt', 'r')
    while True:
        line = file1.readline().replace("\n","").split(" ")
        if (line[0][:3] == word):
            file1.close()
            return line[1].split()
    return


def GetWordsFromText(word,indexes): ## not done
    Requestext= []


    file1 = open('lab1/ListOfIndex.txt', 'r')
    file2 = open('lab1/korpus.txt', 'r')
    file3 = open('lab1/rawindex.txt', 'r')

    for i in range(len(indexes)): ## hitta rätt index
        file3.seek(int(indexes[i]))

        line = file3.readline().replace("\n","").split(" ")
        if (line[0]== word):
            break

    print(line)

    while line[0]==word:# ta ut rätt saker från korpus
        file2.seek(int(line[1])-30)
        Requestext.append(file2.read(60+len(word)).replace("\n"," "))## fixa EDGE CASES
        line = file3.readline().replace("\n","").split(" ")
    return Requestext

SortWords()

indexes = FindWordIndexes("och")
row = GetWordsFromText("och",indexes)
print(row)