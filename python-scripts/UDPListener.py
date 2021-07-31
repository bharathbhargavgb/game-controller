import socket
from SensorFlow import *

SEPARATOR = ','
RECV_BYTES = 1024

class UDPListener:

	ballFlow = SensorFlow()

	def __init__(self, ip, port):

		self.sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
		self.sock.bind((ip, port))
		print ("Starting UDP server on IP " + str(ip) + " PORT " + str(port))

	def listen(self):

		print ("Listening...")
		while True:
			data, address = self.sock.recvfrom(RECV_BYTES)

			if data.find(SEPARATOR) != -1:
				sensor_axes = data.split(SEPARATOR)
				print (sensor_axes)
				self.ballFlow.updatePosition(sensor_axes)
			else:
				print (data)

listener = UDPListener("192.168.1.3", 9156)
listener.listen()
