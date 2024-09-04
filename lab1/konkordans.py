import cProfile
import time
import sys

raw_index_path = '/afs/kth.se/misc/info/kurser/DD2350/data/large01/Public/adk22/labb1/rawindex.txt'
korpus_path = '/afs/kth.se/misc/info/kurser/DD2350/data/large01/Public/adk22/labb1/korpus'
bucket_path = '/home/m/a/maadf/Documents/DD2350/lab1/bucket.txt'
hash_path = '/home/m/a/maadf/Documents/DD2350/lab1/hash.txt'

def three_prefix_hash(word) -> int:
    prefix = word[:3].lower()
    hash_idx  = 0

    for i, char in enumerate(prefix):
        if 'a' <= char <= 'z':  # Map a-z
            hash_idx += (30**i) * (ord(char) - ord('a') + 1)
        elif char == 'å':  # Map å to 27
            hash_idx += (30**i) * (27)
        elif char == 'ä':  # Map ä to 28
            hash_idx += (30**i) * (28)
        elif char == 'ö':  # Map ö to 29
            hash_idx += (30**i) * (29)

    return hash_idx  * 8


def binary_search_in_file(word) -> tuple:
    with open(raw_index_path, 'r', encoding="latin-1") as raw_index_file, \
        open(bucket_path, 'r') as bucket_file, \
        open(hash_path, 'rb') as hash_file:


        hash_file.seek(three_prefix_hash(word))
        hash_data = hash_file.read(8)

        bucket_offset = int.from_bytes(hash_data, byteorder='big')
        bucket_file.seek(bucket_offset)

        raw_indices = bucket_file.readline().strip().split(",")
        left, right = 0, len(raw_indices) - 1

        while left <= right:
            mid = left + (right - left) // 2
            raw_index_file.seek(int(raw_indices[mid]))
            curr_word = raw_index_file.readline().strip().split(" ")[0]

            if curr_word == word:
                end = (bucket_file.readline().strip().split(",")[0] 
                if mid >= len(raw_indices) - 1 
                else raw_indices[mid + 1])

                return raw_indices[mid], end

            elif curr_word < word:
                left = mid + 1

            else:
                right = mid - 1

        return -1


def linear_search_in_file(word) -> int:
    with open(raw_index_path, 'r', encoding="latin-1") as raw_index_file, \
        open(bucket_path, 'r') as bucket_file, \
        open(hash_path, 'rb') as hash_file:


        hash_file.seek(three_prefix_hash(word))
        hash_data = hash_file.read(8)

        bucket_offset = int.from_bytes(hash_data, byteorder='big')
        bucket_file.seek(bucket_offset)

        raw_indices = bucket_file.readline().strip().split(",")
        curr, last = 0, len(raw_indices) - 1

        while curr <= last:
            raw_index_file.seek(int(raw_indices[curr]))
            curr_word = raw_index_file.readline().strip().split(" ")[0]

            if curr_word == word:
                return raw_indices[curr]
            else:
                curr += 1

        return -1, -1



def get_occurrences(word, indices) -> str: ## not done
    korpus_file = open(korpus_path, 'r', encoding="latin-1")
    occurrence_text = ""
    n = 30 # number of characters to print before and after the word

    for idx in indices:
        korpus_file.seek(max(idx-n, 0))
        line = korpus_file.read(60+len(word)).replace("\n", " ")
        occurrence_text += line + "\n"  
    
    korpus_file.close()
    return occurrence_text



def konkordans(word):
    korpus_indices = []

    #raw_index_offset = linear_search_in_file(word)
    raw_index_start, raw_index_end = binary_search_in_file(word)

    if raw_index_start == -1:
        print("Word does not exist")
        return


    with open(raw_index_path, 'r', encoding="latin-1") as raw_index_file:
        raw_index_file.seek(int(raw_index_start))
        lines = raw_index_file.read(int(raw_index_end) - int(raw_index_start)).strip().split("\n")
        korpus_indices = [int(line.strip().split(" ")[1]) for line in lines]


    print("The word "+ word + " occurs " + str(len(korpus_indices)) + " times.")
    
    if (len(korpus_indices)>25):
        answer = input("Print all occurences? Y/N: ").lower()
        if answer.lower() == "y":
            print(get_occurrences(word, korpus_indices))
        else:
            print(get_occurrences(word, korpus_indices[:25]))
    else:
        print(get_occurrences(word, korpus_indices))



if __name__ == "__main__":
    if len(sys.argv) == 2:
        cProfile.run("konkordans(sys.argv[1].lower())")
    else:
        print("Usage: python3 konkordans.py <word>")
        sys.exit(1)