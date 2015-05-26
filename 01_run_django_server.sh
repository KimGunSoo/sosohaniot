#!/bin/bash

echo "#####################################"
echo "#"
echo "# Run the django server!"
echo "#"
sudo ./django_server/rasp_control_prj/manage.py runserver 0.0.0.0:8000
echo "#"
echo "#####################################"
