import "./App.css";
import BarChart from "./components/BarChart";
import BarChart2 from "./components/BarChart2";
import PerformanceMeasurement from "./components/PerformanceMeasurement";

function App() {
  return (
    <div className="App">
      <PerformanceMeasurement/>
      <h1>Hello React + D3 world!</h1>
      <BarChart />
      <h1>Another chart</h1>
      <BarChart2 />
    </div>
  );
}

export default App;
