import matplotlib.pyplot as plt

class SensorFlow:

	def __init__(self):

		self.circle = plt.Circle((0,0), 1, color = 'r')
		self.fig, self.ax = plt.subplots()
		self.ax.add_artist(self.circle)

		plt.xlim([-15, 15])
		plt.ylim([-15, 15])

		plt.ion()
		plt.show()
		plt.pause(0.001)

	def updatePosition(self, sensor_axes):

		x_val = self.circle.center[0] + float(sensor_axes[1])
		y_val = self.circle.center[1] + float(sensor_axes[2])

		self.circle.center = (x_val/2, y_val/2)
		plt.pause(0.001)
		self.fig.canvas.draw()

