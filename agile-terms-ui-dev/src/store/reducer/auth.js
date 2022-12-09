import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  isAuthenticated: false,
  token: null,
  isTokenExpired: false,
};


export const authSlice = createSlice({
  name: "auth",
  initialState,
  reducers: {
    login(state) {
      state.isAuthenticated = true;
    },
    logout(state) {
      state.isAuthenticated = false;
    },
    expiredToken(state){
      state.isTokenExpired = true;
    },
    renewToken(state){
      state.isTokenExpired = false;
    }
  },
});

export const authAction = authSlice.actions;
export default authSlice.reducer;
