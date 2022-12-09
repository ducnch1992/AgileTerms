import React, { useState, useEffect } from "react";
import SucceedNotification from "../../components/SuccessNotification/SucceedNotification";

import service from "../../services/Service";
import { useNavigate, useParams } from "react-router-dom";
import Loading from "../../components/shared/Loading";

export default function ActivateAccountPage() {
  const [isActivatedUser, setIsActivatedUser] = useState(false);
  const { token } = useParams();
  const activeContent = "successful";
  const reasonContent = "Activation";
  const message = "You will be directed to login page in 5 seconds";
  const navigate = useNavigate();
  const initial = () => {
    service
      .get("auth/verify", token)
      .then((response) => {
        if (response.status === 200) {
          setTimeout(() => {
            localStorage.setItem("activate-status", "true");
            setIsActivatedUser(true);
          }, 400);
        }
      })
      .catch((response) => {
        if (response.response.status === 401) {
          setTimeout(() => {
            navigate("/already-activated");
            localStorage.setItem("activate-status","true");
          }, 400);
        } else if (response.response.status === 404) {
          navigate("/not-found");
        }
      });
  };
  useEffect(() => initial(), [navigate]);
  return (
    <>
      {isActivatedUser ? (
        <SucceedNotification
          activeContent={activeContent}
          reasonContent={reasonContent}
          message={message}
        />
      ) : (
        <Loading />
      )}
    </>
  );
}
