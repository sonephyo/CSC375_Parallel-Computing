import { useState } from "react";
import {
  StompSessionProvider,
} from "react-stomp-hooks";
import { SubscribingComponent } from "./SubscribingComponent";
import { SendingMessages } from "./SendingMessages";

function App() {
  const [operationStatus, setOperationStatus] = useState<string>("end");

  return (
    <div>
      <StompSessionProvider url={"http://localhost:8080/ws-endpoint"}>
        <SendingMessages operationStatus={operationStatus} />
        <SubscribingComponent setOperationStatus={setOperationStatus} />
      </StompSessionProvider>
    </div>
  );
}

export default App;