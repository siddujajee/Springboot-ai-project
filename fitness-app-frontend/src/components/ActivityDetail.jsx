import { Box, Typography } from "@mui/material";
import React, { useEffect } from "react";
import { useParams } from "react-router";
import { getActivityDetail } from "../services/api";
import { useState } from "react";

const ActivityDetail = () => {
  const { id } = useParams();
  const [activity, setActivity] = useState(null);
  const [activitySummary, setActivitySummary] = useState(null);
  const [improvements, setImprovments] = useState(null);
  const [safety, setSafety] = useState(null);
  const [suggestions, setSuggestions] = useState(null);
  
  useEffect(() => {
    const fetchActivityDetail = async () => {
      try {
        const response = await getActivityDetail(id);
        setActivity(response.data);
        setImprovments(response.data.improvements);
        setSafety(response.data.safety);
        setSuggestions(response.data.suggestions);
        setActivitySummary(response.data.activitySummary);

      } catch (error) {
        console.error("Error fetching activity detail:", error);
      }
    };
    fetchActivityDetail();
  }, [id]);

  if(!activity) {
    return <Typography>Loading...</Typography>;
  }
  return (
    <Box sx={{ p: 2 }}>
      <Typography variant="h4" gutterBottom>Activity Detail</Typography>
      <Typography variant="h6">Type: {activity.activityType}</Typography>
      <Typography variant="h6">Duration: {activity.duration} minutes</Typography>
      <Typography variant="h6">Calories Burned: {activity.caloriesBurned}</Typography>
      <Typography variant="h6">Date: {new Date(activity.createdAt).toLocaleString()}</Typography>
      <Typography variant="h5" sx={{ mt: 4 }}>Activity Summary:</Typography>
      {activitySummary ? (
        <Typography>{activitySummary}</Typography>
      ) : (
        <Typography>No activity summary available.</Typography>
      )}
      <Typography variant="h5" sx={{ mt: 4 }}>Improvements:</Typography>
      {improvements && improvements.length > 0 ? (
        <ul>  
          {improvements.map((improvement, index) => (
            <li key={index}>{improvement}</li>
          ))}
        </ul>
      ) : (
        <Typography>No improvements available.</Typography>
      )}
      <Typography variant="h5" sx={{ mt: 4 }}>Safety:</Typography>
      {safety && safety.length > 0 ? (
        <ul>
          {safety.map((safetyItem, index) => (
            <li key={index}>{safetyItem}</li>
          ))}
        </ul>
      ) : (
        <Typography>No safety information available.</Typography>
      )}
      <Typography variant="h5" sx={{ mt: 4 }}>Suggestions:</Typography>
      {suggestions && suggestions.length > 0 ? (
        <ul>
          {suggestions.map((suggestion, index) => (
            <li key={index}>{suggestion}</li>
          ))}
        </ul>
      ) : (
        <Typography>No suggestions available.</Typography>
      )}
    </Box>
  );
}

export default ActivityDetail;