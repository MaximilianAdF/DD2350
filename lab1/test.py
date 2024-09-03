#First 3 letter comb gives idx of where words with this letter comb starts but still need to search for the specific searchword in rawindex.txt

abc     (index i ursprungsfilen)  index 1
abc     (index i ursprungsfilen)
abc     (index i ursprungsfilen)
abcd    (index i ursprungsfilen)  index 2
abcd    (index i ursprungsfilen)
abcd    (index i ursprungsfilen)
abcd    (index i ursprungsfilen)
abce    (index i ursprungsfilen)  index 3
abce    (index i ursprungsfilen)
abce    (index i ursprungsfilen)


new3LetterFile #(file we create) 
#Arrannge the file in a way where each 3 letter prefix has a fixed line that is predicted by it's letter combination
#(27^3 + 27^2 + 27 lines, make a function that receives 3 letter prefix and returns character to go to with .seek() to arrive at 3 letter prefix)
abc index1, index2, index3


#Seek in rawIndex.txt for the positions index1, index2, index3. etc (check for correct word)

# with open("example.txt") as input_file:
#     input_file.seek(index_i)  # Move the pointer to the index_i byte
#     line = input_file.readline()  # Read the line starting from the index_i byte
#     print(line) # Check if correct word


#When correct word found, read first 25 lines of rawindex.txt to get indices for word occurences in original text


# Read first lines_number lines starting at .seek() position
# with open(path_to_file) as input_file:
#     input_file.seek(start_position)  # Move the cursor to the specified character position
#     head = [next(input_file) for _ in range(lines_number)] # Read the next lines_number lines
# print(head)
# Have in mind that if the files have less then N lines this will raise StopIteration exception that you must handle 




!_!_
# Sökprogrammet ska inte läsa igenom hela texten och får inte använda speciellt mycket internminne. 
# Internminnesbehovet ska inte växa med antalet distinkta ord i den ursprungliga texten (med andra 
# ord ha konstant internminneskomplexitet). Ni ska därför använda latmanshashning (se föreläsning 2) 
# som datastruktur. Vid redovisningen ska ni kunna motivera att internminneskomplexiteten är konstant 
# för sökprogrammet.


def binary_search_in_file( word):
    word = word[:3]
    file1 = open('lab1/ListOfIndex.txt', 'r')
    left = 0
    file1.seek(0, 2)  # Gå till slutet av filen
    right = file1.tell()


    while True:

        if right - left < 1000: #om det är mindre än 10000 chars mellan punkterna sök linjärt
            file1.seek(left)
            while left <= right:
                line = file1.readline().strip().split(" ")

                if not line or len(line[0]) == 0:
                    break

                if line[0] == word:
                    file1.close()
                    return line[1].split(",")

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


