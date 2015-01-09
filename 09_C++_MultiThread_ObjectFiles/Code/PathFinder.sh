#!/bin/bash

clear

g++ Maze.o Path.o SubmitMazeSoln.o PathFinder.cpp -lpthread -o PathFinder

./PathFinder
