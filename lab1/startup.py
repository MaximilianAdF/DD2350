

# Using readline()






def SortWords():

    indexList = []
    file1 = open('rawindex.txt', 'r')
    file2 = open('ListOfIndex.txt', 'w')
    prevline = file1.readline().replace("\n","").split(" ")
    if (len(prevline[0])>=3):
        threeLetter=prevline[0][:3] #abc av ABCD
    else:
        threeLetter=prevline[0]
    indexList.append(file1.tell())
    while True:

        # Get next line from file
        line = file1.readline().replace("\n","").split(" ")


        #check if its the end of file
        if (line[0] == ""):
            file2.close()
            file1.close()
            #end the program
            break

            


        #check if the same word
        if (prevline[0] != line[0]):
            if(threeLetter == line[0][:3]):
                indexList.append(file1.tell())
                prevline = line
            else:
                file2.write(threeLetter + " " + ",".join(str(x) for x in indexList) + "\n")
                indexList.clear()
                indexList.append(file1.tell())
                threeLetter = line[0][:3]
                prevline = line
        else:
            prevline = line


def FindWordIndexes(word): ## gör bättre
    word = word[:3]
    file1 = open('ListOfIndex.txt', 'r')
    while True:
        line = file1.readline().replace("\n","").split(" ")
        if (line[0][:3] == word):
            file1.close()
            return line[1]
    return


def GetWordsFromText(word,indexes): ## not done
    Requestext= []

    file1 = open('ListOfIndex.txt', 'r')

    for i in range(len(indexes)):
        file1.seek(indexes[i])
        line = file1.readline().replace("\n","").split(" ")
        if (line[0]== word):
            break
    while line[0]==word:
        Requestext.append




    return

SortWords()

indexes = FindWordIndexes("abcd")

print(indexes)