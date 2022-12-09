import React from "react";
import "./Description.scss";
import { useNavigate } from "react-router-dom";
import DescriptionContent from "./DescriptionContent";

export default function Description({ descriptionList, termName }) {
  const navigate = useNavigate();
  return (
    <div className="container description-section pt-1 pb-3 min-vh-100">
      {descriptionList.map((description) => {
        const dateFromBackEnd = description.createDate;
        const dayMonthYear = dateFromBackEnd.split("/");
        const date = new Date(
          `${dayMonthYear[1]}-${dayMonthYear[0]}-${dayMonthYear[2]}`
        );
        const day = date.getDate();
        const month = date.toLocaleString("default", { month: "long" });
        const year = date.getFullYear();
        const dateToShow = `${day} - ${month} - ${year}`;

        return (
          <div key={description.id}>
            <div className="row description__detail py-1 py-sm-4 mt-4 mx-0 description-shadow">
              <div className="mt-4 col-2 col-sm-1 flex-column description__voting text-center">
                <i
                  className="fa fa-caret-up description__arrow description__arrow-up"
                  aria-hidden="true"
                  onClick={() => navigate("/underconstruction")}
                ></i>
                <div className="description__rating">
                  {description.votePoint}
                </div>
                <i
                  className="fa fa-caret-down description__arrow description__arrow-down"
                  aria-hidden="true"
                  onClick={() => navigate("/underconstruction")}
                ></i>
              </div>
              <div className="col-10 col-sm-11 description__content text-break">
                <div className="term_heading font-weight-bold mb-3 ">
                  {termName}
                </div>

                <DescriptionContent content={description.content} />
                <hr className="mt-4" />
                <div className="row description__timestamp py-2">
                  <span className="col-12 col-md-8 description-author">
                    Posted by{" "}
                    <span className="description-author__name font-weight-bold">
                      {description.authorName}
                    </span>
                  </span>
                  <span className="col-12 col-md-4 text-right">
                    {dateToShow}
                  </span>
                </div>
              </div>
            </div>
          </div>
        );
      })}
    </div>
  );
}
