from django.shortcuts import render
from django.http import HttpResponse

import RPi.GPIO as gpio
import time

# Create your views here.

def rasp_cmd_go(request):
    print "rasp_cmd_go"

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

    # RC BACK
    for i in range(100):
        gpio.output(IC1A, gpio.HIGH)
        time.sleep(0.01)

    gpio.output(IC1A, gpio.LOW)

    # RC FORWORD
    for i in range(100):
        gpio.output(IC2A, gpio.HIGH)
        time.sleep(0.01)

    gpio.output(IC2A, gpio.LOW)

    # LEFT
    for i in range(100):
        gpio.output(IC3A, gpio.HIGH)
        time.sleep(0.01)

    gpio.output(IC3A, gpio.LOW)

    # 
    for i in range(100):
        gpio.output(IC4A, gpio.HIGH)
        time.sleep(0.01)

    gpio.output(IC4A, gpio.LOW)

    gpio.cleanup()
    return HttpResponse("Motor control command!");

def index(request):
    return HttpResponse("Home page!");
