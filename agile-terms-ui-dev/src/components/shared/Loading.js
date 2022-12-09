import React from "react";
import ReactLoading from "react-loading";

export default function Loading() {
  return (
    <div className="vh-100 container bg-white justify-content-center d-flex align-items-center">
      <ReactLoading
        type="spin"
        color="#02a9f7"
        height={"17rem"}
        width={"17rem"}
      />
    </div>
  );
}
