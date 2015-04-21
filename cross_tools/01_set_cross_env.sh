#!/bin/bash

# XXX: RPI_CROSS should correspond with 00_set_rpi_tools.sh
RPI_CROSS=raspberrypi_cross_tools

echo "#####################################"
echo "#"
echo "# Set enrironment variables"
echo "#"
ARCH=`uname -a | awk '{print $12}'`
echo "# ARCH=$ARCH"
if [ $ARCH == "i386" -o $ARCH == "i686" ]; then
export RPI_TOOL=$HOME/$RPI_CROSS/tools/arm-bcm2708/gcc-linaro-arm-linux-gnueabihf-raspbian
else
export RPI_TOOL=$HOME/$RPI_CROSS/tools/arm-bcm2708/gcc-linaro-arm-linux-gnueabihf-raspbian-x64
fi
export PATH=$PATH:$RPI_TOOL/bin
echo "# 1. set RPI_TOOL variable : $RPI_TOOL"
echo "# 2. add to PATH : $RPI_TOOL/bin"
echo "#           PATH : $PATH"
echo "#"
echo "#####################################"
