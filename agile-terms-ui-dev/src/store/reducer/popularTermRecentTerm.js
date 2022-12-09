import { createSlice } from "@reduxjs/toolkit";

const initialState = {
    isPopularTerm: true
}

export const popularTermRecentTermSlice = createSlice({
    name: "popularTermRecentTerm",
    initialState,
    reducers: {
        onPopularTerm(state) {
            state.isPopularTerm = true;
        },

        onRecentTerm(state) {
            state.isPopularTerm = false;
        },
    },
});

export const popularTermRecentTermAction = popularTermRecentTermSlice.actions;
export default popularTermRecentTermSlice.reducer;
