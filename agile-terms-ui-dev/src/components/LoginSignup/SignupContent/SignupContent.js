import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router";
import service from "../../../services/Service";
import PasswordLabel from "../../shared/PasswordLabel";
import ShowHidePassword from "../../shared/ShowHidePassword";

import {
  checkConfirmPassword,
  emailChecker,
  handleCheckUserName,
  handleCheckValidPassword,
  setOutlineStyle,
} from "./SignupValidator";

export default function SignupContent() {
  const [passwordType, setPasswordType] = useState("password");
  const [confirmPasswordType, setConfirmPasswordType] = useState("password");
  const [userNameMessage, setUserNameMessage] = useState("User name is blank");

  const [formInfo, setFormInfo] = useState({
    userName: "",
    userEmail: "",
    passwordInput: "",
    confirmPasswordInput: "",
    isFormUserNameValid: false,
    isFormUserEmailValid: false,
    isFormPasswordValid: false,
    isFormConfirmPasswordValid: false,
  });
  const [isSubmit, setIsSubmit] = useState(false);

  const [isSignUpHaveError, setIsSignUpHaveError] = useState(true);
  const [errorMessage, setErrorMessage] = useState({
    userNameErr: "User name is blank",
    userEmailErr: "Email is blank",
    emailResponseStatus: 204,
    passwordErr: "Invalid password",
    confirmPasswordErr: "empty",
  });
  const navigate = useNavigate();
  const path = "users/signup";

  const handleUserNameChange = (e) => {
    setFormInfo({
      ...formInfo,
      userName: document.querySelector("#userName").value,

      isFormUserNameValid: handleCheckUserName(
        document.querySelector("#userName").value
      ).status
    });

    setUserNameMessage(
      handleCheckUserName(document.querySelector("#userName").value).message
    );
    if (userNameMessage !== "Empty") {
      setErrorMessage({
        ...errorMessage,
        userNameErr: userNameMessage,
      });
    }

    if (!formInfo.isFormUserNameValid) {
      setOutlineStyle("userName", false);
      document.querySelector("#userNameSpan").classList.add("text-danger");
    } else {
      document.querySelector("#userNameSpan").classList.remove("text-danger");
    }
  };

  const checkExistedUserName = async () => {
    if (formInfo.isFormUserNameValid) {
      await service
        .validateUserName(formInfo.userName)
        .then((response) => {
          if (formInfo.isFormUserNameValid) {
            setIsSignUpHaveError(false);
            setErrorMessage({
              ...errorMessage,
              userNameErr: "Empty",
            });
            document
              .querySelector("#userNameSpan")
              .classList.remove("text-danger");
          }
        })
        .catch((err) => {
          if (formInfo.userName.trim() === "") {
            setErrorMessage({
              ...errorMessage,
              userNameErr: "User name is blank",
            });
            setOutlineStyle("userName", false);
            document
              .querySelector("#userNameSpan")
              .classList.add("text-danger");
          } else {
            setErrorMessage({
              ...errorMessage,
              userNameErr: err.response.data,
            });
            document
              .querySelector("#userNameSpan")
              .classList.add("text-danger");
          }
          setFormInfo({
            ...formInfo,
            isFormUserNameValid: false,
          });
          setIsSignUpHaveError(true);
          document.querySelector("#userNameSpan").classList.add("text-danger");
        });

      setIsSubmit(false);
    }
  };

  const handleEmailChange = (e) => {
    setFormInfo({
      ...formInfo,
      userEmail: document.querySelector("#userEmail").value,
      isFormUserEmailValid: emailChecker(
        document.querySelector("#userEmail").value
      ),
    });

    if (!formInfo.isFormUserEmailValid) setIsSignUpHaveError(true);

    if (
      !formInfo.isFormUserEmailValid &&
      document.querySelector("#userEmail").value.trim() === ""
    ) {
      setErrorMessage({
        ...errorMessage,
        userEmailErr: "Email is blank",
        emailResponseStatus: 204,
      });
      setOutlineStyle("userEmail", false);
      document.querySelector("#userEmailSpan").classList.add("text-danger");
    } else if (
      !formInfo.isFormUserEmailValid &&
      document.querySelector("#userEmail").value !== "" &&
      errorMessage.emailResponseStatus !== 401
    ) {
      setErrorMessage({
        ...errorMessage,
        userEmailErr: "Wrong email format",
        emailResponseStatus: 204,
      });
      setOutlineStyle("userEmail", false);
      document.querySelector("#userEmailSpan").classList.add("text-danger");
    } else if (errorMessage.emailResponseStatus === 401) {
      if (errorMessage.userEmailErr === "Existed email") {
        document
          .querySelector("#userEmailSpan")
          .classList.remove("text-danger");
      } else {
        setErrorMessage({
          ...errorMessage,
          userEmailErr: "Existed email",
        });
      }
      setOutlineStyle("userEmail", false);
      document.querySelector("#userEmailSpan").classList.add("text-danger");
    }

    if (formInfo.isFormUserEmailValid) {
      setErrorMessage({
        ...errorMessage,
        emailResponseStatus: 204,
      });
      document.querySelector("#userEmailSpan").classList.remove("text-danger");
      setOutlineStyle("userEmail", formInfo.isFormUserEmailValid);
    }
  };

  const checkExistedEmail = async () => {
    if (formInfo.isFormUserEmailValid) {
      await service
        .validateEmail(formInfo.userEmail)
        .then((response) => {
          if (formInfo.isFormUserEmailValid) {
            setIsSignUpHaveError(false);
            setErrorMessage({
              ...errorMessage,
              userEmailErr: "Empty",
              emailResponseStatus: response.status,
            });
            document
              .querySelector("#userEmailSpan")
              .classList.remove("text-danger");
          }
        })
        .catch((err) => {
          if (formInfo.userEmail === "") {
            setErrorMessage({
              ...errorMessage,
              userEmailErr: "Email is blank",
              emailResponseStatus: 204,
            });
            document
              .querySelector("#userEmailSpan")
              .classList.add("text-danger");
          } else {
            setErrorMessage({
              ...errorMessage,
              userEmailErr: err.response.data,
              emailResponseStatus: err.response.status,
            });
            document
              .querySelector("#userEmailSpan")
              .classList.add("text-danger");
          }

          setFormInfo({
            ...formInfo,
            isFormUserEmailValid: false,
          });
          setIsSignUpHaveError(true);
        });
      setIsSubmit(false);
    } else {
      setErrorMessage({
        ...errorMessage,
        emailResponseStatus: 204,
      });
      document.querySelector("#userEmailSpan").classList.add("text-danger");
      setFormInfo({
        ...formInfo,
        isFormUserEmailValid: false,
      });
      setIsSignUpHaveError(true);
      setIsSubmit(false);
    }
  };

  const handlePasswordChange = (e) => {
    setFormInfo({
      ...formInfo,
      passwordInput: document.querySelector("#passwordInput").value,
      isFormPasswordValid: handleCheckValidPassword(
        document.querySelector("#passwordInput").value
      ),
      isFormConfirmPasswordValid:
        formInfo.isFormPasswordValid &&
        checkConfirmPassword(
          document.querySelector("#passwordInput").value,
          formInfo.confirmPasswordInput
        ),
    });

    if (formInfo.isFormPasswordValid) {
      setOutlineStyle(
        "confirmPasswordInput",
        formInfo.isFormConfirmPasswordValid
      );
      if (
        !checkConfirmPassword(
          document.querySelector("#passwordInput").value,
          formInfo.confirmPasswordInput
        )
      ) {
        setErrorMessage({
          ...errorMessage,
          confirmPasswordErr: "Password confirmation does not match",
        });
        setIsSubmit(false);
        document
          .querySelector("#userConfirmPasswordSpan")
          .classList.add("text-danger");
      }
    } else {
      setOutlineStyle("confirmPasswordInput", false);
      setErrorMessage({
        ...errorMessage,
        passwordErr: "Invalid password",
        confirmPasswordErr: "empty",
      });
      document.querySelector("#userPasswordSpan").classList.add("text-danger");
      setIsSubmit(false);

      document
        .querySelector("#userConfirmPasswordSpan")
        .classList.remove("text-danger");
      setIsSubmit(false);
    }

    if (formInfo.confirmPasswordInput === "") {
      setErrorMessage({
        ...errorMessage,
        passwordErr: "Invalid password",
        confirmPasswordErr: "Password confirmation does not match",
      });
      document.querySelector("#userPasswordSpan").classList.add("text-danger");
    }

    if (formInfo.isFormPasswordValid) {
      document
        .querySelector("#userPasswordSpan")
        .classList.remove("text-danger");

      if (formInfo.isFormConfirmPasswordValid) {
        document
          .querySelector("#userConfirmPasswordSpan")
          .classList.remove("text-danger");
      }
    }
    setOutlineStyle("passwordInput", formInfo.isFormPasswordValid);
  };

  const handleConfirmPasswordChange = (e) => {
    setFormInfo({
      ...formInfo,
      confirmPasswordInput: document.querySelector("#confirmPasswordInput")
        .value,
      isFormConfirmPasswordValid:
        checkConfirmPassword(
          formInfo.passwordInput,
          document.querySelector("#confirmPasswordInput").value
        ) && formInfo.isFormPasswordValid,
    });

    if (!formInfo.isFormConfirmPasswordValid) {
      if (formInfo.isFormPasswordValid) {
        setErrorMessage({
          ...errorMessage,
          confirmPasswordErr: "Password confirmation does not match",
        });
        setIsSubmit(false);
        document
          .querySelector("#userConfirmPasswordSpan")
          .classList.add("text-danger");
      } else {
        setErrorMessage({
          ...errorMessage,
          confirmPasswordErr: "Empty",
        });
        setIsSubmit(false);
        document
          .querySelector("#userConfirmPasswordSpan")
          .classList.remove("text-danger");
      }
    } else {
      setErrorMessage({
        ...errorMessage,
        confirmPasswordErr: "Empty",
      });
      setIsSubmit(false);
      document
        .querySelector("#userConfirmPasswordSpan")
        .classList.remove("text-danger");
    }
  };

  const togglePassword = () => {
    passwordType === "password"
      ? setPasswordType("text")
      : setPasswordType("password");
  };

  const toggleConfirmPassword = () => {
    confirmPasswordType === "password"
      ? setConfirmPasswordType("text")
      : setConfirmPasswordType("password");
  };

  useEffect(() => {
    if (formInfo.userName !== "")
      setOutlineStyle("userName", formInfo.isFormUserNameValid);

    if (formInfo.userEmail !== "")
      setOutlineStyle("userEmail", formInfo.isFormUserEmailValid);

    if (formInfo.passwordInput !== "")
      setOutlineStyle("passwordInput", formInfo.isFormPasswordValid);

    if (formInfo.confirmPasswordInput !== "")
      setOutlineStyle(
        "confirmPasswordInput",
        formInfo.isFormConfirmPasswordValid
      );
    if (
      formInfo.isFormUserNameValid &&
      formInfo.isFormUserEmailValid &&
      formInfo.isFormPasswordValid &&
      formInfo.isFormConfirmPasswordValid
    ) {
      document.querySelector("#submitButton").removeAttribute("disabled");
    } else document.querySelector("#submitButton").setAttribute("disabled", "");

    if (
      isSubmit &&
      formInfo.isFormUserNameValid &&
      formInfo.isFormUserEmailValid &&
      formInfo.isFormPasswordValid &&
      formInfo.isFormConfirmPasswordValid &&
      errorMessage.userEmailErr === "Empty" &&
      errorMessage.userNameErr === "Empty" &&
      !isSignUpHaveError
    ) {
      const userRequest = {
        userName: formInfo.userName,
        password: formInfo.passwordInput,
        matchingPassword: formInfo.confirmPasswordInput,
        email: formInfo.userEmail,
      };

      service.create(path, userRequest).then(
        (response) => {
          setIsSubmit(false);
        },
        (err) => {
          setIsSignUpHaveError(true);
          setIsSubmit(false);
        }
      );

      if (
        formInfo.isFormUserNameValid &&
        formInfo.isFormUserEmailValid &&
        formInfo.isFormPasswordValid &&
        formInfo.isFormConfirmPasswordValid &&
        errorMessage.userEmailErr === "Empty" &&
        errorMessage.userNameErr === "Empty" &&
        !isSignUpHaveError
      ) {
        navigate("/sign-up-confirm");
      }
    }
  }, [
    formInfo.confirmPasswordInput,
    formInfo.passwordInput,
    formInfo.userEmail,
    formInfo.userName,
    formInfo.isFormUserNameValid,
    formInfo.isFormUserEmailValid,
    formInfo.isFormPasswordValid,
    formInfo.isFormConfirmPasswordValid,
    isSignUpHaveError,
    errorMessage,
    errorMessage.userEmailErr,
    errorMessage.userNameErr,
    isSubmit,
    navigate,
  ]);

  const onSubmitFormHandler = async (e) => {
    e.preventDefault();
    handleUserNameChange();
    handleEmailChange();
    handlePasswordChange();
    handleConfirmPasswordChange();

    if (
      formInfo.userEmail !== "" &&
      formInfo.userName !== "" &&
      formInfo.passwordInput !== "" &&
      formInfo.confirmPasswordInput !== ""
    ) {
      await checkExistedEmail();
      await checkExistedUserName();
    }

    setIsSubmit(true);
  };

  return (
    <>
      <form id="form-reset" onSubmit={onSubmitFormHandler}>
        <p className="text-secondary">
          Please create account to add/edit content
        </p>
        {/* 
                  ================ USR NAME =====================
        */}
        <div className="form-group d-flex flex-column justify-content-end mt-1 p-0 mb-2">
          <input
            type="text"
            className="form-control"
            aria-describedby="email"
            autoComplete="new-password"
            name="userName"
            id="userName"
            placeholder="User name"
            value={formInfo.userName}
            onChange={handleUserNameChange}
            onKeyUp={handleUserNameChange}
            onFocus={handleUserNameChange}
            onBlur={checkExistedUserName}
          />
          <span
            className="position-relative text-left text-white user-select-none "
            style={{ fontSize: "12px" }}
            id="userNameSpan"
          >
            {errorMessage.userNameErr}
          </span>
        </div>

        {/* 
                  ================ USR EMAIL =====================
        */}
        <div className="form-group d-flex flex-column justify-content-end p-0 mb-2">
          <input
            type="text"
            className="form-control"
            aria-describedby="email"
            autoComplete="new-password"
            name="userEmail"
            id="userEmail"
            placeholder="User email"
            value={formInfo.userEmail}
            onChange={handleEmailChange}
            onKeyUp={handleEmailChange}
            onFocus={handleEmailChange}
            onBlur={checkExistedEmail}
          />
          <span
            className="position-relative text-left text-white user-select-none"
            style={{ fontSize: "12px" }}
            id="userEmailSpan"
          >
            {errorMessage.userEmailErr}
          </span>
        </div>

        {/* 
                  ================ USR PASSWORD =====================
        */}
        <div className="form-group position-relative d-flex flex-column justify-content-end p-0 mb-2">
          <div className="form-group position-relative d-flex flex-row justify-content-end mb-0 p-0">
            <input
              className="form-control"
              aria-describedby="password"
              autoComplete="new-password"
              id="passwordInput"
              name="passwordInput"
              placeholder="Password"
              type={passwordType}
              value={formInfo.passwordInput}
              onChange={handlePasswordChange}
              onKeyUp={handlePasswordChange}
              onBlur={handlePasswordChange}
            />

            <ShowHidePassword
              togglePassword={togglePassword}
              passwordType={passwordType}
            />
          </div>

          <span
            className="position-relative text-left text-white user-select-none"
            style={{ fontSize: "12px" }}
            id="userPasswordSpan"
          >
            {errorMessage.passwordErr}
          </span>
        </div>

        {/* 
                  ================ USR CONFIRMED PASSWORD =====================
        */}
        <div className="form-group position-relative d-flex flex-column flex-wrap justify-content-end p-0 mb-2">
          <div className="form-group position-relative d-flex flex-row justify-content-end mb-0 p-0">
            <input
              id="confirmPasswordInput"
              name="confirmPasswordInput"
              className="form-control"
              aria-describedby="password"
              autoComplete="new-password"
              placeholder="Confirm password"
              type={confirmPasswordType}
              value={formInfo.confirmPasswordInput}
              onChange={handleConfirmPasswordChange}
              onKeyUp={handleConfirmPasswordChange}
              onBlur={handleConfirmPasswordChange}
            />

            <ShowHidePassword
              togglePassword={toggleConfirmPassword}
              passwordType={confirmPasswordType}
            />
          </div>
          <span
            className="position-relative text-left text-white user-select-none"
            style={{ fontSize: "12px" }}
            id="userConfirmPasswordSpan"
          >
            {errorMessage.confirmPasswordErr}
          </span>
        </div>

        <PasswordLabel isPasswordValid={formInfo.isFormPasswordValid} />

        <button
          type="submit"
          id="submitButton"
          className="btn btn-block mt-4 mb-5 login-btn"
          disabled
        >
          Submit
        </button>
      </form>
    </>
  );
}
