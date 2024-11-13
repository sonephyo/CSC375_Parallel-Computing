import axios from "axios";
import "./App.css";
import PerformanceMeasurementGraph from "./components/PerformanceMeasurementGraph";
import { useEffect, useState } from "react";
import { BACKEND_URL } from "./constants";
import PerformanceMeasurementGraph2 from "./components/PerformanceMeasurementGraph2";
import ReactLoading from "react-loading";
import BenchMarkTable from "./components/BenchMarkTable";
import { BenchMarkData } from "./data/dummyData";

function App() {
  const [inProcess, setInProcess] = useState<boolean>(false);
  const [data, setdata] = useState<null | BenchMarkData>(null);

  const handleClick = (e: React.MouseEvent<HTMLButtonElement>) => {
    e.preventDefault();
    setInProcess(true);
    axios
      .get(BACKEND_URL + "/pm/start", { withCredentials: true })
      .then((response) => {
        console.log(response.data);
        setdata(response.data);
        setInProcess(false);
      })
      .catch((error) => {
        alert(error);
        setInProcess(false);
      });
  };

  useEffect(() => {
    const handleBeforeUnload = (event: BeforeUnloadEvent) => {
      if (inProcess) {
        event.preventDefault();
      }
    };

    window.addEventListener("beforeunload", handleBeforeUnload);

    return () => {
      window.removeEventListener("beforeunload", handleBeforeUnload);
    };
  }, [inProcess]);

  return (
    <div className="App">
      <div>
        <h1>JMH Benchmark</h1>
        <h2>
          Comparsion between customConcurrentHashMap and javaConcurrentHashMap
        </h2>
      </div>
      <div>
        <p>Starting comparing custom hashmap and java hashmap</p>
        {!inProcess && <button onClick={handleClick}>Start!!</button>}
        {inProcess && (
          <div
            style={{
              display: "flex",
              flexDirection: "column",
              alignItems: "center",
            }}
          >
            <ReactLoading type={"bars"} color="#fff" />
            <p>Do not close the browser. Still running</p>
          </div>
        )}
      </div>
      {data != null && (
        <div>
          <h3>
            JavaConcurrentHashMap vs CustomConcurrentHashMap logramthimic graph
            (10 entries)
          </h3>
          <PerformanceMeasurementGraph data={data} />
          <h3>
            JavaConcurrentHashMap vs CustomConcurrentHashMap logramthimic
            graph(1000 entries)
          </h3>
          <PerformanceMeasurementGraph2 data={data} />
          <BenchMarkTable data={data} />
        </div>
      )}
    </div>
  );
}

export default App;
