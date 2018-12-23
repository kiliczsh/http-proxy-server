import socket
from sys import argv,getsizeof
from os import urandom
from base64 import b64encode
import random,string





def random_string(string_length):
    """Generate a random string of fixed length """
    letters = string.ascii_lowercase
    return ''.join(random.choice(letters) for i in range(string_length))

def create_file(file_size):
    index_file = open("./index.html",'w+')
    body_text = str(random_string(file_size))
    content  = "<!DOCTYPE html>\n<html>\n<head>\n"
    content += "<title>"+str(file_size)+" bytes</title>\n</head>\n<body>\n<p>"
    content += body_text
    content += "</p>\n</body>\n</html>"
    print("body text size: ", getsizeof(body_text))
    print("body text len: ",len(body_text))
    index_file.write(content)
    return index_file


def get_file(request):
    headers = request.split('\n')
    filename = headers[0].split()[1]
    method = headers[0].split()[0]
    if method == 'GET':
        if filename == '/':
            filename = '/200.html'
        else:
            path = filename[1:]
            try:
                file_size = int(path)
                in_limit = 100 <= file_size <= 20000
                if(in_limit):
                    create_file(file_size)
                    filename = '/index.html'
                else:
                    filename = '/400.html'
            except:
                filename = '/400.html'
    else: # if not GET show 501
        filename = '/501.html'
    return filename


# Define socket host and port
SERVER_HOST = '0.0.0.0'
SERVER_PORT = int(argv[1])

# Create socket
server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
server_socket.bind((SERVER_HOST, SERVER_PORT))
server_socket.listen(10)
print('listening on port %s ...' % SERVER_PORT)


while True:
    # Wait for client connections
    client, client_address = server_socket.accept()
    
    # Get the client request
    request = client.recv(1024).decode()
    print(request)
    # Return an HTTP response
    
    try:
        filename = get_file(request)
        file_input = open("."+filename)
        content = file_input.read()
        file_input.close()

        response = ""
        response += str('HTTP/1.0 200 OK\r\n')
        response += str('Content-Length: ' + str(len(content)) + '\r\n')
        response += str('Content-Type: text/html; charset=UTF-8' + '\r\n\r\n')
        client.sendall(response.encode())
        response = content
        response_bytes = response.encode()
        client.sendall(response_bytes)

    except FileNotFoundError:

        response = 'HTTP/1.0 404 NOT FOUND\r\n'
        response += 'File Not Found'
        response_bytes = response.encode()
        client.sendall(response_bytes)

    # Close connection
    client.close()

# Close socket
server_socket.close()