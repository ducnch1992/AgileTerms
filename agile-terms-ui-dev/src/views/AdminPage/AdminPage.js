import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Loading from "../../components/shared/Loading";
import ToggleVisibility from "../../components/shared/ToggleVisibility";
import TermTopicBar from "../../components/UploadTemplate/TermTopicBar";
import UploadResult from "../../components/UploadTemplate/UploadResult";
import "./AdminPage.scss";

export default function AdminPage() {
  const [displayResult, setDisplayResult] = useState(true);
  const [responseData, setResponseData] = useState({});
  const [show, setShow] = useState(false);
  const navigate = useNavigate();
  const handleDisplay = (value) => {
    setDisplayResult(value);
  };
  const handleResult = (value) => {
    setResponseData(value);
  };

  useEffect(() => {
    if (localStorage.getItem("role") !== "ROLE_ADMIN") {
      if (localStorage.getItem("token") == null) {
        setTimeout(() => {
          navigate("/login?return=/admin-page");
        }, 400);
      } else {
        setTimeout(() => {
          navigate("/access-denied");
        }, 400);
      }
    } else {
      setTimeout(() => {
        setShow(true);
      }, 400);
    }
  }, [navigate]);

  return show ? (
    <section className="admin-page container bg-light px-5">
      <h4 className="admin-page__header py-3 font-weight-bold ">Upload</h4>
      <TermTopicBar
        displayResult={handleDisplay}
        responseResult={handleResult}
      />
      <ToggleVisibility displayResult={displayResult}>
        <UploadResult responseData={responseData} />
      </ToggleVisibility>
    </section>
  ) : (
    <Loading />
  );
}
