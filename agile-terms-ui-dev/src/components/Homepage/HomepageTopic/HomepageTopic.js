import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import service from "../../../services/Service";
import "./HomepageTopic.scss";
import getTextColorFromBackground from "../../../utils/getTextColorFromBackground";

export default function HomepageTopic() {
  const [popularTopics, setPopularTopics] = useState([]);

  const init = () => {
    service.getPopularTopics("topics/popular").then((response) => {
      setPopularTopics(response.data);
    });
  };

  useEffect(() => {
    init();
  }, []);

  return (
    <div className="container homepage-header">
      <div className="homepage-title d-flex justify-content-between">
        <span className="homepage-title__text">Popular topics: </span>
      </div>

      <div className="popular-topic border rounded popular-topic__shadow border border-secondary ">
        {popularTopics.map((popularTopic) => {
          return (
            <div key={popularTopic.id} className="m-1 d-inline-block">
              <Link
                to="/underconstruction"
                className="badge badge-info p-2 popular-topic__item"
                style={{
                  backgroundColor: popularTopic.color,
                  color: getTextColorFromBackground(popularTopic.color),
                }}
              >
                {popularTopic.name}
              </Link>
            </div>
          );
        })}
      </div>
    </div>
  );
}
