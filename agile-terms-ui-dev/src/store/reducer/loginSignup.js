
import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  isLogin: true,
  isSignUp: false
};

export const loginSignupSlice = createSlice({
  name: "loginSignup",
  initialState,
  reducers: {
    login(state) {
      state.isLogin = true;
      state.isSignUp = false;
    },
    signup(state) {
      state.isLogin = false;
      state.isSignUp = true;
    },
  },
});

export const loginSignupAction = loginSignupSlice.actions;
export default loginSignupSlice.reducer;
