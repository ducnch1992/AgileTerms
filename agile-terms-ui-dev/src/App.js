import { BrowserRouter, Route, Routes } from "react-router-dom";
import Footer from "./components/Footer/Footer";
import Header from "./components/Header/Header";
import HomePage from "./views/HomePage/HomePage";
import Underconstruction from "./views/Underconstruction/Underconstruction";
import AdminPage from "./views/AdminPage/AdminPage";
import Term from "./views/Term/Term";
import SignUpLoginPage from "./views/LoginPage/SignUpLoginPage";
import ActivateAccountPage from "./views/ActivateAccountPage/ActivateAccountPage";
import SignUpNotificationPage from "./views/SignUpNotificationPage/SignUpNotificationPage";
import TermContribution from "./views/TermContribution/TermContribution";
import AccessDeniedPage from "./views/AccessDeniedPage/AccessDeniedPage";
import NotFoundPage from "./views/NotFoundPage/NotFoundPage";
import AlreadyActivatedPage from "./views/ActivateAccountPage/AlreadyActivatedPage";
import NotActivatedPage from "./views/ActivateAccountPage/NotActivatedPage";
function App() {
  return (
    <BrowserRouter>
      <Header />
      <Routes>
        <Route
          exact
          path={"/underconstruction"}
          element={<Underconstruction />}
        />
        <Route exact path={"/login"} element={<SignUpLoginPage />} />
        <Route exact path={"/admin-page"} element={<AdminPage />} />
        <Route exact path={"/term/:id"} element={<Term />} />
        <Route exact path={"/"} element={<HomePage />} />
        <Route
          exact
          path={"/activate-account/:token"}
          element={<ActivateAccountPage />}
        />
        <Route exact path={"/already-activated"} element={<AlreadyActivatedPage/>}/>
        <Route exact path={"/not-activated"} element={<NotActivatedPage/>}/>
        <Route
          exact
          path={"/sign-up-confirm"}
          element={<SignUpNotificationPage />}
        />
        <Route
          exact
          path={"/term-contribution"}
          element={<TermContribution />}
        />
        <Route exact path={"/access-denied"} element={<AccessDeniedPage />} />
        <Route path = {"*"} element={<NotFoundPage />}/>
      </Routes>
      <Footer />
    </BrowserRouter>
  );
}

export default App;
