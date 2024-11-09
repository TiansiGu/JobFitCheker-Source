import React, { useState, useEffect } from "react";
import { Line } from "react-chartjs-2";
import "../styles/style.css";
import "../styles/applicationHistory.css";

import {
  Chart as ChartJS,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
  CategoryScale,
} from "chart.js";
import ChartDataLabels from "chartjs-plugin-datalabels";

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
  ChartDataLabels
);

export default function ApplicationHistory() {
  const [applications, setApplications] = useState([]);
  const [graphData, setGraphData] = useState({
    labels: [],
    datasets: [],
  });
  const today = new Date().toDateString();
  const [applicationData, setApplicationData] = useState({
    company: "",
    position: "",
    JobId: "",
  });

  const [errorMessage, setErrorMessage] = useState("");
  const [successMessage, setSuccessMessage] = useState("");

  const fetchAllApplicationData = async () => {
    try {
      const response = await fetch("http://localhost:8080/application-counts", {
        credentials: "include", // Ensures cookies and sessions are included
      });
      if (!response.ok) {
        console.error("Failed to fetch profile data");
      }
      const data = await response.json();
      console.log("Application history: " + JSON.stringify(data));
      setApplications(data);
      makeLinGraph(data);
    } catch (error) {
      console.error("Error fetching data: " + error);
    }
  };
  const makeLinGraph = (data) => {
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
  }, []);

  // add options to adjust y-axis
  const options = {
    scales: {
      x: {
        title: {
          display: true,
          text: "Weeks",
          font: {
            size: 14,
          },
        },
      },
      y: {
        beginAtZero: true,
        suggestedMax: 30,
        title: {
          display: true,
          text: "Number of Applications",
          font: {
            size: 14,
          },
        },
        ticks: {
          stepSize: 10,
        },
      },
    },
    plugins: {
      datalabels: {
        display: true,
        color: "red",
        anchor: "end",
        align: "top",
        offset: 2,
        formatter: (value) => value, // Display the value of each point
      },
    },
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setApplicationData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = async (e, action) => {
    e.preventDefault();
    setSuccessMessage("");
    setErrorMessage("");
    console.log(applicationData);

    try {
      if (action === "addApplication") {
        const response = await fetch("http://localhost:8080/application", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          credentials: "include",
          body: JSON.stringify(applicationData),
        });

        if (response.status === 200) {
          setSuccessMessage(
            `Successfully added application record for ${applicationData.position} at ${applicationData.company}.`
          );
          setApplicationData({ company: "", position: "", jobId: "" });
          // Fetch the updated application data to refresh the chart
          fetchAllApplicationData();
        } else if (response.status === 409) {
          setErrorMessage("Duplicate record: This application already exists.");
        } else {
          setErrorMessage("Failed to add application record.");
        }
      } else if (action === "trackApplication") {
        const { company, position, jobId } = applicationData;
        const response = await fetch(
          `http://localhost:8080/application-records?company=${company}&position=${position}&jobId=${jobId}`,
          {
            method: "GET",
            credentials: "include",
          }
        );
        if (response.status === 200) {
          const text = await response.text();
          console.log(text);
          const message =
            text === "No matching records found"
              ? "Cannot find application record"
              : "You applied this job before";
          setSuccessMessage(`${message}.`);
        } else {
          setErrorMessage("Failed to track application record.");
        }
      }
    } catch (e) {
      setErrorMessage("An error occurred when adding the application");
    }
  };

  return (
    <div>
      <div className="application-graph-container">
        <h1>Application History</h1>
        <p>
          Each data point represents the number of applications for a week
          starting on Monday.
        </p>
        <Line data={graphData} options={options} />
      </div>
      <hr />

      <div className="application-container">
        <h1>Check if you applied before for a position</h1>
        <div className="add-application-container">
          <h3>
            Add an application to your history or track application history
          </h3>
          <form onSubmit={handleSubmit} method="POST">
            <label htmlFor="company">Company:</label>
            <input
              type="text"
              id="company"
              name="company"
              value={applicationData.company}
              onChange={handleChange}
              required
            />
            <label htmlFor="position">Position:</label>
            <input
              type="text"
              id="position"
              name="position"
              value={applicationData.position}
              onChange={handleChange}
              required
            />
            <label htmlFor="jobId">Job Id:</label>
            <input
              type="text"
              id="jobId"
              name="jobId"
              value={applicationData.jobId}
              onChange={handleChange}
              required
            />
            <div class="button-container">
              <button
                type="submit"
                name="addButton"
                onClick={(e) => handleSubmit(e, "addApplication")}
              >
                Add
              </button>
              <button
                type="submit"
                name="trackButton"
                onClick={(e) => handleSubmit(e, "trackApplication")}
              >
                Track
              </button>
            </div>
            {/* Success and error messages displayed at the bottom */}
            {successMessage && (
              <p className="success-message">{successMessage}</p>
            )}
            {errorMessage && <p className="error-message">{errorMessage}</p>}
          </form>
        </div>
      </div>
    </div>
  );
}
