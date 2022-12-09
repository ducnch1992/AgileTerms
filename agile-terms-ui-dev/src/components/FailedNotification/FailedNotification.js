import React from "react";
import { Icon } from "@iconify/react";
import { Link } from "react-router-dom";
import "./FailedNotification.scss";
export default function FailedNotification({ reasonContent, message }) {
  return (
    <section className="d-flex flex-column justify-content-center align-items-center container bg-light failed-content">
      <Icon
        className="my-5 failed-content__button"
        icon="radix-icons:cross-circled"
      />
      <h1 className="failed-content__string">{reasonContent}</h1>
      <h6 className="text-center">{message}</h6>
      <Link to="/">
        <button type="button" className="mt-5 btn btn-return-homepage">
          Redirect to homepage
        </button>
      </Link>
    </section>
  );
}
