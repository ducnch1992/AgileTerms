import React from "react";

export default function ToggleVisibility({ displayResult, children }) {
  return <>{displayResult && children}</>;
}
