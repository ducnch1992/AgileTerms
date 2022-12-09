import React from "react";
import "./MobileMenu.scss";
import { useDispatch,useSelector } from "react-redux";
import {popularTermRecentTermAction} from "../../../../store/reducer/popularTermRecentTerm"


export default function MobileMenu({onChosenTag}) {
  const dispatch = useDispatch();
  const isPopularTerm = useSelector(state => state.popularOrRecentTerm.isPopularTerm);
  

  const chooseTag = (e) => {
    if (e.target.id === "popularTerm") {
      onChosenTag(e.target.value);
    } else {
      onChosenTag(e.target.value);
    }
  }



  return (
    <section className="container mobile-menu pt-3 d-flex justify-content-center ">
      <select
        className="form-select form-select-lg mobile-menu_select popular-topic__shadow"
        aria-label="Default select example"
        onChange={chooseTag}
        id="tagTerm" 
      >
        <option 
        defaultValue = {isPopularTerm}
        className="mobile-menu_item" 
        value="popularTerm"
        >
          Popular terms
        </option>
        <option 
        defaultValue = {!isPopularTerm}
        className="mobile-menu_item" 
        value="recentTerm"
        >
          Recent terms
        </option>
      </select>
    </section>
  );
}
