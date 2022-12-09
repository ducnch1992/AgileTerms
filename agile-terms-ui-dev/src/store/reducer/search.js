import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  isSearching: false,
};

export const searchSlice = createSlice({
  name: "search",
  initialState,
  reducers: {
    startSearch(state) {
      state.isSearching = true;
    },
    endSearch(state) {
      state.isSearching = false;
    },
  },
});

export const searchAction = searchSlice.actions;
export default searchSlice.reducer;
