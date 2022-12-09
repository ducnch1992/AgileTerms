import { fireEvent, render, screen } from "@testing-library/react";
import { BrowserRouter } from "react-router-dom";
import Footer from "./Footer";

describe("<Footer />", () => {
  test("render term of service and app version", () => {
    //Arrange
    render(<Footer />, { wrapper: BrowserRouter });
    const termOfServiceElement = screen.getByRole("link", {
      name: "Terms of service",
    });
    const versionElement = screen.getByText("version 0.1.0 beta");
    //Action

    //Assert
    expect(termOfServiceElement).toBeInTheDocument();
    expect(versionElement).toBeInTheDocument();
  });

  test("switch to under-construction page when user click on term of service", () => {
    //Arrange
    render(
      <BrowserRouter>
        <Footer />
      </BrowserRouter>
    );
    const termOfServiceElement = screen.getByRole("link", {
      name: "Terms of service",
    });

    //Action
    fireEvent.click(termOfServiceElement);

    //Assert
    expect(window.location.pathname).toBe("/underconstruction");
  });
});
