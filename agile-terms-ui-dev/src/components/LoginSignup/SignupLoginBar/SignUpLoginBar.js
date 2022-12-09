import React, { useState } from "react";
import { useDispatch } from "react-redux";
import { loginSignupAction } from "../../../store/reducer/loginSignup";

export default function SignUpLoginBar({ isLogin, handleIsLogin }) {
  const dispatch = useDispatch();
  const [isToggle, setIsToggle] = useState(false);

  const togglePill = (e) => {
    e.preventDefault();

    const currentActivePill = document.querySelector(".active-pill");
    currentActivePill.classList.remove("active-pill");

    document.querySelector(`#${e.target.id}`).classList.add("active-pill");

    setIsToggle(!isToggle);
  };

  return isLogin ? (
    <div className="login-signup-bar">
      <div
        className="pill login-pill active-pill"
        id="login-pill"
        onClick={(e) => {
          togglePill(e);
          dispatch(loginSignupAction.login());

          localStorage.setItem("isLogin", "true");
        }}
      >
        Login
      </div>

      <div
        className="pill signup-pill "
        id="signup-pill"
        onClick={(e) => {
          dispatch(loginSignupAction.signup());
          togglePill(e);
          localStorage.setItem("isLogin", "false");
        }}
      >
        Sign up
      </div>
    </div>
  ) : (
    <div className="login-signup-bar">
      <div
        className="pill login-pill "
        id="login-pill"
        onClick={(e) => {
          handleIsLogin();
          togglePill(e);
          dispatch(loginSignupAction.login());
          localStorage.setItem("isLogin", "true");
        }}
      >
        Login
      </div>

      <div
        className="pill signup-pill active-pill"
        id="signup-pill"
        onClick={(e) => {
          dispatch(loginSignupAction.signup());
          togglePill(e);
          localStorage.setItem("isLogin", "false");
        }}
      >
        Sign up
      </div>
    </div>
  );
}
