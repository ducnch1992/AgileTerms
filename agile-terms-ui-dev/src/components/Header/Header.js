import React, { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Link, useNavigate } from "react-router-dom";
import logo1 from "../../assets/img/logo1.png";
import { authAction } from "../../store/reducer/auth";
import { loginSignupAction } from "../../store/reducer/loginSignup";
import { popularTermRecentTermAction } from "../../store/reducer/popularTermRecentTerm";
import "./Header.scss";

export default function Header() {
  const isAuthenticated = useSelector((state) => state.auth.isAuthenticated);
  const dispatch = useDispatch();
  const navigate = useNavigate();

  useEffect(() => {
    if (localStorage.getItem("token") !== null) {
      dispatch(authAction.login());
    }
  });
  const logoutHandle = () => {
    dispatch(authAction.logout());
    dispatch(loginSignupAction.login());
    localStorage.setItem("isLogin", "true");
    localStorage.removeItem("token");
    localStorage.removeItem("role");
    localStorage.removeItem("activate-status");
    localStorage.removeItem("author");
    navigate("/");
  };
  return (
    <header>
      <nav className="navbar navbar-expand justify-content-between">
        <a className="navbar-brand mb-auto " 
        href="/" >
          <img
            src={logo1}
            alt="agile-term-logo"
            id="agile-term-logo"
            className="my-auto"
          />
        </a>
        <div className="align-self-center">
          <a href="/" className="navbar__link">
            <h1 className="navbar__title">AgileTerms</h1>
          </a>
          <p className="navbar__slogan">
            Pragmatic software development dictionary
          </p>
        </div>
        {isAuthenticated ? (
          <>
            <button
              type="button"
              className="btn btn-login"
              onClick={logoutHandle}
            >
              Logout
            </button>
          </>
        ) : (
          <>
            <button
              type="button"
              className="btn btn-login"
              onClick={() => {
                navigate("/login");
                dispatch(loginSignupAction.login());
                localStorage.setItem("isLogin", "true");
              }}
            >
              <span>Login | Sign up</span>
            </button>
          </>
        )}
      </nav>
    </header>
  );
}
