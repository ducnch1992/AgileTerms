import React from "react";
import "./SearchResult.scss";
import { useSelector } from "react-redux";

export default function SearchResult({
  data,
  handleClickOption,
  handleContributeTerm,
}) {
  const renderNotFoundMessage = () => {
    if (data.keywords !== "") {
      return (
        <>
          There are no definitions for <cite>"{data.keywords}"</cite> yet. You
          should add one!{" "}
          <div
            className="m-1 d-md-inline-block badge p-2 term-found-list__item"
            onMouseDown={handleContributeTerm}
          >
            Click here to create a new term
          </div>
        </>
      );
    }
  };
  const isSearching = useSelector((state) => state.search.isSearching);

  return (
    isSearching &&
    data.termContainKeyword && (
      <div className="py-3 pb-sm-3 justify-content-center search-result">
        <div className="search-result-title d-flex">
          <span className="search-result-title__text">Search Result: </span>
        </div>

        <div className="term-found-list  border rounded  border-secondary ">
          <div className="m-1 d-inline-block ">
            {data.termContainKeyword.length !== 0
              ? data.termContainKeyword.map((term) => {
                  return (
                    <div
                      // to={`/term/${term.encodedId}`}
                      key={term.encodedId}
                      className="m-1 d-md-inline-block badge p-2 term-found-list__item"
                      onMouseDown={() => handleClickOption(term)}
                    >
                      {term.name}
                    </div>
                  );
                })
              : renderNotFoundMessage()}
          </div>
        </div>
      </div>
    )
  );
}
