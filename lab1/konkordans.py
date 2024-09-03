import cProfile
import sys

raw_index_path = '/afs/kth.se/misc/info/kurser/DD2350/data/large01/Public/adk22/labb1/rawindex.txt'
korpus_path = '/afs/kth.se/misc/info/kurser/DD2350/data/large01/Public/adk22/labb1/korpus'
bucket_path = '/home/m/a/maadf/Documents/DD2350/lab1/bucket.txt'
hash_path = '/home/m/a/maadf/Documents/DD2350/lab1/hash.txt'

def three_prefix_hash(word) -> int:
    prefix = word[:3].lower()
    hash_idx = 0

    for i, c in enumerate(prefix):
        if ord(c) >= ord('a') and ord(c) <= ord('z'): # a-z, UTF-8 97-122
            hash_idx += (30**i) * (ord(c) - ord('a') + 1)
        elif ord(c) == ord('å'):
            hash_idx += (30**i) * 27 # å, UTF-8 229
        elif ord(c) == ord('ä'):
            hash_idx += (30**i) * 28 # ä, UTF-8 228
        elif ord(c) == ord('ö'):
            hash_idx += (30**i) * 29 # ö, UTF-8 246

    return hash_idx * 8



def binary_search_in_file(word) -> int:
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
                return raw_indices[mid]

            elif curr_word < word:
                left = mid + 1

            else:
                right = mid - 1

        return -1



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


    raw_index_offset = binary_search_in_file(word)
    if raw_index_offset == -1:
        print("Word does not exist")
        return

    with open(raw_index_path, 'r', encoding="latin-1") as raw_index_file:
        raw_index_file.seek(int(raw_index_offset))

        while True:
            line = raw_index_file.readline().strip().split(" ")
            if line[0] != word: break # Done with the word
            korpus_indices.append(int(line[1]))
    

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