package com.csc375.heat_propagation_backend.conceptTesting;
import jcuda.*;
import jcuda.runtime.*;
import jcuda.driver.*;

public class JCudaExample {
    public static void main(String[] args) {
        // Initialize the CUDA driver
        JCudaDriver.setExceptionsEnabled(true);
        CUdevice device = new CUdevice();
        JCudaDriver.cuInit(0);
        JCudaDriver.cuDeviceGet(device, 0);
        CUcontext context = new CUcontext();
        JCudaDriver.cuCtxCreate(context, 0, device);

        // Allocate memory for input data on the GPU
        int numElements = 1000;
        float[] hostData = new float[numElements];
        for (int i = 0; i < numElements; i++) {
            hostData[i] = i;
        }

        CUdeviceptr deviceData = new CUdeviceptr();
        JCudaDriver.cuMemAlloc(deviceData, numElements * Sizeof.FLOAT);
        JCudaDriver.cuMemcpyHtoD(deviceData, Pointer.to(hostData), numElements * Sizeof.FLOAT);

        // Simple computation on the GPU (this can be replaced with more complex operations)
        // For example, adding a constant value to each element
        // You would need to write the CUDA kernel for this (which is more advanced)

        // Copy data back to host
        JCudaDriver.cuMemcpyDtoH(Pointer.to(hostData), deviceData, numElements * Sizeof.FLOAT);

        // Print results
        for (int i = 0; i < 10; i++) {
            System.out.println(hostData[i]);
        }

        // Clean up
        JCudaDriver.cuMemFree(deviceData);
        JCudaDriver.cuCtxDestroy(context);
    }
}
