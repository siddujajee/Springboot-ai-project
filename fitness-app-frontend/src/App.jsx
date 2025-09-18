import Button from '@mui/material/Button';
import Box from '@mui/material/Box';
import { useContext, useEffect, useState } from 'react';
import { useDispatch } from 'react-redux';
import { setCredentials } from './store/authSlice';
import { AuthContext } from 'react-oauth2-code-pkce';
import { BrowserRouter as Router, Navigate, Route, Routes, useLocation } from "react-router";
import ActivityForm from './components/ActivityForm';
import ActivityList from './components/ActivityList';
import ActivityDetail from './components/ActivityDetail';
import { Typography } from '@mui/material';
import { Card, CardContent } from '@mui/material';

const ActivitiesPage = () => {
  return (
    <Box component="section" sx={{ p: 2, border: '1px dashed grey' }}>
      <ActivityForm onActivityAdded={() => window.location.reload()} />
      <ActivityList />
    </Box>
  );
}
function App() {
  const {token, tokenData, logIn, logOut, isAuthenticated} = useContext(AuthContext)
  const dispatch = useDispatch();
  const [authReady, setAuthReady] = useState(false);
  useEffect(() => {
    if(token){
      dispatch(setCredentials({
        token,
        user: tokenData,
      }));
      setAuthReady(true);
    }
  }, [token, tokenData, dispatch])
  return (
    <Router>
      {!token ? (
        <Box
          sx={{
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
            minHeight: "100dvh", // ✅ Fix: avoids unnecessary scroll on mobile/desktop
            background: "linear-gradient(135deg, #7198a4ff 0%, #dfdfe4ff 100%)",
            p: 2,
            overflow: "hidden", // optional: ensures no accidental scroll
          }}
        >
          <Card sx={{ maxWidth: 400, width: "100%", borderRadius: 3, boxShadow: 5 }}>
            <CardContent sx={{ textAlign: "center", p: 4 }}>
              <Typography variant="h4" gutterBottom sx={{ fontWeight: "bold" }}>
                Fitness App
              </Typography>
              <Typography variant="subtitle1" color="text.secondary" gutterBottom>
                Please login to continue
              </Typography>
              <Button variant="contained" size="large" sx={{
                  mt: 3,
                  backgroundColor: "#de005e",
                  "&:hover": { backgroundColor: "#b8004d" },
                  textTransform: "none",
                  borderRadius: 2,
                  px: 4,
                }} onClick={() => logIn()}>Login</Button>
            </CardContent>
          </Card>
        </Box>
        
      ) : (
        <Box component="section" sx={{ p: 2, border: '1px dashed grey' }}>
          <Button variant="contained" color="#de005e" onClick={() => logOut()}>Logout</Button>
          <br /><br />
          <Routes>
            <Route path="/activities" element={<ActivitiesPage />} />
            <Route path="/activities/:id" element={<ActivityDetail />} />
            <Route path="/" element={token ? <Navigate to="/activities" replace /> : <div>Welcome, Please log in</div>} />
          </Routes>
        </Box>
      )}
    </Router>
    
  )
}

export default App
