import { useState, useRef, useEffect } from "react";
import * as d3 from "d3";

function BarChart2() {
  const [data, setData] = useState([10, 15, 20, 25, 30, 35, 40]);

  const updateData = () => {
    setData(data.map((value) => value + Math.round(Math.random() * 10 - 5)));
  };

  return (
    <div style={{ textAlign: "center" }}>
      <h1>Bar Chart with D3.js and React</h1>
      <button onClick={updateData}>Update Data</button>
      <BarChartTemp data={data} width={600} height={400} />
    </div>
  );
}

export default BarChart2;

const BarChartTemp = ({ data, width = 500, height = 300 }) => {
  const svgRef = useRef();

  useEffect(() => {
    // Set up the SVG element
    const svg = d3
      .select(svgRef.current)
      .attr("width", width)
      .attr("height", height)
      .style("background", "#f3f3f3")
      .style("margin-top", "20px");

    // Set up scales
    const xScale = d3
      .scaleBand()
      .domain(data.map((_, i) => i))
      .range([0, width])
      .padding(0.2);

    const yScale = d3
      .scaleLinear()
      .domain([0, d3.max(data)])
      .range([height, 0]);

    // Create the bars
    svg
      .selectAll(".bar")
      .data(data)
      .join("rect")
      .attr("class", "bar")
      .attr("x", (d, i) => xScale(i))
      .attr("y", (d) => yScale(d))
      .attr("width", xScale.bandwidth())
      .attr("height", (d) => height - yScale(d))
      .attr("fill", "teal");

    // Create the x-axis
    svg
      .select(".x-axis")
      .style("transform", `translateY(${height}px)`)
      .call(d3.axisBottom(xScale).tickFormat((i) => i + 1));

    // Create the y-axis
    svg.select(".y-axis").call(d3.axisLeft(yScale));
  }, [data, width, height]);

  return (
    <svg ref={svgRef}>
      <g className="x-axis" />
      <g className="y-axis" />
    </svg>
  );
};
