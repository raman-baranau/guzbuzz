import React, { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import axios from "axios";
import { GAME_URL } from "App";


export function JoinComponent({ setAuth }) {
  const navigate = useNavigate();
  const location = useLocation();
  const attemptedRoomId = location.state ? location.state.attemptedRoomId : "";

  const [roomCode, setRoomCode] = useState(attemptedRoomId);
  const [name, setName] = useState('');
  const [joinMode, setJoinMode] = useState(true);
  const [loading, setLoading] = useState(false);

  const onSubmit = (e) => {
    e.preventDefault();

    if (joinMode) {
      enterRoom(roomCode);
    } else {
      makeRoom();
    }
  }

  async function makeRoom() {
    setLoading(true);
    try {
      const createRes = await createRoom();
      if (createRes.status !== 201) {
        throw new Error();
      }
      const roomID = createRes.data.id;
      await enterRoom(roomID, true);
    } catch (error) {
      setLoading(false);
      console.log(error);
    }
  }

  async function createRoom() {
    try {
      const response = axios.post(`${GAME_URL}/gameroom`);
      return response;
    } catch (error) {
      if (error.response) {
        return error.response;
      } else {
        return { status: 500 };
      }
    }
  }

  async function enterRoom(roomId, hosting = false) {
    if (!hosting) {
      setLoading(true);
    }
    try {
      const response = await axios.post(`${GAME_URL}/new-user`, {
        name: roomId + "-" + name
      });
      const auth = {
        roomId: roomId,
        playerName: response.data.name,
        token: response.data.token
      };
      setAuth(auth);
      setLoading(false);
      navigate(`/${roomId}`);
    } catch (error) {
      setLoading(false);
      if (error.response) {
        return error.response;
      } else {
        return { status: 500 };
      }
    }
  }

  return joinMode ? (
    <div className="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
      <form className="w-full max-w-sm mx-auto my-4 flex flex-col group" onSubmit={onSubmit} noValidate>
        <h3 className="w-full text-2xl font-medium font-bol text-slate-700 mb-4">Join a game</h3>
        <label className="block">
          <span className="block text-sm font-medium text-slate-700">Room Code</span>
          <input
            type="text"
            className="npt peer"
            value={roomCode}
            onChange={(e) => {
              setRoomCode(e.target.value);
            }}
            pattern="[A-Z]{4}"
            placeholder="WXYZ"
            required />
          <span className="mt-2 hidden text-sm text-pink-500 peer-[&:not(:placeholder-shown):invalid]:block">
            Room code should contain 4 uppercase letters
          </span>
          <span className="block text-sm font-medium text-slate-700 mt-4">Your Name</span>
          <input
            type="text"
            className="npt peer/name"
            value={name}
            onChange={(e) => {
              setName(e.target.value);
            }}
            pattern="\w{2,}"
            placeholder="PanKevinLalande"
            required />
          <span className="mt-2 hidden text-sm text-pink-500 peer-[&:not(:placeholder-shown):invalid]/name:block">
            Please enter at least 2 letters
          </span>
          <button className="w-full btn btn-blue mt-4 py-2 px-4 font-bold rounded group-invalid:pointer-events-none group-invalid:opacity-30" type="submit" disabled={loading}>
            {loading ? 'Joining...' : 'Join'}
          </button>
        </label>
      </form>
      <div className="relative flex py-5 items-center">
        <div className="flex-grow border-t border-slate-700"></div>
        <span className="flex-shrink mx-4 text-slate-700 font-medium text-sm">OR</span>
        <div className="flex-grow border-t border-slate-700"></div>
      </div>
      <button
        className="w-full btn btn-blue mt-4 py-2 px-4 font-bold rounded"
        type="button"
        onClick={
          () => {
            setJoinMode(false);
          }
        }>
        Create room
      </button>
    </div>
  ) : (
    <div className="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
      <form className="w-full max-w-sm mx-auto my-4 flex flex-col" onSubmit={onSubmit} noValidate>
        <h3 className="w-full text-2xl font-medium font-bol text-slate-700 mb-4">Host a game</h3>
        <label className="block">
          <span className="block text-sm font-medium text-slate-700 mt-4">Your Name</span>
          <input
            type="text"
            className="npt peer"
            value={name}
            onChange={(e) => {
              setName(e.target.value);
            }}
            pattern="\w{2,}"
            placeholder="PanKevinLalande"
            required />
          <span className="mt-2 hidden text-sm text-pink-500 peer-[&:not(:placeholder-shown):invalid]:block">
            Please enter at least 2 letters
          </span>
          <button className="w-full btn btn-blue mt-4 py-2 px-4 font-bold rounded" type="submit" disabled={loading}>
            {loading ? 'Creating...' : 'Host'}
          </button>
        </label>
      </form>
      <div className="relative flex py-5 items-center">
        <div className="flex-grow border-t border-slate-700"></div>
        <span className="flex-shrink mx-4 text-slate-700 font-medium text-sm">OR</span>
        <div className="flex-grow border-t border-slate-700"></div>
      </div>
      <button
        className="w-full btn btn-blue mt-4 py-2 px-4 font-bold rounded"
        type="button"
        onClick={
          () => {
            setJoinMode(true);
          }
        }>
        Enter room
      </button>
    </div>
  )
}
