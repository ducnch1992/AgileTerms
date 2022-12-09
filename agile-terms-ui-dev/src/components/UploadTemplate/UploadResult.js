import React from "react";
import FailedLineFound from "./FailedLineFound";
import "./UploadResult.scss";

export default function UploadResult({ responseData }) {
  let uploadedListSize = 0;
  if (undefined !== responseData.successfulCases) {
    uploadedListSize = Object.keys(responseData.successfulCases).length;
  }

  return undefined !== responseData.failedRows ? (
    <div className="mt-5">
      <p>
        The file was stopped processing from{" "}
        <strong>row {responseData.rowStopCounting}!</strong>
      </p>
      <div className="resultDisplay row">
        <div className="succeed-result col-md-6 pl-0">
          <p className="succeed-result__label rounded text-left pl-4">
            {!uploadedListSize && "No term imported"}
            {uploadedListSize > 0 &&
              `${uploadedListSize} term(s) successfully imported`}
          </p>

          <ul className="pl-4">
            {undefined !== responseData.successfulCases &&
              uploadedListSize > 0 &&
              Object.entries(responseData.successfulCases).map(
                ([key, value]) => (
                  <li key={key}>
                    <strong>{key}</strong>:{" "}
                    <span className="description-color">{value}</span>{" "}
                    description(s) imported
                  </li>
                )
              )}
          </ul>
        </div>
        <div className="failed-result col-md-6">
          {Object.entries(responseData.failedRows).map(([key, value]) => {
            return <FailedLineFound key={key} cause={key} dataList={value} />;
          })}
        </div>
      </div>
    </div>
  ) : (
    <></>
  );
}
