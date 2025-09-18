import { createSlice } from '@reduxjs/toolkit'


const authSlice = createSlice({
  name: 'auth',
  initialState: {
    user: JSON.parse(localStorage.getItem('user')) || null,
    token: localStorage.getItem('token') || null,
    userId: localStorage.getItem('userId') || null,
  },
  reducers: {
    setCredentials: (state, action) => {
      state.user = action.payload.user;
      state.token = action.payload.token;
      state.userId = action.payload.user.sub; // accessing keycloak user ID from token payload
      localStorage.setItem('user', JSON.stringify(action.payload.user)); // user information is token decoded, so stringify it
      localStorage.setItem('token', action.payload.token);
      localStorage.setItem('userId', action.payload.user.sub);
    },
    logout: (state) => {
      state.user = null;
      state.token = null;
      state.userId = null;
      localStorage.removeItem('user');
      localStorage.removeItem('token');
      localStorage.removeItem('userId');
    },
  },
});

export const { setCredentials, logout } = authSlice.actions;
export default authSlice.reducer;