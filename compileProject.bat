@echo off

if not exist "./bin" mkdir bin

javac -d bin Main.java %*