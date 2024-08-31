
def WordHash(word:str)->int:
    hash = 0
    for i in range(len(word)):
        hash+= (29**i)*(ord(word[i]) - 96)
       
    return hash
    



def binary_search_in_file( word):
    word = word[:3]
    file1 = open('lab1/ListOfIndex.txt', 'r')
    left = 0
    file1.seek(0, 2)  # Gå till slutet av filen
    right = file1.tell()


    while True:

        if right - left < 10000: #om det är mindre än 10000 chars mellan punkterna sök linjärt
            file1.seek(left)
            while left <= right:
                line = file1.readline().strip().split(" ")

                if not line or len(line[0]) == 0:
                    break

                if line[0] == word:
                    return line[1].split()

                left = file1.tell()

            break  # Avsluta binärsöknings-loopen eftersom vi har gått över till sekventiell sökning

        mid = (left + right) // 2
        file1.seek(mid)

        # Flytta till början av nästa rad om vi är mitt i en rad
        if mid > 0:
            file1.readline()

        line = file1.readline().replace("\n","").split(" ")

        if (WordHash(word) > WordHash(line[0])):# kollar om ordet är större än det i mitten
            left = mid
        else:
            right = mid

    return []  # Returnera en tom lista om ordet inte finns i filen


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

    indexes = binary_search_in_file(word)
    if(len(indexes)==0): #checks if word exists
        print("Dose not exist")
        return

    rows = GetWordsFromText(word,indexes)

    if (len(rows)>25): # check number of occurences
        awnser = input("Ther are more than 25 ocurences print? Y/N: ").lower()
        print("The word "+ word + " occurs "+ str(len(rows)) + " times.")
        if(awnser=="y"):
            PrintRows(rows)
    else:
        print("The word "+ word + " occurs " + str(len(rows)) + " times.")
        PrintRows(rows)





Konkordans()


