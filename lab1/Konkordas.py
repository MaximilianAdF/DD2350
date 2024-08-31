

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


    file1 = open('lab1/rawindex.txt', 'r')
    file2 = open('lab1/korpus.txt', 'r')

    for i in range(len(indexes)): ## hitta rätt index
        file1.seek(int(indexes[i]))
        line = file1.readline().replace("\n","").split(" ")
        if (line[0]== word):
            break

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

    indexes = FindWordIndexes(word)

    rows =GetWordsFromText(word,indexes)
    print(rows)

    if (len(rows)>25):
        awnser = input("Ther are more than 25 ocurences print? Y/N: ").lower()
        print("The word "+ word + " ocurrs "+ len(rows)+ " times.")
        if(awnser=="y"):
            PrintRows(rows)
    else:
        print("The word "+ word + " ocurrs " + str(len(rows)) + " times.")
        PrintRows(rows)





Konkordans()


