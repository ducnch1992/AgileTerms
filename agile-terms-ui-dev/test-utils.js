import { render } from "@testing-library/react";
import React from "react";
import { Provider } from "react-redux";
import store from "./src/store/index";


export default function renderWithProviders(ui) {
  function Wrapper({ children }) {
    return <Provider store={store}>{children}</Provider>;
  }
  return { store, ...render(ui, { wrapper: Wrapper }) };
}
