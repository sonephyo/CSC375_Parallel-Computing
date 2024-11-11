import axios from "axios";
import { BACKEND_URL } from "../constants";

function PerformanceMeasurement() {
  const handleClick = (e: React.MouseEvent<HTMLButtonElement>) => {
    axios
      .get(BACKEND_URL + "/pm/start", {withCredentials: true})
      .then((response) => {
        console.log(response.data);
      })
      .catch((error) => {
        console.log(error);
      });
  };

  return (
    <div>
      <h1>Starting comparing custom hashmap and java hashmap</h1>
      <button onClick={handleClick}>Start!!</button>
    </div>
  );
}

export default PerformanceMeasurement;
