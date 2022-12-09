import React, { useRef, useState } from "react";
import { useSelector } from "react-redux";
import { Link } from "react-router-dom";
import Alert from "../../shared/Alert";
import ShowHidePassword from "../../shared/ShowHidePassword";

export default function LoginContent({
  isLoggedInRequired,
  isLoggedInFailed,
  handleLogin,
  handleCloseAlert,
}) {
  const emailRef = useRef("");
  const passwordRef = useRef("");
  const [isShowAlert, setIsShowAlert] = useState({
    loginRequired: true,
  });
  const [passwordType, setPasswordType] = useState("password");
  const [passwordInput, setPasswordInput] = useState("");
  const collapseHandle = () => {
    handleCloseAlert(false);
  };
  

  const handlePasswordChange = (e) => {
    setPasswordInput(e.target.value);
  };
  const togglePassword = () => {
    if (passwordType === "password") {
      setPasswordType("text");
      return;
    }
    setPasswordType("password");
  };
  const handleLoginInfo = (e) => {
    e.preventDefault();
    const email = emailRef.current.value;
    const password = passwordRef.current.value;
    const userData = {
      username: email,
      password: password,
    };

    handleLogin(userData);
  };
  const loginRequiredHandle = () => {
    setIsShowAlert((prev) => ({ ...prev, loginRequired: false }));
  };

  const isTokenExpired = useSelector(state => state.auth.isTokenExpired);
  
  return (
    <>
      {isShowAlert.loginRequired && isLoggedInRequired && (
        <Alert
          classes="alert alert-danger alert-dismissible fade show"
          
          title={isTokenExpired?"Session Timed Out":"Login Required"}

          isCollapse={loginRequiredHandle}
        />
      )}
      <p className="alert-light">
        Please enter your user name and password to login
      </p>
      <form onSubmit={handleLoginInfo} autoComplete="off">
        <div className="form-group mb-4">
          <input
            ref={emailRef}
            type="text"
            className="form-control"
            name="loginEmail"
            autoFocus
            id="loginEmail"
            aria-describedby="email"
            placeholder="Enter your user name"
          />
        </div>

        <div className="form-group position-relative d-flex flex-row justify-content-end">
          <input
            id="confirmPassword"
            name="confirmPassword"
            type={passwordType}
            ref={passwordRef}
            className="form-control"
            placeholder="Enter your password"
            onChange={handlePasswordChange}
            value={passwordInput}
            aria-describedby="password"
            autoComplete="new-password"
          />

          <ShowHidePassword
            togglePassword={togglePassword}
            passwordType={passwordType}
            passwordInput={passwordInput}
            setPasswordInput={setPasswordInput}
          />
        </div>
        <Link to="/underconstruction">Forgot password?</Link>
        {isLoggedInFailed && (
          <Alert
            classes="alert alert-danger alert-dismissible fade show"
            title="Login Failed!"
            message="Wrong user name or password"
            isCollapse={collapseHandle}
          />
        )}

        <button type="submit" className="btn btn-block mt-4 mb-5 login-btn">
          Login
        </button>
      </form>
    </>
  );
}
