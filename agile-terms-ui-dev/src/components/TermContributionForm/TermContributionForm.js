import TextField from "@mui/material/TextField";
import { useFormik } from "formik";
import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import * as yup from "yup";
import PopUpModal from "../shared/PopUpModal";

import service from "../../services/Service";
import TopicSelect from "../TermContribution/TopicSelect";
import "../TermContributionForm/TermContributionForm.scss";

export default function TermContributionForm({ topicList }) {
  const DESCRIPTION_CHARACTER_LIMIT = 1000;
  const TERM_CHARACTER_LIMIT = 50;

  const TERM_MIN_LENGTH = 2;
  const TERM_MAX_LENGTH = 50;
  const authorName = localStorage.getItem("author");
  const [open, setOpen] = useState(false);
  const termName = localStorage.getItem("termName");

  const isContributeDescription = termName ? true : false;

  const navigate = useNavigate();
  const handleClickOpen = () => {
    setOpen(true);
  };

  const handleDeleteButton = () => {
    navigate(-1);
  };

  const handleClose = () => {
    setOpen(false);
  };

  const {
    values,
    errors,
    touched,
    handleChange,
    handleBlur,
    setErrors,
    handleSubmit,
    setFieldValue,
    isSubmitting,
  } = useFormik({
    initialValues: {
      termTitle: termName ? termName : "",
      descriptionContent: "",
      topicIdList: [],
    },
    validationSchema: yup.object().shape({
      termTitle: yup
        .string()
        .trim("White spaces should only be in the middle of words.")
        .strict(true)
        .required("Term title must not be blank.")
        .min(TERM_MIN_LENGTH, "Term title must be between 2-50 characters.")
        .max(
          TERM_MAX_LENGTH - 1,
          "Term title must be between 2-50 characters."
        ),

      descriptionContent: yup
        .string()
        .max(
          DESCRIPTION_CHARACTER_LIMIT - 1,
          "Description content should be between 0-1000 characters."
        ),
    }),
    onSubmit: async (values) => {
      if (
        values.termTitle.trim() !== "" &&
        values.termTitle.trim().length !== 0
      ) {
        const userInput = {
          name: values.termTitle.trim().replace(/\s\s+/g, " "),
          descriptionList: [
            {
              content: values.descriptionContent.trim().replace(/\s\s+/g, " "),
              userName: authorName,
            },
          ],
          topicIdList: values.topicIdList.map((topic) => topic.value),
          relatedTermIdList: [],
        };
        await service
          .create("/terms", userInput)
          .then((response) => {
            if (response.status === 201) {
              navigate(`/term/${response.data.encodedId}`);
            }
          })
          .catch((err) => {
            if (err.response.status === 400) {
              setErrors({ descriptionContent: err.response.data });
            } else if (err.response.status === 401) {
              localStorage.setItem("isLogin", "true");
              localStorage.removeItem("token");
              navigate("/login");
            }

          });
      }
    },
  });

  return (
    <div className="container term-contribution p-4 term-contribution__shadow">
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          {/* {/ =============== TITLE =============== /} */}
          <label htmlFor="exampleInputEmail1">
            <strong>
              Term title <span className="text-danger">*</span>
            </strong>
          </label>

          <TextField
            id="termTitle"
            name="termTitle"
            type="text"
            InputLabelProps={{ shrink: false }}
            label=" "
            variant="outlined"
            fullWidth
            value={values.termTitle}
            onChange={handleChange}
            onBlur={handleBlur}
            onFocus={handleChange}
            error={touched.termTitle && Boolean(errors.termTitle)}
            disabled={isContributeDescription}
          />
          <div className="row px-0 description-hint d-flex flex-nowrap justify-content-between mb-3">
            <span
              id="termTitleSpan"
              className="term-hint__detail mx-3 user-select-none text-secondary"
            >
              {errors.termTitle &&
               touched.termTitle ? (
                <p className="text-danger">{errors.termTitle}</p>
              ) : (
                <p>Type an appropriate title (2-50 characters)</p>
              )}
            </span>
            <span className="description-hint__character-count mr-3">{`${values.termTitle.length}/${TERM_CHARACTER_LIMIT}`}</span>
          </div>
        </div>

        {/* {/ =============== DESCRIPTION =============== /} */}

        <label htmlFor="descriptionContent">
          <strong>Description</strong>
        </label>
        <TextField
          id="descriptionContent"
          name="descriptionContent"
          InputLabelProps={{ shrink: false }}
          margin="dense"
          variant="outlined"
          fullWidth
          label=" "
          rows={10}
          multiline
          value={values.descriptionContent}
          onChange={handleChange}
          onFocus={handleChange}
          onKeyDown={handleChange}
          onBlur={handleBlur}
          error={
            touched.descriptionContent && Boolean(errors.descriptionContent)
          }
        />
        <div className="row px-0 description-hint d-flex flex-nowrap justify-content-between mb-3">
          <span
            id="descriptionContentSpan"
            className="description-hint__detail mx-3 text-secondary user-select-none"
          >
            {errors.descriptionContent && touched.descriptionContent ? (
              <p className="text-danger">{errors.descriptionContent}</p>
            ) : (
              <p>
                Type the description thoroughly and in details. What does it do?
                When would you use it? What are pros/cons and alternatives? (max
                1000 characters)
              </p>
            )}
          </span>
          <span className="description-hint__character-count mr-3">{`${values.descriptionContent.length}/${DESCRIPTION_CHARACTER_LIMIT}`}</span>
        </div>

        {/* {/ ================ TOPIC SELECTION ================  /} */}
        <label htmlFor="descriptionContent">
          <strong>Topics</strong>
        </label>
        <TopicSelect
          id="topicIdList"
          values={values.topicIdList}
          dataList={topicList}
          onChange={(value) => setFieldValue("topicIdList", value)}
        />

        {/* {/ ================ FORM BUTTON ==================== /} */}
        <div>
          <button
            type="submit"
            id="submitButton"
            className="btn mt-4 py-2 save-btn col-md-2 col-sm-6"
            disabled={isSubmitting}
          >
            Save
          </button>
          <button
            type="button"
            id="cancelButton"
            className="btn mt-4 py-2 cancel-btn col-md-2 col-sm-6"
            onClick={handleClickOpen}
          >
            Cancel
          </button>
        </div>
      </form>

      {/* ====================== POPUP MODAL ===================== */}
      <PopUpModal
        open={open}
        handleClose={handleClose}
        handleApproveOption={handleDeleteButton}
        handleCancelOption={handleClose}
        title="Please confirm your discard"
        content="Do you want to discard this draft? You won't be able to revert this."
        approveMessage="Yes, I want to discard it."
        cancelMessage="Cancel"
      />
    </div>
  );
}
