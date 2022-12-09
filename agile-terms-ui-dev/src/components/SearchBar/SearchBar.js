import React, { useState } from "react";
import { useDispatch } from "react-redux";
import { Link, useNavigate } from "react-router-dom";
import { loginSignupAction } from "../../store/reducer/loginSignup";
import "./SearchBar.scss";
import service from "../../services/Service";
import SearchResult from "./SearchResult";
import { searchAction } from "../../store/reducer/search";
import { authAction } from "../../store/reducer/auth";

export default function SearchBar({ addButtonContent }) {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const [searchData, setSearchData] = useState({
    keywords: "",
    termContainKeyword: [],
  });

  const handleContributeTerm = () => {
    localStorage.setItem("termName", searchData.keywords);
    navigate("/term-contribution");
    dispatch(searchAction.endSearch());
  };

  const handleSearchInput = async (inputKeywords) => {
    if (inputKeywords === "") {
      setSearchData({
        keywords: "",
        termContainKeyword: [],
      });
    } else {
      await service
        .create("/terms/contain-key", {
          name: inputKeywords.trim().replace(/\s\s+/g, " "),
        })
        .then((response) => {
          setSearchData({
            keywords: inputKeywords,
            termContainKeyword: response.data,
          });
        })
        .catch((e) => {
          console.log(e);
        });
    }
  };

  const handleClickOption = (value) => {
    setSearchData({
      keywords: "",
      termContainKeyword: [],
    });
    navigate(`/term/${value.encodedId}`);
    dispatch(searchAction.endSearch());
  };

  const handleNavigate = () => {
    if (localStorage.getItem("token") === null) {
      setTimeout(() => {
        dispatch(loginSignupAction.login());
        localStorage.setItem("isLogin", "true");
        navigate(`/login?return=/term-contribution`);
      }, 500);
    } else {
      service
        .verifyToken(localStorage.getItem("token"))
        .then(() => {
          if (localStorage.getItem("activate-status") === "true") {
            navigate("/term-contribution");
            dispatch(loginSignupAction.login());
            localStorage.setItem("isLogin", "true");
          } else navigate("/not-activated")
        })
        .catch((error) => {
          if (error.response.status === 401) {
            setTimeout(() => {
              localStorage.removeItem("token");
              localStorage.removeItem("activate-status");
              localStorage.removeItem("author");
              localStorage.removeItem("role");
              dispatch(authAction.logout());
              dispatch(authAction.expiredToken());
              localStorage.setItem("isLogin", "true");
              navigate(`/login?return=/term-contribution`);
            }, 500);
          }
        });
    }
  };

  let isMobile = false;
  if (window.innerWidth <= 576) {
    isMobile = true;
  }

  return (
    <section className="container bg-white search-bar py-3 pb-sm-3">
      <div className="justify-content-between row flex-nowrap">
        <p className="search-bar_description font-weight-bold ml-3">
          Learn about a term -{" "}
          <Link to="/underconstruction" className="text-primary">
            see all
          </Link>{" "}
          or get{" "}
          <Link to="/underconstruction" className="text-inspired">
            inspired
          </Link>
        </p>

        <div>
          <button
            className="btn btn-add-description d-flex align-items-center mr-3 mb-2"
            onClick={handleNavigate}
          >
            <i className="fa fa-plus btn-add-description__icon mr-1"></i>
            {isMobile ? (
              <span className="btn-add-description__text">
                {addButtonContent}
              </span>
            ) : (
              <span className="btn-add-description__text">
                Add new {addButtonContent}
              </span>
            )}
          </button>
        </div>
      </div>

      <div className="input-group search-bar__shadow border border-secondary rounded">
        <input
          value={searchData.keywords}
          className="form-control border-0"
          type="search"
          placeholder="Looking for an agile term..."
          onChange={(e) => {
            handleSearchInput(e.target.value);
          }}
          id="search-input"
          autoComplete="off"
          onFocus={() => dispatch(searchAction.startSearch())}
          onBlur={() => {
            dispatch(searchAction.endSearch());
          }}
        />
      </div>
      <SearchResult
        data={searchData}
        handleClickOption={handleClickOption}
        handleContributeTerm={handleContributeTerm}
      />
    </section>
  );
}
