import React, { useState, useEffect } from "react"

export default function CheckQualification() {
    const [jobDescription, setJobDescription] = useState(null);
    const [feedback, setFeedBack] = useState(null);

    const handleSubmit = async(e) => {
        e.preventDefault();
        setFeedBack(null);
        try {
            const response = await fetch(`http://localhost:8080/check-qualification`, {
                method: 'POST',
                headers: {
                    'Content-type': 'application/json',
                },
                credentials: 'include',
                body: JSON.stringify({ jobDescription })
            });
            if (!response.ok) {
                throw new Error("Error occurred when checking qualification.")
            }
            // will return shouldApply, reason
            const data = await response.json();

            console.log("feedback" + data.ShouldApply + data.reason)
            setFeedBack(data);
        } catch (e) {
            console.error('Error checking qualification:', e);
        }
    }

    return (

        <div style={{textAlign: 'left', padding: '20px'}}>
            <h1>Check if you are qualified to apply for a job</h1>
            <form onSubmit={handleSubmit} method="POST">
                <textarea
                    value={jobDescription}
                    onChange={(e) => setJobDescription(e.target.value)}
                    placeholder="Please paste or enter the plain job description here.(Please not use the link)"
                    required
                    rows="20"
                    cols="80"
                />
                <button type="submit">Check</button>
            </form>
            <div style={{textAlign: 'left', marginTop: '20px', padding: '10px'}}>
                <h1>Result</h1>
                {feedback ? (
                    <div style={{textAlign: 'left'}}>
                        <p><strong>Should Apply? {feedback.ShouldApply ? 'Yes' : 'No'}</strong></p>
                        <p>Reason: {feedback.reason}</p>
                    </div>
                ) : (
                    <p>No feed back available</p>
                )}
            </div>
        </div>
    )

}