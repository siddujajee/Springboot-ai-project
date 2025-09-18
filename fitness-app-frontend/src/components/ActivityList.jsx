import {Card, CardContent, Grid, Typography} from "@mui/material";
import React, { useEffect, useState } from "react";
import { getActivities } from "../services/api";
import { useNavigate } from "react-router";

const ActivityList = () => {
  const [activities, setActivities] = useState([]);
  const navigate = useNavigate();
  
  const fetchActivities = async () => {
    try {
      const response = await getActivities();
      setActivities(response.data);
    } catch (error) {
      console.error("Error fetching activities:", error);
    }
  };

  useEffect(() => {
    fetchActivities();
  }, []);

  return (
    <Grid container rowSpacing={1} columnSpacing={{ xs: 1, sm: 2, md: 3 }}>
      {activities.map((activity) => (
        <Grid item xs={6} key={activity.id}>
          <Card onClick={() => navigate(`/activities/${activity.id}`)} sx={{ p: 2, mb: 2, cursor: 'pointer' }}>
            <CardContent>
              <Typography variant="h6">{activity.type}</Typography>
              <Typography>Duration: {activity.duration} minutes</Typography>
              <Typography>Calories Burned: {activity.caloriesBurned}</Typography>
            </CardContent>
          </Card>
        </Grid>
      ))}
    </Grid>
  );
}

export default ActivityList;