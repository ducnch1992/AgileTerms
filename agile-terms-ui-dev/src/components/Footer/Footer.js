import React from "react";
import { Link } from "react-router-dom";
import axonIcon from "../../assets/img/Axon-icon.png";
import "./Footer.scss";
export default function Footer() {
  return (
    <footer className="container-fluid">
      <div className="d-flex justify-content-between align-items-end pb-1 footer-content">
        <img
          src={axonIcon}
          alt="axon icon"
          className="h-100 footer-content_logo"
        />
        <Link
          to="/underconstruction"
          className="text-light footer-content_text"
        >
          <u>Terms of service </u>
        </Link>
        <span className="footer-content_version">version 0.1.0 beta</span>
      </div>
    </footer>
  );
}
