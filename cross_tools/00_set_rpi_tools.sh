#!/bin/bash

# XXX: RPI_CROSS should correspond with 01_set_cross_env.sh
RPI_CROSS=raspberrypi_cross_tools

echo "#####################################"
echo "#"
echo "# RPI git clone"
echo "# RPI cross tools path : $HOME/$RPI_CROSS"
echo "#"
cd $HOME
mkdir $RPI_CROSS
cd $HOME/$RPI_CROSS
git clone git://github.com/raspberrypi/tools.git
echo "#"
echo "#####################################"
