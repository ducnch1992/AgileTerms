import React, { useState } from "react";
import "./TermTopicBar.scss";
import ConfigVariables from "./../../ConfigVariables.json";
import service from "../../services/Service";
import Alert from "../shared/Alert";
import { useNavigate } from "react-router-dom";
import { authAction } from "../../store/reducer/auth";
import { useDispatch } from "react-redux";
import { Icon } from "@iconify/react";

export default function TermTopicBar({ displayResult, responseResult }) {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const DOWNLOAD_URL = "http://192.168.70.67:81";
  const [termIsSelected, setTermIsSelected] = useState(true);
  const [isValidFile, setIsValidFile] = useState(true);
  const [inValidMessage, setInvalidMessage] = useState({
    title: "Invalid Input File!",
    message: "",
  });
  const [fileName, setFileName] = useState("");
  const selectHandler = () => {
    setTermIsSelected(!termIsSelected);
  };

  const collapseHandle = () => {
    setIsValidFile(true);
  };

  function handleChange(event) {
    const file = event.target.files[0];
    setFileName(`file ${file.name} was uploaded.`);
    const REGEX =
      /^([a-zA-Z0-9\s`~!@#$%^&\(\)_\-+=\{\}\[\]<>';,.])+(.xls|.xlsx)$/; // file extension should be .xlsx or .xls
    const fileSize = file.size / 1024 / 1024; // file size in MB

    if (!REGEX.test(file.name.toLowerCase())) {
      displayResult(false);
      setIsValidFile(false);
      setInvalidMessage((prev) => ({
        ...prev,
        message: "Please upload an Excel File (.xls/.xlsx)",
      }));
    } else if (fileSize >= ConfigVariables.EXCEL_MAX_FILE_SIZE) {
      setIsValidFile(false);
      setInvalidMessage((prev) => ({
        ...prev,
        message: `Please upload file smaller than ${ConfigVariables.EXCEL_MAX_FILE_SIZE} MB`,
      }));
    } else {
      let formData = new FormData();
      formData.append("file", file);
      service
        .uploadFile("terms/upload-file", formData, {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        })
        .then((response) => {
          if (response.status === 200) {
            displayResult(true);
            setIsValidFile(true);
            responseResult(response.data);
          }
          event.target.value = null;
        })
        .catch((err) => {
          displayResult(false);
          setIsValidFile(false);
          event.target.value = null;
          if (err.response.request.status === 400) {
            setIsValidFile(false);
            setInvalidMessage(() => ({
              title: "Invalid Input File!",
              message: err.response.data,
            }));
          } else if (err.response.request.status === 403) {
            setInvalidMessage({
              title: "Session Time Out!",
              message:
                "Please Login again, you will be redirect to login page after 5 seconds",
            });
            dispatch(authAction.logout());
            localStorage.removeItem("token");
            localStorage.removeItem("role");
            setTimeout(() => {
              navigate("/login?return=/admin-page");
            }, 5000);
          } else {
            setInvalidMessage((prev) => ({
              ...prev,
              message: err.response.data,
            }));
          }
        });
    }
  }

  return (
    <>
      <form
        method="post"
        encType="multipart/form-data"
        className="d-inline mr-5"
      >
        <div className="container-fluid btn-group btn-group-toggle px-0">
          <input
            type="radio"
            id="terms"
            name="TermsTopics"
            value="terms"
            onChange={() => selectHandler()}
            checked={termIsSelected}
            hidden
          />
          <label className="select-bar__label col-6 " htmlFor="terms">
            Term
          </label>
          <input
            type="radio"
            id="topics"
            name="TermsTopics"
            value="topics"
            onChange={() => selectHandler()}
            checked={!termIsSelected}
            hidden
          />
          <label className="select-bar__label col-6" htmlFor="topics">
            Topic
          </label>
        </div>
        <div className="upload-template py-3" data-text="Select your file!">
          <input
            type="file"
            accept=".xlsx, .xls"
            name="uploadFile"
            id="uploadBtn"
            onChange={handleChange}
            hidden
          />
          <label
            htmlFor="uploadBtn"
            className=" btn upload-btn__label col-2 mr-2"
            data-toggle="modal"
            data-target="#exampleModalCenter"
            onClick={termIsSelected ? "Upload Terms" : ()=>{navigate("/underconstruction")}}
          >
            <Icon className="mr-1 upload-btn__icon" icon="ep:upload-filled" />

            {termIsSelected ? "Upload Terms" : "Upload Topics"}
          </label>
          {fileName}
        </div>
        {termIsSelected ? (
          <a
            href={`${DOWNLOAD_URL}/AgileTerm_TermAndDescription_Template.xlsx`}
          >
            <u>Get template</u>
          </a>
        ) : (
          <a href={`${DOWNLOAD_URL}/AgileTerm_Topic_Template.xlsx`}>
            <u>Get template</u>
          </a>
        )}
      </form>
      {!isValidFile && (
        <Alert
          classes="alert alert-danger alert-dismissible fade show"
          title={inValidMessage.title}
          message={inValidMessage.message}
          isCollapse={collapseHandle}
        />
      )}
    </>
  );
}
