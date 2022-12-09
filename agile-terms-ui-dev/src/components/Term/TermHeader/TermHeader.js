import React, { useState } from "react";
import { Link } from "react-router-dom";
import getTextColorFromBackground from "../../../utils/getTextColorFromBackground";
import "./TermHeader.scss";

export default function TermHeader({ term, topicList }) {
  const [fullLength, setFullLength] = useState("false");
  let isMobile = false;
  if (window.innerWidth <= 576) {
    isMobile = true;
  }

  let topicListLine = document.querySelector(".topic-related-line");

  if (null !== topicListLine) {
    if (null !== topicList && topicList.length !== 0) {
      topicListLine.classList.remove("d-none");
      topicListLine.classList.add("d-block");
    } else {
      topicListLine.classList.remove("d-block");
      topicListLine.classList.add("d-none");
    }
  }

  const renderFirstThreeTopics = (list) => {
    let threeTopicList = [];
    for (let i = 0; i < 3; i++) {
      threeTopicList.push(
        <Link
          key={list[i].id}
          to="/term-contribution"
          className="topic-badge badge badge-pill p-2 m-1"
          style={{
            backgroundColor: list[i].color,
            color: getTextColorFromBackground(list[i].color),
          }}
        >
          {list[i].name}
        </Link>
      );
    }
    return threeTopicList;
  };

  let showButtonInMobile = document.querySelector(
    ".term-topic__display-option"
  );
  const renderList = () => {
    if (topicList.length) {
      if (topicList.length > 5 && fullLength && isMobile) {
        return renderFirstThreeTopics(topicList);
      } else {
        if (null != showButtonInMobile) {
          showButtonInMobile.classList.remove("d-inline-block");
          showButtonInMobile.classList.add("d-none");
        }

        return topicList.map((topic) => (
          <Link
            key={topic.id}
            to={"/underconstruction"}
            className="topic-badge badge badge-pill p-2 m-1"
            style={{
              backgroundColor: topic.color,
              color: getTextColorFromBackground(topic.color),
            }}
          >
            {topic.name}
          </Link>
        ));
      }
    } else {
    }
  };

  return (
    <div className="term-header container pb-1 border border-dark border-right-0 border-left-0">
      <div className="term-title d-flex justify-content-between mb-1">
        <span className="term-title__text">{term}</span>
      </div>

      <div className="topic-related-line d-none">
        <span>Contributors think "{term}" is related to:</span>
      </div>

      <div className="term-topic d-block">

        {renderList()}

        {isMobile  && topicList.length > 5 ? (
          <span>
            {fullLength && (
              <span
                className="term-topic__display-option d-inline-block text-primary p-2 m-1"
                onClick={() => {
                  setFullLength(false);
                }}
              >
                Show More
              </span>
            )}
            {!fullLength && (
              <span
                className="term-topic__display-option d-inline-block text-primary p-2 m-1"
                onClick={() => {
                  setFullLength(true);
                }}
              >
                Show Less
              </span>
            )}
          </span>
        ) : (
          <></>
        )}
      </div>
    </div>
  );
}
