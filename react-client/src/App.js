import './App.css';
import React, { useState } from "react";
import {
  StompSessionProvider,
  useSubscription,
  useStompClient
} from "react-stomp-hooks";

const SOCKET_URL = 'http://localhost:8080/ws-endpoint';

const App = () => {
  return (
    //Initialize Stomp connection, will use SockJS for http(s) and WebSocket for ws(s)
    //The Connection can be used by all child components via the hooks or hocs.
    <StompSessionProvider
      url={SOCKET_URL}
      //All options supported by @stomp/stompjs can be used here
    >
      <SubscribingComponent />
      <PublishComponent />
    </StompSessionProvider>
  );
};

function SubscribingComponent() {
  const [lastMessage, setLastMessage] = useState("No message received yet");

  //Subscribe to /topic/reply, and use handler for all received messages
  //Note that all subscriptions made through the library are automatically removed when their owning component gets unmounted.
  //If the STOMP connection itself is lost they are however restored on reconnect.
  //You can also supply an array as the first parameter, which will subscribe to all destinations in the array
  useSubscription("/topic/reply", (message) => setLastMessage(message.body));

  return (
    <div>Last Message: {lastMessage}</div>
  );
}

const PublishComponent = () => {
  const stompClient = useStompClient();
  const [name, setName] = useState('');
  
  const publishMessage = () => {
    if(stompClient) {
      stompClient.publish({destination: '/app/broadcast', body: name})
    }
  }

  const handleNameChange = (e) => {
    setName(e.target.value);
  }

  const handleSubmit = (e) => {
    publishMessage();
    e.preventDefault();
  }

  return (
    <form onSubmit={handleSubmit}>
        <label>
          What is your name?
          <input type="text" value={name} onChange={handleNameChange} />
        </label>
        <input type="submit" value="Submit" />
    </form>
  )
}

export default App;
