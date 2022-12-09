import React from "react";

function ShowHidePassword({ togglePassword, passwordType }) {
  return (
    <>
      {window.innerWidth <= 576 && (
        <span
          className="btn bg-transparent position-absolute"
          onTouchStart={togglePassword}
          onTouchEnd={togglePassword}
          onMouseDown={togglePassword}
          onMouseUp={togglePassword}
        >
          {passwordType === "password" ? (
            <i className="fa fa-eye-slash"></i>
          ) : (
            <i className="fa fa-eye"></i>
          )}
        </span>
      )}
    </>
  );
}
export default ShowHidePassword;
