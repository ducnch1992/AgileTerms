import React from "react";
import AsyncCreatableSelect from "react-select/async-creatable";
import { components } from "react-select";
export default function CreatableAsyncSelect({
  id,
  handleTermInputChange,
  onChange,
  handleBlur,
}) {
  const singleValue = (props) => {
    return <div className="btn btn-primary">{props.data.value}</div>;
  };
  return (
    <AsyncCreatableSelect
      id={id}
      name={id}
      cacheOptions
      isClearable
      styles={{
        singleValue: (base) => ({
          ...base,
          color: "white",
          backgroundColor: "#01547C",
          textAlign: "center",
          paddingLeft: "5%",
          borderRadius: "5px",
        }),
      }}
      loadOptions={handleTermInputChange}
      onChange={(option) => onChange(option.value)}
      onBlur={handleBlur}
      placeholder={"Choose term you want to contribute..."}
    />
  );
}
