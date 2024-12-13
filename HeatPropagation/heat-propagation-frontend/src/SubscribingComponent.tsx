import { useState } from "react";
import { useSubscription } from "react-stomp-hooks";
import Heatmap from "./HeatMap";

export const SubscribingComponent = ({
  setOperationStatus,
}: {
  setOperationStatus: (status: string) => void;
}) => {
  const [metalAlloy, setmetalAlloy] = useState<null | number[][]>(null);

  useSubscription("/topic/reply", (message) => {
    const jsonData = JSON.parse(message.body);
    console.log("====================================");
    console.log(jsonData);
    console.log("====================================");
    setmetalAlloy(jsonData.metalAlloyData);
  });

  useSubscription("/topic/status", (message) => {
    const jsonData = JSON.parse(message.body);

    console.log("====================================");
    console.log(jsonData);
    console.log("====================================");
  });

  return (
    // <div>
    //   <div className="flex flex-col items-center">
    //     {metalAlloy && (
    //       <div className="flex flex-col">
    //         {metalAlloy.map((row, rowIndex) => (
    //           <div key={rowIndex} className="flex flex-row">
    //             {row.map((item, colIndex) => (
    //               <p
    //                 className={
    //                   "w-14 h-14 flex flex-row justify-center items-center border-2 "
    //                 }
    //                 key={colIndex}
    //               >
    //                 {item.toFixed(1)}
    //               </p>
    //             ))}
    //           </div>
    //         ))}
    //       </div>
    //     )}
    //   </div>
    // </div>
    <div>
      {metalAlloy && <Heatmap data={metalAlloy} />}
      {metalAlloy && (
        <table className="table-auto border-collapse border border-gray-300 w-full text-sm">
          <thead>
            <tr className="bg-gray-100">
              {metalAlloy[0].map((_, index) => (
                <th
                  key={index}
                  className="border border-gray-300 px-4 py-2 text-left"
                >
                  Column {index + 1}
                </th>
              ))}
            </tr>
          </thead>
          <tbody>
            {metalAlloy.map((row, rowIndex) => (
              <tr
                key={rowIndex}
                className={rowIndex % 2 === 0 ? "bg-white" : "bg-gray-50"}
              >
                {row.map((value, colIndex) => (
                  <td
                    key={colIndex}
                    className="border border-gray-300 px-4 py-2 text-right"
                  >
                    {value.toFixed(2)}
                  </td>
                ))}
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};
