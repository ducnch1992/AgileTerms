import React from "react";
import "./NotFoundPage.scss";
import { Link } from "react-router-dom";

export default function NotFoundPage() {
  return (
    <>
      <div className="vh-100 bg-white container d-flex justify-content-center align-items-center flex-column not-found-page">
        <div className="not-found-page__404">404</div>
        <div className="not-found-page__message">OOPS! NOTHING WAS FOUND</div>
        <Link to="/">
          <button type="button" className="mt-5 btn not-found-page__button">
            Redirect to homepage
          </button>
        </Link>
      </div>
    </>
  );
}
