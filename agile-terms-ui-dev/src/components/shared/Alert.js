import React from "react";

export default function Alert({ classes, title, message, isCollapse }) {
  return (
    <div className={classes} role="alert">
      <strong>{title}</strong> {message}
      <button
        type="button"
        data-dismiss="alert"
        className="close"
        onClick={isCollapse}
      >
        <span aria-hidden="true">&times;</span>
      </button>
    </div>
  );
}
