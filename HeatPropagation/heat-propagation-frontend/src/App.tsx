import { useState } from "react";
import {
  StompSessionProvider,
  useStompClient,
  useSubscription,
} from "react-stomp-hooks";

function App() {
  const [operationStatus, setOperationStatus] = useState<string>("end");

  return (
    <div>
      <StompSessionProvider url={"http://localhost:8080/ws-endpoint"}>
        <SubscribingComponent setOperationStatus={setOperationStatus} />
        <SendingMessages operationStatus={operationStatus} />
      </StompSessionProvider>
    </div>
  );
}

export default App;

const SubscribingComponent = ({
  setOperationStatus,
}: {
  setOperationStatus: (status: string) => void;
}) => {

  useSubscription("/topic/reply", (message) => {
    const jsonData = JSON.parse(message.body);
    console.log('====================================');
    console.log(jsonData);
    console.log('====================================');
  });

  useSubscription("/topic/status", (message) => {
    const jsonData = JSON.parse(message.body);

    console.log('====================================');
    console.log(jsonData);
    console.log('====================================');
  });

  return (
    
    <div>Hello This is  Subscribing Component</div>
  );
};

const SendingMessages = ({ operationStatus }: { operationStatus: string }) => {

  const stompClient = useStompClient();

  const start = () => {
    if (stompClient) {
      stompClient.publish({
        destination: "/app/start",
        body: JSON.stringify({
          testData: "This is the testData sent from SendingMessages Component"
        }),
      });
    }
  };

  return (
    <div>
      <h2>This is the sendingMessage Componenet</h2>

      <button onClick={start}>Start!!!</button>
    </div>
  );
};
