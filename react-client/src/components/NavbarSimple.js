import React from "react";

function Logo({ size = 25 }) {
    return (
      <svg
        width={size}
        height={size}
        viewBox="0 0 95 95"
        fill="none"
        xmlns="http://www.w3.org/2000/svg"
      >
        <circle cx="47" cy="30" r="30" fill="#be123c" />
        {/* <circle cx="20" cy="75" r="20" fill="#4338ca" /> */}
        <rect x="10" y="75" width="75" height="20" rx="10" fill="#4338ca" />
      </svg>
    );
  }

export function NavbarSimple() {

  return (
    <div className='flex justify-between items-center h-24 max-w-[1240px] mx-auto px-4 bg-gradient-to-r from-cyan-500 to-blue-500'>
      <Logo /><h1 className='w-full text-3xl font-bold text-white ml-2'>Guzik - Buzzer App</h1>
    </div>
  );
}
