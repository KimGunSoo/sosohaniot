#!/bin/bash

RPI_CROSS=raspberrypi_cross_tools

echo "#####################################"
echo "#"
echo "# STEP 1 : RPI git clone"
echo "# RPI cross tools path : $HOME/$RPI_CROSS"

echo "#"
cd $HOME
mkdir $RPI_CROSS
cd $HOME/$RPI_CROSS
git clone git://github.com/raspberrypi/tools.git
echo "#"

echo "# STEP 2 : set enrironment variables"
echo "# SHOULD set RPI_TOOL variable manually."
export RPI_TOOL=$HOME/$RPI_CROSS/tools/arm-bcm2708/gcc-linaro-arm-linux-gnueabihf-raspbian
echo "# 1. create RPI_TOOL variable : $RPI_TOOL"
echo "# 2. add to PATH : $RPI_TOOL/bin"

echo "#"
echo "#####################################"
