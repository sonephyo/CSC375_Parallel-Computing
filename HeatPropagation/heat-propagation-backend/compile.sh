#!/bin/bash

cd /Users/sonephyo/Documents/Fall\ 2024/CSC375/Code/HeatPropagation/heat-propagation-backend

# Compiling classes needed for the MetalAlloyServer and also a testing MetalAlloyTest
javac -d out/production/heat-propagation-backend -sourcepath src/main/java \
src/main/java/com/csc375/heat_propagation_backend/metalAlloyServerClient/MetalAlloyServer.java \
src/main/java/com/csc375/heat_propagation_backend/metalAlloy/MetalAlloy.java \
src/main/java/com/csc375/heat_propagation_backend/metalAlloy/MetalAlloyPartition.java
