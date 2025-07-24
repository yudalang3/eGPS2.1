############################################################################
## How to use
## bash running.sh inputDir
############################################################################
### Following are the contents of the bash file


#!/bin/bash

# check for the dir parameter
if [ "$#" -ne 1 ]; then
    echo "Please input the dir."
    echo "Usage: $0 <directory-path>"
    exit 1
fi

# extract the input dir parameter
INPUT_DIR=$1

# define the motif file
MEME_FILE="XXX_bindingMotif.meme"
OUTPUT_SUFFIX=".output.results.txt"

# check for the motif file
if [ ! -f "$MEME_FILE" ]; then
    echo "Error: MEME file '$MEME_FILE' not found!"
    exit 1
fi

# iterate the input dir with file ending with extracted.fa
for FILE in $(find "$INPUT_DIR" -type f -name "*.extracted.fa"); do
    # extract the file name（not including parent dir and extension）
    BASENAME=$(basename "$FILE" .extracted.fa)

    # set the output file, if run with the best-site parameter, the output dir is not mandatory.
    OUTPUT_DIR="${BASENAME}_fimo_results"
    # mkdir -p "$OUTPUT_DIR"

    # construct the cmd
    CMD="time fimo --best-site --no-pgc --no-qvalue --thresh 1e-3 -oc '$OUTPUT_DIR' '$MEME_FILE' '$FILE' > '${BASENAME}${OUTPUT_SUFFIX}'"

    # debug the cmd
    echo "Running command: $CMD"

    # execute the cmd
    eval "$CMD"

    echo "Processed: $FILE -> ${BASENAME}${OUTPUT_SUFFIX}"
done

echo "All files processed!"

