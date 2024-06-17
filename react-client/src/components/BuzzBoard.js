import { WS_ENDPOINT } from "App";
import React, { useState } from "react";
import { Navigate, useNavigate, useParams } from "react-router-dom";
import {
  StompSessionProvider,
  useStompClient,
  useSubscription,
} from "react-stomp-hooks";


export default function BuzzBoard({ auth, setAuth }) {
  const { id } = useParams();
  const navigate = useNavigate();

  return (
    <>
      {id && auth.roomId === id && auth.playerName && auth.token
       ? (<StompSessionProvider
          url={WS_ENDPOINT}
          debug={(str) => {
            console.log(str);
          }}
          connectHeaders={{
            "gameRoomId": auth.roomId,
            "playerName": auth.playerName,
            "token": auth.token
          }}
          onStompError={(frame) => {
            console.log("Broker reported error: " + frame.headers["message"]);
            console.log("Additional details: " + frame.body);
          }}
          onWebSocketClose={(frame) => {
            console.log("WS close details: " + frame.body);
            navigate(`/`);
          }}
        >
        <Board auth={auth} setAuth={setAuth} />
      </StompSessionProvider>) 
      : <Navigate to="/" state={{attemptedRoomId: id}}/>}
    </>
  );
}

function Board({ auth, setAuth }) {
  const [connectedUsers, setConnectedUsers] = useState(new Map());
  const [buzzed, setBuzzed] = useState(false);
  const [isHost, setIsHost] = useState(false);

  const stompClient = useStompClient();

  const processConnectedUsers = (message) => {
    let users = JSON.parse(message.body);
    let map = new Map();
    for (let u of users) {
      map.set(u.name, false);
    }
    setConnectedUsers(map);
  }

  const resetBuzzes = () => {
    setBuzzed(false);
    const m = new Map();
    for (const user of connectedUsers.keys()) {
      m.set(user, false);
    }
    setConnectedUsers(m);
  }

  useSubscription("/app/host", (message) => {
    let hostUser = JSON.parse(message.body);
    setIsHost(hostUser.name === auth.playerName);
  });

  useSubscription("/app/connected.users", processConnectedUsers);

  useSubscription("/topic/" + auth.roomId + ".connected.users", processConnectedUsers);

  useSubscription("/topic/" + auth.roomId + ".buzzes", (message) => {
    let buzz = JSON.parse(message.body);
    if (buzz.buzzType === "BUZZ") {
      setConnectedUsers(new Map(connectedUsers.set(buzz.playerName, true)));
    } else {
      resetBuzzes();
    }
  });

  const handleBuzz = (e) => {
    e.preventDefault();
    if (stompClient) {
      stompClient.publish({
        destination: "/app/buzz",
        body: JSON.stringify({ buzzType: "BUZZ" })
      });
      setBuzzed(true);
    } else {
      //TODO proper processing
      console.log("No stomp client provided - " + stompClient);
    }
  }

  const sendResetBuzzes = () => {
    if (stompClient) {
      stompClient.publish({
        destination: "/app/buzz",
        body: JSON.stringify({ buzzType: "RESET" })
      });
    } else {
      //TODO proper processing
      console.log("No stomp client provided - " + stompClient);
    }
  }

  return (
    <div className="mt-10 pb-10 sm:mx-auto sm:w-full sm:max-w-sm text-center text-xl">
      <p>Room {auth.roomId}</p>
      <div className="mt-5 mb-5">
        <button
          className={"w-80 h-80 text-7xl font-bold rounded-full " + (buzzed ? "btn-gray" : "btn-blue")}
          onClick={handleBuzz}
          disabled={buzzed}>
          {buzzed ? "Buzzed" : "Buzz"}
        </button>
      </div>
      {isHost ? (
        <div>
          <button
            className={([...connectedUsers.keys()].filter(a => connectedUsers.get(a)).length > 0 ? "btn-blue-out" : "btn-trp")}
            onClick={sendResetBuzzes}
            disabled={[...connectedUsers.keys()].filter(a => connectedUsers.get(a)).length === 0}>
            Reset all buzzers
          </button>
        </div>
      ) : null}
      <div className="relative flex py-5 items-center">
        <div className="flex-grow border-t border-slate-400"></div>
      </div>
      <div className="mt-5">
        <p>Players Buzzed</p>
        <ul className="text-2xl font-semibold">
          {
            [...connectedUsers.keys()].filter(a => connectedUsers.get(a)).map(user => (
              <li>{user}</li>
            ))
          }
        </ul>
      </div>
      <div className="mt-10">
        <p>Other Players</p>
        <ul className="text-2xl font-semibold">
          {
            [...connectedUsers.keys()].filter(a => !connectedUsers.get(a)).map(user => (
              <li>{user}</li>
            ))
          }
        </ul>
      </div>
    </div>
  );
}
