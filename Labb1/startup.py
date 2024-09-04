raw_index_path = '/afs/kth.se/misc/info/kurser/DD2350/data/large01/Public/adk22/labb1/rawindex.txt'
bucket_path = '/home/m/a/maadf/Documents/DD2350/Labb1/bucket.txt' #'/var/tmp/bucket.txt
hash_path = '/home/m/a/maadf/Documents/DD2350/Labb1/hash.txt' #'/var/tmp/hash.txt'


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


def setup_struct():
    file_in = open(raw_index_path, 'r', encoding="latin-1")
    file_out = open(bucket_path, 'w')
    hash_out = open(hash_path, 'wb')

    hash_size = (three_prefix_hash('ööö') + 1) * 8
    hash_out.truncate(hash_size)

    indices = [0]
    out_pos = 0
    in_pos = 0

    line = file_in.readline()
    prev_word = line.strip().split(" ")[0]

    while True:
        # Update our position in the in_file
        in_pos += len(line)

        # Get next line from file
        line = file_in.readline()
        word = line.strip().split(" ")[0]


        #check if its the end of file
        if not line:
            file_out.write(",".join(map(str, indices)) + "\n")
            file_in.close()
            file_out.close()
            hash_out.close()
            return


        # Check if the first three letters are the same
        if (prev_word != word and prev_word[:3] == word[:3]):
            indices.append(in_pos)

        
        # Check if the first three letters are different
        elif (prev_word != word and prev_word[:3] != word[:3]):
            # Calculate hash index (byte offset) and seek there in hash_out & write out_pos
            hash_idx = three_prefix_hash(prev_word)
            hash_out.seek(hash_idx)
            hash_out.write(out_pos.to_bytes(8, byteorder='big'))

            index_string = ",".join(map(str, indices)) + "\n"
            file_out.write(index_string)
            out_pos += len(index_string)
            indices = [in_pos]

        prev_word = word


if __name__ == "__main__":
    setup_struct()