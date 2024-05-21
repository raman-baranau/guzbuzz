import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { GAME_URL } from "App";


export function JoinComponent({ setAuth }) {
  const navigate = useNavigate();
  const [roomCode, setRoomCode] = useState('');
  const [name, setName] = useState('');
  const [joinMode, setJoinMode] = useState(true);
  const [loading, setLoading] = useState(false);

  const handleSubmit = (e) => {
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
      const response = axios.post(`${GAME_URL}/gameroom`, {
        host: { name: name }
      });
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
    const auth = {
      playerName: name,
      roomId: roomId,
    };
    setAuth(auth);
    setLoading(false);
    navigate(`/${roomId}`);
  }

  return joinMode ? (
    <div className="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
      <form className="w-full max-w-sm mx-auto my-4 flex flex-col" onSubmit={handleSubmit}>
        <h3 className="w-full text-2xl font-medium font-bol text-slate-700 mb-4">Join a game</h3>
        <label className="block">
          <span className="block text-sm font-medium text-slate-700">Room Code</span>
          <input
            type="text"
            className="npt"
            value={roomCode}
            onChange={(e) => {
              setRoomCode(e.target.value);
            }} />
          <span className="block text-sm font-medium text-slate-700 mt-4">Your Name</span>
          <input
            type="text"
            className="npt"
            value={name}
            onChange={(e) => {
              setName(e.target.value);
            }} />
          <button className="w-full btn btn-blue mt-4 py-2 px-4 font-bold rounded" type="submit" disabled={loading}>
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
      <form className="w-full max-w-sm mx-auto my-4 flex flex-col" onSubmit={handleSubmit}>
        <h3 className="w-full text-2xl font-medium font-bol text-slate-700 mb-4">Host a game</h3>
        <label className="block">
          <span className="block text-sm font-medium text-slate-700 mt-4">Your Name</span>
          <input
            type="text"
            className="npt"
            value={name}
            onChange={(e) => {
              setName(e.target.value);
            }} />
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
