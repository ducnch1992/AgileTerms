import { Icon } from "@iconify/react";
import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import "./SucceedNotification.scss";
export default function SucceedNotificationPage({
  activeContent,
  reasonContent,
  message,
}) {
  const navigate = useNavigate();
  let redirect;
  const redirectAfter5Seconds = () => {
    redirect = setTimeout(() => {
      navigate("/");
    }, 5000);
  };

  const stopTimeoutAndRedirect = () => {
    clearTimeout(redirect);
    navigate("/");
  };

  useEffect(() => {
    redirectAfter5Seconds();
  });

  return (
    <section className="d-flex flex-column justify-content-center align-items-center container bg-light activate-content">
      <Icon className="my-5 activate-content__button" icon="bi:check-circle" />
      <h1 className="text-center">
        {reasonContent} was
        <span className="ml-2 activate-content__string">{activeContent}</span>
      </h1>
      <h6 className="text-center">{message}</h6>
      
      <button
        type="button"
        className="mt-5 btn btn-return-homepage"
        onClick={stopTimeoutAndRedirect}
      >
        Redirect to homepage
      </button>
    </section>
  );
}
