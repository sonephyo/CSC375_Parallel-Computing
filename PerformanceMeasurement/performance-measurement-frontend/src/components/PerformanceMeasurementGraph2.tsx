import * as d3 from "d3";
import { useEffect, useRef } from "react";
import { BenchMarkData } from "../data/dummyData";

function PerformanceMeasurementGraph2({ data }: { data: BenchMarkData }) {
  const ref = useRef();

  useEffect(() => {
    const tooltip = d3.select("#tooltip");

    const customData = data.customConcurrentHashMap1000.results;
    const javaData = data.javaConcurrentHashMap1000.results;
    // set the dimensions and margins of the graph
    const margin = { top: 30, right: 30, bottom: 120, left: 80 },
      width = 800 - margin.left - margin.right,
      height = 700 - margin.top - margin.bottom;

    // append the svg object to the body of the page
    const svg = d3
      .select(ref.current)
      .append("svg")
      .attr("width", width + margin.left + margin.right)
      .attr("height", height + margin.top + margin.bottom)
      .append("g")
      .attr("transform", `translate(${margin.left},${margin.top})`);

    const filteredData = [];

    for (const name in customData) {
      filteredData.push({
        method: name,
        customConcurrentHashMapScore: customData[name]["score"],
        javaConcurrentHashMapScore: javaData[name]["score"],
      });
    }
    console.log(filteredData);

    // X-axis scale (methods)
    const x0 = d3
      .scaleBand()
      .range([0, width])
      .domain(filteredData.map((d) => d.method))
      .padding(0.2);

    // X1 scale (sub-grouping for each method)
    const x1 = d3
      .scaleBand()
      .domain(["customConcurrentHashMapScore", "javaConcurrentHashMapScore"])
      .range([0, x0.bandwidth()])
      .padding(0.1);

    const maxY = d3.max(filteredData, (d) =>
      Math.max(d.customConcurrentHashMapScore, d.javaConcurrentHashMapScore)
    );
    const y = d3.scaleLog().domain([1, maxY]).range([height, 0]).base(10);

    svg
      .append("g")
      .attr("transform", `translate(0, ${height})`)
      .call(d3.axisBottom(x0))
      .selectAll("text")
      .attr("transform", "translate(-10,0)rotate(-45)")
      .style("text-anchor", "end");

    svg.append("g").call(d3.axisLeft(y));

    // Define colors for the two data sets
    const color = d3
      .scaleOrdinal()
      .domain(["customConcurrentHashMapScore", "javaConcurrentHashMapScore"])
      .range(["#5f0f40", "#0f5f5f"]);

    // Add Y axis
    svg
      .selectAll("g.layer")
      .data(filteredData)
      .join("g")
      .attr("transform", (d) => `translate(${x0(d.method)},0)`)
      .selectAll("rect")
      .data((d) => [
        {
          key: "customConcurrentHashMapScore",
          value: d.customConcurrentHashMapScore,
        },
        {
          key: "javaConcurrentHashMapScore",
          value: d.javaConcurrentHashMapScore,
        },
      ])
      .join("rect")
      .attr("x", (d) => x1(d.key))
      .attr("y", (d) => y(d.value))
      .attr("width", x1.bandwidth())
      .attr("height", (d) => height - y(d.value))
      .attr("fill", (d) => color(d.key))
      .on("mouseover", (event, d) => {
        tooltip
          .style("display", "block")
          .html(`Type: ${d.key}<br>Score: ${d.value}`);
      })
      .on("mousemove", (event) => {
        tooltip
          .style("left", `${event.pageX + 5}px`)
          .style("top", `${event.pageY + 5}px`);
      })
      .on("mouseout", () => {
        tooltip.style("display", "none");
      });
    svg
      .append("text")
      .attr("x", -300)
      .attr("y", -50)
      .attr("dy", ".35em")
      .attr("transform", "rotate(-90)") 
      .style("text-anchor", "middle") 
      .text("op/s") 
      .style("font-size", "16px")
      .style("fill", "white");

    const legend = svg
      .append("g")
      .attr("transform", `translate(${width - 400},${-margin.top / 2 - 15})`);
    legend
      .selectAll("rect")
      .data(["customConcurrentHashMapScore", "javaConcurrentHashMapScore"])
      .enter()
      .append("rect")
      .attr("x", (d, i) => i * 220)
      .attr("y", 0)
      .attr("width", 15)
      .attr("height", 15)
      .attr("fill", (d) => color(d));

    legend
      .selectAll("text")
      .data(["customConcurrentHashMapScore", "javaConcurrentHashMapScore"])
      .enter()
      .append("text")
      .attr("x", (d, i) => i * 220 + 20)
      .attr("y", 12)
      .text((d) => d)
      .style("font-size", "12px")
      .style("fill", "white")
      .attr("alignment-baseline", "middle");
  }, []);

  return (
    <div>
      <div
        style={{
          margin: "20px",
        }}
      >
        <svg width={800} height={700} id="barchart" ref={ref} />
        <div
          id="tooltip"
          style={{
            position: "absolute",
            display: "block",
            padding: "5px",
            //   background: "white",
            border: "1px solid #333",
            borderRadius: "5px",
            pointerEvents: "none",
          }}
        ></div>
      </div>
    </div>
  );
}

export default PerformanceMeasurementGraph2;
