from startup import three_prefix_hash
import time
import sys

raw_index_path = '/afs/kth.se/misc/info/kurser/DD2350/data/large01/Public/adk22/labb1/rawindex.txt'
korpus_path = '/afs/kth.se/misc/info/kurser/DD2350/data/large01/Public/adk22/labb1/korpus'
bucket_path = '/home/m/a/maadf/Documents/DD2350/Labb1/bucket.txt' #'/var/tmp/bucket.txt
hash_path = '/home/m/a/maadf/Documents/DD2350/Labb1/hash.txt' #'/var/tmp/hash.txt'


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
                if not end: # If we have reached the end of the bucket set end to the last char of the file
                    end = int(raw_index_file.seek(0, 2))

                return int(raw_indices[mid]), int(end)

            elif curr_word < word:
                left = mid + 1

            else:
                right = mid - 1

        return -1, -1

def binary_and_linear_in_file(word) -> tuple:
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
            if(right-left)< 1000:
                curr = left
                last = right
                while curr <= last:
                    raw_index_file.seek(int(raw_indices[curr]))
                    curr_word = raw_index_file.readline().strip().split(" ")[0]

                    if curr_word == word:
                        end = (bucket_file.readline().strip().split(",")[0] 
                        if curr >= len(raw_indices) - 1
                        else raw_indices[curr + 1])
                        if not end:
                            end = int(raw_index_file.seek(0, 2))

                        return int(raw_indices[curr]), int(end)
                    else:
                        curr += 1

                return -1, -1
                

            if curr_word == word:
                end = (bucket_file.readline().strip().split(",")[0] 
                if mid >= len(raw_indices) - 1 
                else raw_indices[mid + 1])

                return int(raw_indices[mid]), int(end)

            elif curr_word < word:
                left = mid + 1

            else:
                right = mid - 1

        return -1, -1


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
                end = (bucket_file.readline().strip().split(",")[0] 
                if curr >= len(raw_indices) - 1
                else raw_indices[curr + 1])
                if not end:
                    end = int(raw_index_file.seek(0, 2))

                return int(raw_indices[curr]), int(end)
            else:
                curr += 1

        return -1, -1


def print_occurences(start, end):
    raw_index_file = open(raw_index_path, 'r', encoding="latin-1")
    korpus_file = open(korpus_path, 'r', encoding="latin-1")
    n = 30 # number of characters to print before and after the word

    raw_index_file.seek(start)
    curr_pos = start
    count = 0

    while curr_pos < end:
        raw_line = raw_index_file.readline()
        line = raw_line.strip().split(" ")
        curr_pos += len(raw_line)
        count += 1

        korpus_file.seek( max( int(line[1]) - n, 0) )
        print(korpus_file.read( (2 * n) + len(line[0]) ).replace("\n", " "))

        if (count == 25): # Print first 25 occurences, then ask user if they want to print more
            answer = input("Print all occurences? Y/N: ").lower()
            if (answer != 'y'):
                break

    raw_index_file.close()
    korpus_file.close()


def konkordans(word):
    time_start = time.time()
    raw_index_start, raw_index_end = binary_search_in_file(word)

    if raw_index_start == -1:
        print("Word does not exist")
        return

    with open(raw_index_path, 'r', encoding="latin-1") as raw_index_file:
        raw_index_file.seek(int(raw_index_start))
        occurences = raw_index_file.read(int(raw_index_end) - int(raw_index_start)).count("\n")


    print("Time elapsed: " + str(time.time() - time_start) + " seconds.")
    print("The word '"+ word + "' occurs " + str(occurences) + " times.")
    print_occurences(raw_index_start, raw_index_end)



if __name__ == "__main__":
    if len(sys.argv) == 2:
        konkordans(sys.argv[1].lower())
    else:
        print("Usage: python3 konkordans.py <word>")
        sys.exit(1)