import { configureStore } from "@reduxjs/toolkit";
import authReducer from "./reducer/auth";
import loginSignupReducer from "./reducer/loginSignup";
import popularTermRecentTermReducer from "./reducer/popularTermRecentTerm";
import logger from "redux-logger";
import searchReducer from "./reducer/search";

const store = configureStore({
  reducer: {
    auth: authReducer,
    loginSignup: loginSignupReducer,
    popularOrRecentTerm: popularTermRecentTermReducer,
    search: searchReducer,
  },
  middleware: [logger],
});

export default store;
