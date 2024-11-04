import React, { useState, useEffect } from "react"
import {Line} from 'react-chartjs-2';
import {
    Chart as ChartJS,
    LinearScale,
    PointElement,
    LineElement,
    Title,
    Tooltip,
    Legend, CategoryScale,
} from "chart.js";

ChartJS.register(
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    Title,
    Tooltip,
    Legend
);



export default function ApplicationHistory () {
    const [applications, setApplications] = useState([]);
    const [graphData, setGraphData] = useState({
        labels: [],
        datasets: [],
    });
    const today = new Date().toDateString();

    const fetchAllApplicationData = async () => {
        try {
            const response = await fetch("http://localhost:8080/application-counts", {
                credentials: "include", // Ensures cookies and sessions are included
            });
            if (!response.ok) {
                throw new Error("Failed to fetch profile data");
            }
            const data = await response.json();
            console.log("Application history: " + JSON.stringify(data));
            setApplications(data);
            makeLinGraph(data);
        } catch (error) {
            console.error("Error fetching data: " + error);
        }

    }
    const makeLinGraph =(data) => {
        // get each week's start date:
        const weekStartDate = data.map((entry) => {
            const [year, month, day] = entry.weekStart; // get the date for each week
            const localDate = new Date(year, month - 1, day); // Convert to Date object (-1 since month is 0-indexed)

            console.log("Date:", localDate); // Log the parsed date for debugging
            return localDate.toLocaleDateString();
        });

        const counts = data.map((entry) => entry.applicationCount);

        setGraphData({
            // labels: labels,
            labels: weekStartDate,
            datasets: [
                {
                    label: "Weekly Application History",
                    data: counts,
                    borderColor: "rgb(75, 192, 192)",
                },
            ],
        });
    };

    // Fetch data
    useEffect(() => {
        fetchAllApplicationData();
    }, [])

    return (
        <div>
            <h1>Application History</h1>
            <Line data={graphData} />

        </div>
    );




}
