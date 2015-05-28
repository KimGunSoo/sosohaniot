import RPi.GPIO as gpio
import sys, tty, termios, time

#Motor 1 Enable GPIO Pin
IC12EN = 25

#Motor 2 Enable GPIO Pin
IC34EN = 18

#Motor 1 GPIO Pin
IC1A = 8
IC2A = 7

#Motor 2 GPIO Pin
IC3A = 14
IC4A = 15

gpio.cleanup()

gpio.setmode(gpio.BCM)

#Pin Output Setup
gpio.setup(IC12EN, gpio.OUT)
gpio.setup(IC34EN, gpio.OUT)

gpio.setup(IC1A, gpio.OUT)
gpio.setup(IC2A, gpio.OUT)
gpio.setup(IC3A, gpio.OUT)
gpio.setup(IC4A, gpio.OUT)

#Pin Initialization
gpio.output(IC12EN, gpio.HIGH)
gpio.output(IC34EN, gpio.HIGH)

gpio.output(IC1A, gpio.LOW)
gpio.output(IC2A, gpio.LOW)
gpio.output(IC3A, gpio.LOW)
gpio.output(IC4A, gpio.LOW)

def getch():
    fd = sys.stdin.fileno()
    old_settings = termios.tcgetattr(fd)
    try:
        tty.setraw(sys.stdin.fileno())
        ch = sys.stdin.read(1)
    finally:
        termios.tcsetattr(fd, termios.TCSADRAIN, old_settings)
    return ch

def motor_forward():
    print "go forward!!!"
    gpio.output(IC3A, gpio.LOW)
    gpio.output(IC4A, gpio.HIGH)
    #test
    time.sleep(0.1)
    gpio.output(IC4A, gpio.LOW)


def motor_back():
    print "go back!!!"
    gpio.output(IC4A, gpio.LOW)
    gpio.output(IC3A, gpio.HIGH)
    #test
    time.sleep(0.1)
    gpio.output(IC3A, gpio.LOW)

def main():
    while True:
        char = getch()
        if(char == "w"):
            motor_forward();
        if(char == "s"):
            motor_back();
        if(char == "x"):
            print "program ended..."
            gpio.output(IC3A, gpio.LOW)
            gpio.output(IC4A, gpio.LOW)
            gpio.cleanup()
            break;

main()





