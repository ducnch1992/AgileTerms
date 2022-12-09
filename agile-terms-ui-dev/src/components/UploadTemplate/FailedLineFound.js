import React, { useState } from "react";
import "./FailedLineFound.scss";
export default function FailedLineFound({ cause, dataList }) {
  const [fullLength, setFullLength] = useState(false);
  const dataListSize = dataList.length;
  let reason = "";
  switch (cause) {
    case "termIsNull":
      reason = " with empty term in file";
      break;
    case "invalidTermLength":
      reason = " with term length less than 2 or more than 50 characters";
      break;
    case "invalidDescriptionLength":
      reason = " with description length more than 1000 characters";
      break;
    case "existedInTheDatabase":
      reason= " with data existed in database"
      break;
    case "duplicatedDescriptionInFile":
      reason= " duplicated in file"
      break;

    default:
      reason = "undefine";
  }

  const renderFirstFiveItem = (list) => {
    let liList = [];
    for (let i = 0; i < 5; i++) {
      liList.push(<li key={list[i]}>Row {list[i]}</li>);
    }
    return liList;
  };

  const renderList = () => {
    if (dataList.length > 5 && !fullLength) {
      return renderFirstFiveItem(dataList);
    } else {
      return dataList.map((line) => {
        return <li key={line}>Row {line}</li>;
      });
    }
  };

  return dataListSize > 0 ? (
    <div className="mb-">
      <p className="failed-result__label rounded text-left mb-2 pl-4">
      {dataListSize} row(s) {reason}:
        </p>
      <ul className="pl-4">{renderList()}</ul>
      
      {fullLength ? (
        <span
          className="display-option "
          onClick={() => {
            setFullLength(false);
          }}
        >
          Show Less
        </span>
      ) : (
        <>
        {dataListSize > 5 ? 
         <><span>
            ..................... <br/>
          </span>
          <span
          className="display-option m-2"
          onClick={() => {
            setFullLength(true);
          }}
        >
          Show More
        </span></> : <></>}</>
      )}
    </div>
  ):<></>
}

