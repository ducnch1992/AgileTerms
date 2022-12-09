import React from "react";
import { useSelector } from "react-redux";
import SearchBar from "../../components/SearchBar/SearchBar";
import "./Underconstruction.scss";

export default function Underconstruction() {
  const isSearching = useSelector((state) => state.search.isSearching);
  return (
    <>
      <SearchBar addButtonContent={"Term"} />
      {!isSearching && (
        <div className="container bg-light">
          <div className="container-sm">
            <div className="row box">
              <h4>Under-construction</h4>
            </div>
            <div className="row text">
              <span>admin on 01 - july - 2022</span>
            </div>
            <div className="row message">
              <h6>Awesome features are on the way ...</h6>
            </div>
          </div>
          <p></p>
          <div className="container-sm">
            <div className="row box ">
              <h4>AgileTerms</h4>
              <div className="col-sm-2 col-3 my-auto d-flex justify-content-center">
                <p className="badge badge-pill badge-info">dictionary</p>
              </div>
              <div className="col-sm-3 col-3 my-auto">
                <p className="badge badge-pill badge-dark ">crowdsource</p>
              </div>
            </div>
            <div className="row text">
              <span>admin on 01 - july - 2022</span>
            </div>
            <div className="row message">
              <h6>
                AgileTerms is an online dictionary that provides:<br></br>
                <p></p>
                1. Pragmatic definition<br></br>
                2. Contribution from experts & practitioners <br></br>
                3. Learner engagement <br></br>
                <p></p>
                Unlike other sources, ours product are: <br></br>
                <p></p>
                1. Condense <br></br>
                2. Thriving <br></br>
                3. Community verified
              </h6>
            </div>
          </div>
        </div>
      )}
    </>
  );
}
