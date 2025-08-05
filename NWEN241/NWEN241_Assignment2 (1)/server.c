/**
 * Skeleton file for server.c
 *
 * You are free to modify this file to implement the server specifications
 * as detailed in Assignment 3 handout.
 *
 * As a matter of good programming habit, you should break up your imple-
 * mentation into functions. All these functions should contained in this
 * file as you are only allowed to submit this file.
 */

#include <stdio.h>
// Include necessary header files

#include<sys/socket.h>
#include<netinet/in.h>
#include<string.h>
#include<stdlib.h>
#include<unistd.h>
#include<arpa/inet.h>

/**
 * The main function should be able to accept a command-line argument
 * argv[0]: program name
 * argv[1]: port number
 *
 * Read the assignment handout for more details about the server program
 * design specifications.
 */

// Handle the error message if one of the system calls fails
void error(const char *msg){
    printf("Error : %s\n", msg);
    exit(1);
}

// Send HELLO message to client
void send_hello(int client_fd) {
    const char *hello = "HELLO\n";
    if (send(client_fd, hello, strlen(hello), 0) < 0) {
        error("Error sending HELLO");
    }
}

// Handle BYE command: simply close the connection
void handle_bye(int client_fd) {
    close(client_fd); // Close client socket
}

// Handle GET command: open file and send contents
void handle_get(int client_fd, const char *filename) {
    if (strlen(filename) == 0) { // If file is empty, send server error
        const char *msg = "SERVER 500 Get Error\n";
        send(client_fd, msg, strlen(msg), 0);
        return;
    }

    FILE *fp = fopen(filename, "r"); // Attempt to open the file for reading
    if (fp == NULL) { // If file not found, send error message
        const char *msg = "SERVER 404 Not Found\n";
        send(client_fd, msg, strlen(msg), 0);
    } else { // Send confirmation that the file exist
        const char *ok_msg = "SERVER 200 OK\n\n";
        send(client_fd, ok_msg, strlen(ok_msg), 0);

        char file_buffer[1024];
        size_t n;
        while ((n = fread(file_buffer, 1, sizeof(file_buffer), fp)) > 0) {
            send(client_fd, file_buffer, n, 0); // Send file data
        }

        // Send trailing newlines to indicate end of file
        const char *end_msg = "\n\n";
        send(client_fd, end_msg, strlen(end_msg), 0);
        fclose(fp); // Close the file
    }
}

// Handle PUT command: receive data and save to file
void handle_put(int client_fd, const char *filename) {
    if (strlen(filename) == 0) { // If filename is not given, send an error message
        const char *msg = "SERVER 501 Put Error\n";
        send(client_fd, msg, strlen(msg), 0);
        return;
    }

    FILE *fp = fopen(filename, "w"); // Open the file for writing
    if (fp == NULL) {
        const char *msg = "SERVER 501 Put Error\n";
        send(client_fd, msg, strlen(msg), 0);
    } else {
        int newline_count = 0;
        char buffer[1024];

        // Loop until two consecutive newlines are detetcted
        while (1) {
            memset(buffer, 0, sizeof(buffer));
            int bytes = recv(client_fd, buffer, sizeof(buffer) - 1, 0);
            if (bytes <= 0) break;

            for (int i = 0; i < bytes; i++) {
                fputc(buffer[i], fp); // Write byte to file

                // Check for two consecutive lines
                if (buffer[i] == '\n') {
                    newline_count++;
                    if (newline_count == 2) break;
                } else {
                    newline_count = 0;
                }
            }

            if (newline_count == 2) {
                break;
            }
        }
        fclose(fp); // Save and close the file

        const char *created_msg = "SERVER 201 Created\n";
        send(client_fd, created_msg, strlen(created_msg), 0);
    }
}

// Handle unknown command
void handle_unknown(int client_fd) {
    const char *msg = "SERVER 502 Command Error\n";
    send(client_fd, msg, strlen(msg), 0);
}

// Read client message and process command
void process_client(int client_fd) {
    char buffer[1024];
    while(1){
        memset(buffer, 0, sizeof(buffer));
        int bytes_received = recv(client_fd, buffer, sizeof(buffer) - 1, 0);
        if (bytes_received < 0) {
            error("Error receiving data");
        }

        // Parse the command and optional filename
        char command[5], filename[1024];
        memset(command, 0, sizeof(command));
        memset(filename, 0, sizeof(filename));
        sscanf(buffer, "%s %s", command, filename);

        // Handle each command
        if (strcasecmp(command, "BYE") == 0) {
            handle_bye(client_fd);
            break;

        }
        else if (strcasecmp(command, "GET") == 0) {
            handle_get(client_fd, filename);
        }
        else if (strcasecmp(command, "PUT") == 0) {
            handle_put(client_fd, filename);
        }
        else {
            handle_unknown(client_fd);
        }
    }
}

/**
 * Main function to start the server and listen for incoming client connections
 */

int main(int argc, char *argv[])
{
    // Validate command line arguments
    if (argc != 2) {
        fprintf(stderr, "Usage: %s <port>\n", argv[0]);
        return -1;
    }

    // Validate appropriate port number
    int port = atoi(argv[1]);
    if (port < 1024) {
        error("Error: Port number must be >= 1024\n");
        return -1;
    }

    // Create a TCP socket
    int fd = socket(AF_INET, SOCK_STREAM, 0);
    if (fd == -1){
        error("Error creating socket");
    }

    // Prepare address structure
    struct sockaddr_in addr;
    memset(&addr, 0, sizeof(addr));
    addr.sin_family = AF_INET;
    addr.sin_port = htons(port); // Set the port
    addr.sin_addr.s_addr = INADDR_ANY; // Accept connections from any address

    // Bind socket to address and port
    if (bind(fd, (struct sockaddr *)&addr, sizeof(addr)) < 0){
        error("Error binding");
    }

    // Listen for incoming connections
    if (listen(fd, SOMAXCONN) < 0){
        error("Listening error");
    }

    // Main loop for the server: handling incoming connections
    while (1) {
        struct sockaddr_in client_addr;
        socklen_t addrlen = sizeof(client_addr);

        // Accept a new client connection
        int client_fd = accept(fd, (struct sockaddr*)&client_addr, &addrlen);
        if (client_fd < 0){
            error("Error accepting client");
        }

        send_hello(client_fd); // Send HELLO message
        process_client(client_fd); // Handle client commands
    }

    close(fd); // Close server socket
    return 0;
}
