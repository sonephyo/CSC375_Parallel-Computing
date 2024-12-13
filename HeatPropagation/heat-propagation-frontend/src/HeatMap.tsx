import React, { useRef, useEffect } from "react";
import * as d3 from "d3";

const Heatmap = ({ data }: { data: number[][] }) => {
  const ref = useRef<SVGSVGElement | null>(null);

  useEffect(() => {
    const margin = { top: 30, right: 30, bottom: 30, left: 30 };

    const numCols = data[0].length;
    const numRows = data.length;

    // Calculate available space for the heatmap
    const availableWidth = window.innerWidth - margin.left - margin.right;
    const availableHeight = window.innerHeight - margin.top - margin.bottom;

    // Calculate cell size that fits the available space
    const cellSizeX = availableWidth / numCols;
    const cellSizeY = availableHeight / numRows;

    // Use the smaller of the two cell sizes to ensure it fits in both directions
    const cellSize = Math.min(cellSizeX, cellSizeY);

    // Calculate the actual width and height of the SVG
    const width = numCols * cellSize;
    const height = numRows * cellSize;
    const svg = d3
      .select(ref.current)
      .append("svg")
      .attr("width", width + margin.left + margin.right)
      .attr("height", height + margin.top + margin.bottom)
      .append("g")
      .attr("transform", `translate(${margin.left},${margin.top})`);

    // Flatten data for D3
    const flattenedData = data.flatMap((row, rowIndex) =>
      row.map((value, colIndex) => ({
        value,
        x: colIndex * cellSize,
        y: rowIndex * cellSize,
      }))
    );
    const colorScale = d3
      .scaleSequential(d3.interpolateRdYlBu) // Blue to red interpolation
      .domain([100, 0])
      .clamp(true);

    // Bind data to rectangles
    svg
      .selectAll("rect")
      .data(flattenedData)
      .join("rect")
      .attr("x", (d) => d.x)
      .attr("y", (d) => d.y)
      .attr("width", cellSize)
      .attr("height", cellSize)
      .attr("fill", (d) => colorScale(d.value))
      .attr("stroke", "white");
  }, [data]);

  return (
    <svg ref={ref} width={window.innerWidth} height={window.innerHeight}></svg>
  );
};

export default Heatmap;
