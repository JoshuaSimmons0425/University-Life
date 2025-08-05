#include <string.h>

// Task 1: Insertion
int editor_insert_char(char editing_buffer[], int editing_buflen, char to_insert, int pos){
    if(pos < 0 || pos >= editing_buflen){ // Check if position is outside the boundary
        return 0;
    }
    for(int i = editing_buflen - 1; i > pos; i--){
        editing_buffer[i] = editing_buffer[i - 1]; // For every element on the right of pos, shift to the right once
    }
    editing_buffer[pos] = to_insert; // Insert character to the corresponding position

    return 1;
}

// Task 2: Deletion
int editor_delete_char(char editing_buffer[], int editing_buflen, char to_delete, int offset){
    if(offset < 0 || offset >= editing_buflen){ // Check if offset is outside the boundary
        return 0;
    }
    int pos = -1;
    for(int i = offset; i < editing_buflen - 1; i++){
        if (editing_buffer[i] == '\0'){ // If the offset is beyond editing_buffer range, terminate the operation
            return 0;
        }
        if (editing_buffer[i] == to_delete){ // If the character to delete is found, update it's position and stop the loop.
            pos = i;
            break;
        }
    }
    for(int i = pos; i < editing_buflen - 1; i++){
        editing_buffer[i] = editing_buffer[i + 1]; // Shift all the elements after the offset to the left once
    }
    if (pos == -1){ // If the character is not found, terminate the operation
        return 0;
    }

    editing_buffer[editing_buflen - 1] = '\0'; // Add a null terminater at the end of the buffer after deletion

    return 1;
}

// Task 3: Replacement
int editor_replace_str(char editing_buffer[], int editing_buflen, const char *str, const char *replacement, int offset){

    if(!str || str[0] == '\0' || offset < 0 || offset >= editing_buflen){ // Check if str empty or offset is outside the boundary
        return -1;
    }

    int len_str = strlen(str), len_replacement = strlen(replacement), pos = -1; // Initialising the length of each string and position

    for(int i = offset; i < editing_buflen - len_str; i++){
        if(strncmp(&editing_buffer[i], str, len_str) == 0){ // Check if str exists within the buffer
            pos = i;
            break;
        }
    }
    if(pos == -1){ // If str doesn't exist, terminate the operation
        return -1;
    }
    if(len_replacement > len_str){ // If the replacement is longer than str, the buffer will shift to the right allocated to that extra space
        int shift = len_replacement - len_str, changed_end = pos + len_replacement;

        if(changed_end >= editing_buflen){ // If the added contents of replacement exceeds buflen, trunicate the contents
            len_replacement = editing_buflen - pos;
            shift = len_replacement - len_str;
        }
        for(int i = editing_buflen - 1; i >= pos + len_str; i--){
            if(i + shift < editing_buflen){
                editing_buffer[i + shift] = editing_buffer[i]; // Moving the characters with the amount of shift to the right, starting from the end
            }
        }

    }
    else if(len_replacement < len_str){ // If the replacement is shorter than str, the buffer will shift to the left
        int shift = len_str - len_replacement; // Initialise the shift
        for(int i = pos + len_replacement; i < editing_buflen - shift; i++){
            editing_buffer[i] = editing_buffer[i + shift]; // Move the characters to the left with the amount of shift
        }

        for(int i = editing_buflen - shift; i < editing_buflen; i++){
            editing_buffer[i] = '\0'; // Add a null terminater to the indexes that are now empty from the shift
        }
    }
    for(int i = 0; i < len_replacement; i++){
        editing_buffer[i + pos] = replacement[i]; // Replace the corresponding indexes with the characters of replacement
    }

    editing_buffer[editing_buflen - 1] = '\0'; // Adding a null terminator to the end of the buffer after successful replacement

    return (pos + len_replacement - 1 < editing_buflen) ? pos + len_replacement - 1: editing_buflen - 1; // Returning the index of the last replaced character
}


// Task 4: Viewing
void editor_view(int rows, int cols, char viewing_buffer[rows][cols], const char editing_buffer[], int editing_buflen, int wrap){

    int row = 0, col = 0; // Initialising column and row number for each buffer

    for(int edit_idx = 0; edit_idx < editing_buflen; edit_idx++){
        if(editing_buffer[edit_idx] != '\0' && row < rows){ // Check if the index does not contain a null terminater and is within the viewing buffer
            char ch = editing_buffer[edit_idx]; // Initialise the character
            if (ch == '\n'){ // If the end of the editing buffer is reached, go to the next row and reset the column
                row++;
                col = 0;
            } else {
                if(wrap == 0){ // If wrapping does not occur and the contents exceed the buflen, do nothing with the remaining characters
                    if(col < cols - 1){
                        viewing_buffer[row][col] = ch; // Place the character into the index
                        col++; // Move to next column
                    }
                } else { // If wrapping does occur and the buffer contents exceeds the buflen
                    viewing_buffer[row][col] = ch;
                    col++;
                    if(col == cols - 1){ // If there are remaining contents of the buffer, move it to the start of the next row.
                        row++;
                        col = 0;
                    }
                }
            }
        }
    }
}

