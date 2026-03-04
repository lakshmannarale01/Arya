// 1. FIXED: Added useEffect to the imports
import React, { useState, useEffect } from 'react';
import axios from 'axios';

function App() {
  const [input, setInput] = useState("");
  const [response, setResponse] = useState("");
  const [history, setHistory] = useState([]); // This stores your DB logs

  // 2. FIXED: useEffect is now defined
  useEffect(() => {
    fetchHistory();
  }, [response]); // Refresh list whenever Arya replies

  const fetchHistory = async () => {
    try {
      const res = await axios.get('http://localhost:8080/history');
      setHistory(res.data);
    } catch (err) {
      console.error("Error fetching history", err);
    }
  };

  const askArya = async () => {
    const res = await axios.get(`http://localhost:8080/ask?message=${input}&persona=Assistant`);
    setResponse(res.data);
  };

  return (
    <div style={{ display: 'flex', height: '100vh', background: '#121212', color: 'white' }}>

      {/* 3. SIDEBAR: Using the 'history' variable here clears the warning */}
      <div style={{ width: '250px', borderRight: '1px solid #333', padding: '20px', overflowY: 'auto' }}>
        <h3>History</h3>
        {history.map((item) => (
          <div key={item.id} style={{ fontSize: '12px', marginBottom: '10px', padding: '5px', background: '#1e1e1e' }}>
            <p><b>You:</b> {item.userMessage.substring(0, 20)}...</p>
          </div>
        ))}
      </div>

      {/* MAIN CHAT */}
      <div style={{ flex: 1, padding: '50px' }}>
        <h1>Arya AI Dashboard</h1>
        <input
          type="text"
          value={input}
          onChange={(e) => setInput(e.target.value)}
          style={{ padding: '10px', width: '300px' }}
        />
        <button onClick={askArya} style={{ padding: '10px 20px', marginLeft: '10px' }}>Ask</button>
        <div style={{ marginTop: '20px', color: '#bb86fc' }}>
          <strong>Arya:</strong> {response}
        </div>
      </div>
    </div>
  );
}

export default App;