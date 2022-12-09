import React, { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { useNavigate, useParams } from "react-router-dom";
import SearchBar from "../../components/SearchBar/SearchBar";
import Description from "../../components/Term/Description/Description";
import TermHeader from "../../components/Term/TermHeader/TermHeader";
import service from "../../services/Service";
import "./Term.scss";

export default function Term() {
  const isSearching = useSelector((state) => state.search.isSearching);
  const [termFound, setTermFound] = useState("");
  const [topicRelated, setTopicRelated] = useState([]);
  const [descriptions, setDescriptions] = useState([]);
  const { id } = useParams();

  const addButtonContent = "Description";
  const navigate = useNavigate();

  useEffect(() => {
    const initial = () => {
      service
        .getSubordinate("terms", id, "details")
        .then((response) => {
          setTermFound(response.data.name);
          setTopicRelated(response.data.topicList);
          setDescriptions(response.data.descriptionList);

          if (
            localStorage.getItem("termName") === null ||
            localStorage.getItem("termName") === ""
          ) {
            localStorage.setItem("termName", response.data.name);
          }
        })
        .catch((error) => {
          if (error.response.status === 404) {
            navigate("/not-found");
          }
        });
    };
    initial();
    window.scrollTo(0, 0);
  }, [id, navigate]);

  return (
    <section className="term-section">
      <div className="sticky-top">
        <SearchBar addButtonContent={addButtonContent} />
        {!isSearching && (
          <TermHeader term={termFound} topicList={topicRelated} />
        )}
      </div>
      {!isSearching && <Description descriptionList={descriptions} />}
    </section>
  );
}
