import React from "react";
import {useNavigate } from "react-router-dom";
import "./PopularTerms.scss";
import PopularTermsContent from "./PopularTermsContent";

export default function PopularRecentTerms({ termList }) {
  const navigate = useNavigate();
  localStorage.removeItem("termName");
  let content = ""

  return (
    <div className="container popular-term pt-1 min-vh-100">
      {termList.map((term) => {
        const termName = term.name;
        const firstDescription = term.descriptionList[0];
        const encodedId = term.encodedId;

        if (typeof firstDescription === "undefined") {
          content = "No description added yet."
        } else {
          content = firstDescription.content;
        }

        return (
          <div key={term.encodedId}>
            <div className="popular-term__description pt-3 pb-4 mt-3 popular-term__shadow">
              <div className="popular-term__description-heading font-weight-bold mb-3 border-bottom border-secondary pb-3">
                {termName}
              </div>
              <div className="mb-3">
                <PopularTermsContent content={content} />
              </div>

              <div className="d-flex flex-row-reverse">
                <button
                  className="btn btn-add-description d-flex align-items-center"
                  onClick={() => navigate(`/term/${encodedId}`)}
                >
                  <span>See more descriptions</span>
                </button>
              </div>

            </div>
          </div>
        );
      })}

    </div>
  );
}
