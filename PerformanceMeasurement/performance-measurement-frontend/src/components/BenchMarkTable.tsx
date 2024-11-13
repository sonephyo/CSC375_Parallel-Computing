import { BenchMarkData } from "../data/dummyData";

interface BenchmarkData {
  score: number;
  scoreError: number;
  sampleCount: number;
  scoreUnit: string;
  min: number;
  max: number;
  standardDeviation: number;
  variance: number;
}

interface Benchmarks {
  [key: string]: BenchmarkData;
}

function BenchMarkTable({data}: {
    data: BenchMarkData
}) {
  // Helper function to render each benchmark's result data
  const renderBenchmarkRows = (benchmarks: Benchmarks) => {
    return Object.keys(benchmarks).map((benchmark) => {
      const {
        score,
        scoreError,
        sampleCount,
        scoreUnit,
        min,
        max,
        standardDeviation,
        variance,
      } = benchmarks[benchmark];

      return (
        <tr key={benchmark}>
          <td>{benchmark}</td>
          <td>{score.toFixed(2)}</td>
          <td>{scoreError.toFixed(2)}</td>
          <td>{sampleCount}</td>
          <td>{scoreUnit}</td>
          <td>{min.toFixed(2)}</td>
          <td>{max.toFixed(2)}</td>
          <td>{standardDeviation.toFixed(2)}</td>
          <td>{variance.toExponential(2)}</td>
        </tr>
      );
    });
  };
  console.log("====================================");
  console.log(data);
  console.log("====================================");
  return (
    <div>
      {Object.keys(data).map((mapName: string) => (
        <div key={mapName}>
          <h2>{mapName}</h2>
          <table border={1} style={{ width: "100%", marginBottom: "20px" }}>
            <thead>
              <tr>
                <th>Benchmark</th>
                <th>Score</th>
                <th>Score Error</th>
                <th>Sample Count</th>
                <th>Score Unit</th>
                <th>Min</th>
                <th>Max</th>
                <th>Standard Deviation</th>
                <th>Variance</th>
              </tr>
            </thead>
            <tbody>{renderBenchmarkRows(data[mapName].results)}</tbody>
          </table>
        </div>
      ))}
    </div>
  );
}

export default BenchMarkTable;
