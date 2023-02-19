import threading
import socket

client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client.connect(('localhost', 1234))

def client_receive():
    while True:
        try: 
            message = client.recv(1024).decode('utf-8')[:-1]
            print(message)
        except:
            print('Error!')
            client.close()
            break
def client_send():
    while True:
        msg = input()
        client.send(msg.encode('utf-8')+b'\n')

receive_thread = threading.Thread(target=client_receive)
send_thread = threading.Thread(target=client_send)

receive_thread.start()
send_thread.start()