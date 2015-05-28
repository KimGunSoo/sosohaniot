from django.shortcuts import render
from django.http import HttpResponse

import RPi.GPIO as gpio
import time

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

GPIO_initialized = 0

def motor_forward():
    print "go forward!!!"
    print "debug: IC3A=%d" % (IC3A)
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


def rasp_cmd_init(request):
    print "rasp_cmd_init"

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

    global GPIO_initialized
    GPIO_initialized = 1

    return HttpResponse("Motor control command! : INIT");

def rasp_cmd_deinit(request):
    print "rasp_cmd_deinit"
    gpio.output(IC3A, gpio.LOW)
    gpio.output(IC4A, gpio.LOW)
    gpio.cleanup()
 
    global GPIO_initialized
    GPIO_initialized = 0
    return HttpResponse("Motor control command! : DEINIT");

def rasp_cmd_getstate(request):
    global GPIO_initialized
    ret_string = "Motor control command! : STATE = " + str(GPIO_initialized)
    return HttpResponse(ret_string);

def rasp_cmd_go(request):
    print "rasp_cmd_go"

    global GPIO_initialized
    if(GPIO_initialized == 1):
        motor_forward()
    else:
        print "gpio is NOT initialized"

    return HttpResponse("Motor control command! : GO");

def rasp_cmd_back(request):
    print "rasp_cmd_back"

    global GPIO_initialized
    if(GPIO_initialized == 1):
        motor_back()
    else:
        print "gpio is NOT initialized"

    return HttpResponse("Motor control command! : BACK");

