# Distributed System for Parallelized Application Processing

This project is designed to showcase a distributed system built to process intensive applications across multiple machines. By using various tools and technologies such as React, TailwindCSS, Spring, WebSockets, and SIMD (Single Instruction, Multiple Data), the project efficiently allocates tasks to multiple university servers, combines results, and displays them on the frontend.

## Overview

In this project, I built a system that distributes intensive tasks between two university servers, with my laptop acting as the task allocator. The results from both servers are then combined and displayed in real-time using WebSockets.

The system uses SIMD for parallel low-level parallelism, significantly increasing the performance of the application. Compared to running the application on a single machine, the runtime has been improved by a factor of four.

## Technologies Used

- **Frontend**: React, TailwindCSS
- **Backend**: Spring Boot
- **WebSockets**: For real-time communication between the frontend and backend
- **Distributed System**: Utilizing two university servers and a client-side laptop for task allocation
- **SIMD**: To enhance low-level parallelism across multiple machines

## Architecture

1. **Client-Side (Frontend)**:
   - Built using **React** and **TailwindCSS** to create an intuitive user interface.
   - Uses **WebSockets** to communicate with the backend in real time, updating the UI with results as they come in.

2. **Server-Side (Backend)**:
   - **Spring Boot** application to manage the server endpoints and task distribution.
   - Allocates tasks to two university servers, which process the data and send results back to the client.

3. **Distributed System**:
   - My laptop acts as the main controller, connecting to two university servers.
   - The servers run the distributed computation and send their results back to my laptop.
   - The results are aggregated and displayed on the frontend through WebSocket communication.

4. **SIMD for Parallelism**:
   - Utilized **SIMD** on multiple machines to achieve parallel processing at a low level, improving the overall computation speed.
   - The system is designed to run tasks in parallel on both university servers, significantly increasing performance compared to a single machine.

## Performance

By using this distributed system with SIMD, the runtime of intensive tasks was reduced by four times compared to running them on a single machine. This improvement was achieved by parallelizing operations across multiple servers and optimizing low-level operations using SIMD.

## Setup and Installation

1. Clone the repository:
   ```bash
   git clone <repository_url>
   cd <project_directory>
