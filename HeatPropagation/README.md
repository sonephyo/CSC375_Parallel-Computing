# Distributed System for Parallelized Application Processing (Heat Propagation)

This project is designed to showcase a distributed system built to process intensive applications across multiple machines. By using various tools and technologies such as React, TailwindCSS, Spring, WebSockets, and SIMD (Single Instruction, Multiple Data), the project efficiently allocates tasks to multiple university servers, combines results, and displays them on the frontend.

## Video Demo
<div style="position: relative; padding-bottom: 64.90384615384616%; height: 0;"><iframe src="https://www.loom.com/embed/743c0009f2aa431c8602f75161d9881d?sid=dedc257e-f06b-49a2-ac17-0fdaa04d5da9" frameborder="0" webkitallowfullscreen mozallowfullscreen allowfullscreen style="position: absolute; top: 0; left: 0; width: 100%; height: 100%;"></iframe></div>

## Overview

In this project, I built a system that distributes intensive tasks between two university servers and my laptop, with my laptop acting as the task allocator. The results from both servers are then combined and displayed in real-time using WebSockets.

The system uses SIMD for parallel low-level parallelism, significantly increasing the performance of the application. Compared to running the application on a single machine, the runtime has been improved by a factor of four.

## Key Concepts

### 1. Metal Alloy Simulation
The project simulates heat distribution across a metal alloy represented as a 2D array of temperature values. The alloy is partitioned into smaller segments that can be processed in parallel to improve performance. The heat propagation calculation is repeated for a set number of iterations.

### 2. Client-Server Architecture
The system operates with a **client-server model** where the client communicates with two university servers. Each server processes a portion of the data, and the results are sent back to the client for merging. This distributed approach helps handle computationally intensive tasks and scales the system's performance.

### 3. Asynchronous Communication with `CompletableFuture`
To enhance system performance, **asynchronous communication** is used. The `CompletableFuture` class is employed to send and receive data between the client and the server without blocking the execution flow. This allows the client to continue local computations while waiting for the server to return results.

### 4. WebSocket Integration for Real-Time Updates
The project integrates **WebSockets** to provide real-time updates on the heat distribution status. As computations are performed, the results are sent to the frontend, allowing users to view the simulation’s progress interactively.

### 5. SIMD-Based Parallelism
To improve the efficiency of heat propagation calculations, **SIMD (Single Instruction, Multiple Data)** parallelism is used. This approach processes multiple data points in parallel across different machines, reducing the overall computation time.

### 6. Data Merging
After each iteration, the results from the server and the local machine are combined into a single 2D array using the `combine2DArrays` method. This ensures that the partitions of the metal alloy are seamlessly integrated, maintaining consistency across the simulation.

### 7. Task Distribution and Control
The system divides the tasks between the local machine and the servers. The client allocates the work, sending a partition of the metal alloy to the server, and waits for the processed data. The results from both the server and local machine are combined to update the overall simulation.

## Features

- **Distributed System**: Offloads computation to university servers for parallel processing.
- **Real-Time Updates**: Uses WebSockets to display simulation results live on the frontend.
- **Parallel Computation**: Leverages SIMD to parallelize heat propagation calculations.
- **Scalable Architecture**: The system can scale to handle large datasets and intensive computations.

## Prerequisites

- Java 11 or higher
- A WebSocket client or frontend to visualize the results
- Two or more servers for the distributed computation

### Starting the Client

1. **Connect to Servers**: Initialize the connection to the university servers using the `startConnection()` method. The client will connect to the specified IP addresses and ports of the servers.

2. **Running the Simulation**: 
    - Instantiate a `MetalAlloy` object representing the alloy and set up its initial temperature distribution.
    - Call `startHeating()` with the `MetalAlloy` object and the number of iterations to run the simulation.

3. **WebSocket Integration**: If you have a WebSocket service, you can pass it to the client to send real-time updates to the frontend.

### Example

```java
MetalAlloyClient client = new MetalAlloyClient();
client.startConnection("192.168.1.1", 8080, webSocketService);
client.startHeating(metalAlloy, 1000);
```