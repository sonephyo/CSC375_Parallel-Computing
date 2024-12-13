import React, { useState } from "react";
import { useStompClient } from "react-stomp-hooks"; // Adjust to your actual import

export const SendingMessages = ({ operationStatus }: { operationStatus: string }) => {
  const stompClient = useStompClient();
  
  // State to hold form data
  const [formData, setFormData] = useState({
    numOfRows: 5,
    topLeftHeat: 100,
    bottomRightHeat: 100,
    metal1ThermalConstant: 0.75,
    metal2ThermalConstant: 1,
    metal3ThermalConstant: 1.25,
    numOfIterations: 1000,
  });

  // Handle form input change
  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  // Handle form submission
  const start = () => {
    if (stompClient) {
      stompClient.publish({
        destination: "/app/start",
        body: JSON.stringify({
          numOfRows: formData.numOfRows,
          topLeftHeat: formData.topLeftHeat,
          bottomRightHeat: formData.bottomRightHeat,
          metal1ThermalConstant: formData.metal1ThermalConstant,
          metal2ThermalConstant: formData.metal2ThermalConstant,
          metal3ThermalConstant: formData.metal3ThermalConstant,
          numOfIterations: formData.numOfIterations
        }),
      });
    }
  };

  const stop = () => {
    if (stompClient) {
        stompClient.publish({
            destination: "/app/terminate",
        })
    }
  }

  return (
    <div className="max-w-lg mx-auto p-6 bg-white rounded-lg shadow-lg">
      <h2 className="text-2xl font-semibold mb-6">Send Data for Heatmap Calculation</h2>
      <form className="space-y-4">
        <div>
          <label className="block text-sm font-medium text-gray-700">Number of Rows:</label>
          <input
            type="number"
            name="numOfRows"
            value={formData.numOfRows}
            onChange={handleInputChange}
            className="mt-1 block w-full p-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700">Top Left Heat:</label>
          <input
            type="number"
            name="topLeftHeat"
            value={formData.topLeftHeat}
            onChange={handleInputChange}
            className="mt-1 block w-full p-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700">Bottom Right Heat:</label>
          <input
            type="number"
            name="bottomRightHeat"
            value={formData.bottomRightHeat}
            onChange={handleInputChange}
            className="mt-1 block w-full p-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700">Metal 1 Thermal Constant:</label>
          <input
            type="number"
            name="metal1ThermalConstant"
            value={formData.metal1ThermalConstant}
            onChange={handleInputChange}
            className="mt-1 block w-full p-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700">Metal 2 Thermal Constant:</label>
          <input
            type="number"
            name="metal2ThermalConstant"
            value={formData.metal2ThermalConstant}
            onChange={handleInputChange}
            className="mt-1 block w-full p-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700">Metal 3 Thermal Constant:</label>
          <input
            type="number"
            name="metal3ThermalConstant"
            value={formData.metal3ThermalConstant}
            onChange={handleInputChange}
            className="mt-1 block w-full p-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>
        <div>
          <label className="block text-sm font-medium text-gray-700">Num of Iterations:</label>
          <input
            type="number"
            name="numOfIterations"
            value={formData.numOfIterations}
            onChange={handleInputChange}
            className="mt-1 block w-full p-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>
      </form>

      <button
        onClick={start}
        className="mt-6 w-full p-3 bg-blue-500 text-white rounded-md hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-500"
      >
        Start!!!
      </button>
      <button className="mt-6 w-full p-3 rounded-md text-red-500" onClick={stop}>Terminate Program</button>
    </div>
  );
}