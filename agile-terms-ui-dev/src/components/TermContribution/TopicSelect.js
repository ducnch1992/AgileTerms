import React from "react";
import Select from "react-select";
import getTextColorFromBackground from "../../utils/getTextColorFromBackground";

export default function TopicSelect({ id, dataList, value, onChange }) {
  const customStyles = {
    menu: (provided, state) => ({
      ...provided,
      cursor: "pointer",
      fontWeight: "700",
      color: state.selectProps.menuColor,
    }),
    option: (styles, { data, isDisabled, isFocused, isSelected }) => ({
      ...styles,
      cursor: "pointer",
      borderRadius: "2px",
      padding: 20,
      backgroundColor: isDisabled
        ? undefined
        : isSelected
        ? data.color
        : isFocused
        ? data.color
        : "white",

      color: isFocused ? getTextColorFromBackground(data.color) : "black",
      ":active": {
        ...styles[":active"],
        backgroundColor: !isDisabled
          ? isSelected
            ? data.color
            : "#02a9f7"
          : undefined,
      },
    }),
    multiValue: (styles, { data }) => {
      return {
        cursor: "pointer",
        ...styles,
        backgroundColor: "none",
      };
    },
    multiValueLabel: (styles, { data }) => ({
      ...styles,
      cursor: "pointer",
      color: getTextColorFromBackground(data.color),
      backgroundColor: data.color,
      fontWeight: "700",
      borderRadius: "7px 0px 0px 7px",
    }),
    multiValueRemove: (styles, { data }) => ({
      ...styles,
      color: "#6D6E6E",
      backgroundColor: data.color,
      borderRadius: "0px 7px 7px 0px",
      ":hover": {
        color: "black",
      },
    }),

    control: () => ({
      cursor: "pointer",
      backgroundColor: "white",
      display: "flex",
      border: "solid grey 2px",
      borderRadius: "5px",
    }),
  };
  return (
    <>
      <Select
        id={id}
        name={id}
        closeMenuOnSelect={false}
        styles={customStyles}
        isMulti={true}
        options={dataList}
        placeholder={""}
        value={value}
        onChange={(value) => onChange(value)}
      />
      <p
        id="topicSelectionSpan"
        className="term-hint__detail user-select-none text-secondary my-1"
      >
        Select relevant topics
      </p>
    </>
  );
}
