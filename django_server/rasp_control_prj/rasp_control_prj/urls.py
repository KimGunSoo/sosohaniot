"""rasp_control_prj URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/1.8/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  url(r'^$', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  url(r'^$', Home.as_view(), name='home')
Including another URLconf
    1. Add an import:  from blog import urls as blog_urls
    2. Add a URL to urlpatterns:  url(r'^blog/', include(blog_urls))
"""
from django.conf.urls import include, url
from django.contrib import admin
from rasp_if.motor_control import rasp_cmd_init
from rasp_if.motor_control import rasp_cmd_deinit
from rasp_if.motor_control import rasp_cmd_getstate
from rasp_if.motor_control import rasp_cmd_go
from rasp_if.motor_control import rasp_cmd_back
from rasp_if.views import index

urlpatterns = [
    url(r'^admin/', include(admin.site.urls)),
    url(r'^rasp_cmd_init/', rasp_cmd_init),
    url(r'^rasp_cmd_deinit/', rasp_cmd_deinit),
    url(r'^rasp_cmd_getstate/', rasp_cmd_getstate),
    url(r'^rasp_cmd_go/', rasp_cmd_go),
    url(r'^rasp_cmd_back/', rasp_cmd_back),
    url(r'^$', index),
]
