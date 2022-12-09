import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate, useSearchParams } from "react-router-dom";
import LoginContent from "../../components/LoginSignup/LoginContent/LoginContent";
import SignupContent from "../../components/LoginSignup/SignupContent/SignupContent";
import SignUpLoginBar from "../../components/LoginSignup/SignupLoginBar/SignUpLoginBar";
import Loading from "../../components/shared/Loading";
import service from "../../services/Service";
import { authAction } from "../../store/reducer/auth";
import { loginSignupAction } from "../../store/reducer/loginSignup";
import "./LoginPage.scss";

export default function SignUpLoginPage() {
  const [isUserAlreadyLoggedIn, setIsUserAlreadyLoggedIn] = useState(true);
  const isLogin = useSelector((state) => state.loginSignup.isLogin);
  const [param] = useSearchParams();
  const returnUrl = param.get("return");
  const [isLoggedInRequired, setIsLoggedInRequired] = useState(
    Boolean(returnUrl)
  );
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const [isLoggedInFailed, setIsLoggedInFailed] = useState(false);
  const handleCloseAlert = (value) => {
    setIsLoggedInFailed(value);
  };
  const handleIsLogin = () => {
    setIsLoggedInFailed(false);
  };
  const handleLogin = (userInput) => {
    service
      .login(userInput)
      .then((response) => {
        if (response.status === 200) {
          localStorage.setItem("role", response.data.roles);
          localStorage.setItem("token", response.data.token);
          localStorage.setItem("author", response.data.username);
          localStorage.setItem("activate-status", response.data.isActive);
          setTimeout(()=>{
            dispatch(authAction.renewToken());
          },100)
          if (response.data.roles.includes("ROLE_ADMIN")) {
            navigate("/admin-page");
          } else if (null != returnUrl) {
            navigate(returnUrl);
          } else {
            navigate(-1);
          }
          dispatch(authAction.login());
        }
      })
      .catch((e) => {
        setIsLoggedInFailed(true);
      });
  };

  useEffect(() => {
    if (
      localStorage.getItem("role") === null ||
      localStorage.getItem("token") === null
    ) {
      setTimeout(() => {
        setIsUserAlreadyLoggedIn(false);
        if (localStorage.getItem("isLogin") === "true") {
          dispatch(loginSignupAction.login());
        } else {
          dispatch(loginSignupAction.signup());
          setIsLoggedInFailed(false);
        }
      }, 400);
    } else
      setTimeout(() => {
        navigate(-1);
      }, 400);
  }, []);
  return isUserAlreadyLoggedIn ? (
    <Loading />
  ) : (
    <section className="login-content">
      <div className="container py-3 col-12 col-md-8 col-lg-8 mt-4 ">
        <SignUpLoginBar isLogin={isLogin} handleIsLogin={handleIsLogin} />
        {isLogin ? (
          <>
            <LoginContent
              isLoggedInRequired={isLoggedInRequired}
              handleLogin={handleLogin}
              isLoggedInFailed={isLoggedInFailed}
              handleCloseAlert={handleCloseAlert}
            />
          </>
        ) : (
          <>
            <SignupContent />
          </>
        )}
      </div>
    </section>
  );
}
