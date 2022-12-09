import React, { useState, useEffect } from "react";
import SearchBar from "../../components/SearchBar/SearchBar";
import "./HomePage.scss";
import HomepageTopic from "../../components/Homepage/HomepageTopic/HomepageTopic";
import MobileMenu from "../../components/Homepage/HomepageMenu/MobileMenu/MobileMenu";
import PCMenu from "../../components/Homepage/HomepageMenu/PCMenu/PCMenu";
import PopularRecentTerms from "../../components/PopularTerms/PopularRecentTerms";
import service from "../../services/Service";
import { useSelector } from "react-redux";

export default function Homepage() {
  const [termList, setTermList] = useState([]);
  const isPopularTerm = useSelector(
    (state) => state.popularOrRecentTerm.isPopularTerm
  );

  const [termTag, setTermTag] = useState(
    isPopularTerm ? "popularTerm" : "recentTerm"
  );
  const isSearching = useSelector((state) => state.search.isSearching);

  const init = (termTag) => {
    if (termTag === "popularTerm") {
      service
        .getAll("/terms/popular")
        .then((response) => {
          setTermList(response.data);
        })
        .catch((e) => {
          console.log(e);
        });
    } else {
      service
        .getAll("/terms/recent")
        .then((response) => {
          setTermList(response.data);
        })
        .catch((e) => {
          console.log(e);
        });
    }
  };
  const onChosenTag = (clickedTag) => {
    setTermTag(clickedTag);
  };

  useEffect(() => {
    init(termTag);
  }, [termTag]);

  return (
    <section className="homepage">
      <SearchBar addButtonContent={"Term"} />
      {!isSearching && <HomepageTopic />}
      {!isSearching && window.innerWidth <= 576 ? (
        <MobileMenu onChosenTag={onChosenTag} />
      ) : (
        !isSearching && <PCMenu onChosenTag={onChosenTag} />
      )}
      {!isSearching && <PopularRecentTerms termList={termList} />}
    </section>
  );
}
