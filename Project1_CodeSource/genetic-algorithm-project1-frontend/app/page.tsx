"use client"

import React, {useState} from "react";
import {StompSessionProvider, useStompClient, useSubscription} from "react-stomp-hooks";

export default function Home() {
    // const demo_facility_layout = [
    //     [0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 0, 0, 0, 2, 2],
    //     [0, 0, 0, 3, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 2, 0],
    //     [0, 3, 0, 3, 0, 0, 0, 0, 0, 3, 0, 1, 1, 2, 2, 0, 0, 3, 0, 0, 0, 0, 0, 0],
    //     [0, 3, 0, 3, 0, 0, 0, 0, 0, 3, 0, 1, 1, 2, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0],
    //     [0, 3, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 3, 0, 0, 3, 0, 0, 0, 0, 0, 0],
    //     [0, 0, 0, 0, 0, 0, 3, 0, 0, 4, 0, 0, 0, 0, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0],
    //     [0, 0, 0, 2, 2, 0, 3, 0, 4, 4, 4, 0, 0, 0, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0],
    //     [0, 0, 0, 2, 2, 2, 3, 0, 0, 0, 0, 0, 1, 1, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0],
    //     [0, 2, 2, 0, 2, 0, 0, 0, 0, 0, 0, 0, 1, 1, 2, 2, 0, 0, 0, 0, 0, 3, 0, 0],
    //     [0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 2, 0, 0, 2, 2, 0, 0, 3, 0, 0],
    //     [0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 3, 0, 0, 0, 0, 2, 0, 3, 0, 3, 0, 0],
    //     [0, 0, 0, 0, 0, 0, 3, 4, 4, 4, 0, 0, 3, 0, 3, 0, 0, 0, 0, 3, 0, 0, 0, 0],
    //     [0, 2, 2, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 3, 3, 0, 0, 0, 3, 0, 4, 0, 0],
    //     [0, 2, 0, 0, 0, 0, 3, 0, 3, 0, 4, 0, 0, 0, 3, 3, 0, 0, 0, 0, 4, 4, 4, 0],
    //     [0, 0, 0, 0, 0, 0, 0, 0, 3, 4, 4, 4, 0, 0, 0, 3, 0, 0, 0, 1, 1, 0, 0, 0],
    //     [0, 0, 0, 3, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 1, 1, 0, 4, 0],
    //     [0, 0, 0, 3, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 4, 4, 4, 0, 0, 4, 4, 4],
    //     [0, 4, 0, 3, 0, 0, 3, 0, 1, 1, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0],
    //     [4, 4, 4, 0, 0, 0, 3, 0, 0, 4, 4, 4, 1, 1, 0, 0, 0, 0, 0, 0, 4, 4, 4, 0],
    //     [3, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 4, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
    //     [3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 4, 4, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0],
    //     [3, 3, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 3, 2, 2, 0, 0, 0],
    //     [0, 3, 4, 4, 4, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 3, 2, 0, 2, 2, 0],
    //     [0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0],
    // ];
    //

    // function getData(url: string | URL | Request) {
    //     // Perform GET request
    //     return fetch(url, {
    //         method: 'GET', // Specifies GET request
    //         headers: {}
    //     })
    //         .then(response => {
    //             if (!response.ok) {
    //                 throw new Error(`HTTP error! Status: ${response.status}`);
    //             }
    //             return response;// Parse response as JSON
    //         })
    // }


    return (
        <div>
            <StompSessionProvider
                url={"http://localhost:8080/ws-endpoint"}
            >
                <SubscribingComponent/>
                <SendingMessages/>
            </StompSessionProvider>
        </div>
    )
}

const SubscribingComponent = () => {
    const [data, setData] = useState<null | Array<Array<number>>>(null);

    const colorPicker = (number: number) => {
        switch(number) {
            case 0:
                return "bg-violet-500"
            case 1:
                return "bg-pink-500"
            case 2:
                return "bg-orange-500"
            case 3:
                return "bg-red-500"
            case 4:
                return "bg-blue-500"
            default:
                return "bg-gray-800"
        }
    }

    useSubscription("/topic/reply", message => {
        // console.log(message);
        const jsonData = JSON.parse(message.body);
        setData(jsonData.data)
        // console.log(data)
    })
    return (
        <div>
            {data ? data.map((list, index) => (
                <div key={index} className="flex flex-row justify-center">
                    {list.map((item, itemIndex) => (
                        <div key={itemIndex}
                             className={`w-5 h-5 flex justify-center items-center ${colorPicker(item)} border-2 border-black`}>
                            <p>{item}</p>
                        </div>
                    ))}
                </div>
            )) : "Empty"}
        </div>
    )
}

const SendingMessages = () => {

    const stompClient = useStompClient();

    const start = () => {
        if (stompClient) {
            console.log("Event started")
            stompClient.publish({
                destination: "/app/start",
            })
        }
    }

    const stop = () => {
        if (stompClient) {
            console.log("Event stopped")
            stompClient.publish({
                destination: "/app/stop",
            })
        }
    }


    return (<div style={{
        display: "flex",
        flexDirection: "column",
    }}>
        <button onClick={start}>Start Message</button>
        <button onClick={stop}>Stop Message</button>
    </div>)
}
