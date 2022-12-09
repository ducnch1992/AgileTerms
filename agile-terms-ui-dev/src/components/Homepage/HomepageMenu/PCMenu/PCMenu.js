import React from "react";
import "./PCMenu.scss";
import { useDispatch, useSelector } from "react-redux";

export default function PCMenu({ onChosenTag }) {
  const dispatch = useDispatch();
  const isPopularTerm = useSelector(
    (state) => state.popularOrRecentTerm.isPopularTerm
  );

  const handleBorder = (e) => {
    if (e.target.id === "popularTerm") {
      e.target.className = "active";
      e.target.style.borderRadius = "0px 0px 0px 5px";
    } else {
      e.target.className = "";
      e.target.style.borderRadius = "0px 0px 0px 0px";
    }
  };

  const upDownBorder = (e) => {
    if (e.target.id === "recentTerm") {
      if (
        
        !document.querySelector("#popularTerm").classList.contains("active")
      ) {
        document.querySelector("#blank-div").style.borderRadius =
          "0px 0px 0px 0px";
      }
    } else {
      document.querySelector("#blank-div").style.borderRadius =
        "0px 0px 0px 5px";
    }
  };
  const onMouseChange = (e) => {
    if (!e.target.classList.contains("active")) {
      document.querySelector("#blank-div").style.borderRadius =
        "0px 0px 0px 0px";
    }
  };

  const chooseTag = (e) => {
    if (e.target.id === "popularTerm") {
      onChosenTag(e.target.id);
    } else {
      onChosenTag(e.target.id);
    }
  };

  setTimeout(function () {
    if (!document.querySelector("#popularTerm").classList.contains("active")) {
      document.querySelector("#blank-div").style.borderRadius =
        "0px 0px 0px 0px";
    }
  }, 50);

  return (
    <section className="container bg-white px-5">
      <div className="blank-div" id="blank-div">
        <input
          id="popularTerm"
          className={isPopularTerm ? "popularTerm active" : "popularTerm"}
          type="radio"
          name="menu-option"
          hidden
          defaultChecked={isPopularTerm}
          onLoad={handleBorder}
          onClick={(e) => {
            handleBorder(e);
            chooseTag(e);
          }}
        />
        <label
          className="menu-items text-center col-3 mb-0"
          id="labelPopTerm"
          htmlFor="popularTerm"
          onMouseOver={upDownBorder}
          onMouseLeave={onMouseChange}
        >
          Popular Terms
        </label>

        <input
          id="recentTerm"
          type="radio"
          name="menu-option"
          hidden
          defaultChecked={!isPopularTerm}
          onClick={(e) => {
            handleBorder(e);
            chooseTag(e);
          }}
        />
        <label
          className="menu-items text-center col-3 mb-0"
          htmlFor="recentTerm"
          id="recentTerm"
          onMouseOver={upDownBorder}
        >
          Recent Terms
        </label>
        <span className="col-6" htmlFor="recentTerm"></span>
      </div>
    </section>
  );
}
