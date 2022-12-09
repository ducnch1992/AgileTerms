import React from "react";

export default function IconSuccessFailed({ isValid, message, id }) {

  return (
    <>
      {isValid ? (
        <i
          className="fa fa-check-circle my-auto ml-1 text-success"
          aria-hidden="true"
          hidden="true"
          id="userNameIcon"
        ></i>
      ) : (
        <i
          className="fa fa-times-circle my-auto ml-1 text-danger"
          aria-hidden="true"
          data-toggle="popover"
          data-trigger="focus"
          title={message}
          hidden="true"
          id={id}
        ></i>
      )}
    </>
  );
}
