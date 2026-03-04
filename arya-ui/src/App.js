import React, { useState, useEffect, useRef } from 'react';
import axios from 'axios';

function App() {
  const [input, setInput] = useState("");
  const [response, setResponse] = useState("");
  const [history, setHistory] = useState([]);

  // Create a reference to the bottom of the chat for auto-scrolling
  const chatEndRef = useRef(null);

  // Fetch history on initial load and whenever a new response is received
  useEffect(() => {
    fetchHistory();
  }, [response]);

  // Auto-scroll to the bottom whenever the response changes
  useEffect(() => {
    chatEndRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [response]);

  const fetchHistory = async () => {
    try {
      const res = await axios.get('http://localhost:8080/history');
      setHistory(res.data);
    } catch (err) {
      console.error("Error fetching history:", err);
    }
  };

  const askArya = async () => {
    try {
      const res = await axios.get(`http://localhost:8080/ask?message=${input}&persona=Assistant`);
      setResponse(res.data);
      setInput(""); // Clear input after asking
    } catch (err) {
      console.error("Error asking Arya:", err);
      setResponse("Error: Could not connect to the backend.");
    }
  };

  const clearHistory = async () => {
    if (window.confirm("Are you sure you want to delete all chat history?")) {
      try {
        await axios.delete('http://localhost:8080/history');
        setHistory([]); // Clear local state
        setResponse(""); // Clear current display
      } catch (err) {
        console.error("Error clearing history:", err);
        alert("Failed to clear history.");
      }
    }
  };

  return (
    <div style={{ display: 'flex', height: '100vh', background: '#121212', color: 'white', fontFamily: 'Arial, sans-serif' }}>

      {/* SIDEBAR */}
      <div style={{ width: '280px', borderRight: '1px solid #333', padding: '20px', display: 'flex', flexDirection: 'column', background: '#181818' }}>
        <h3 style={{ borderBottom: '1px solid #333', paddingBottom: '10px' }}>History</h3>

        {/* CLEAR HISTORY BUTTON */}
        <button
          onClick={clearHistory}
          style={{
            marginBottom: '15px',
            background: '#cf6679',
            color: 'white',
            border: 'none',
            padding: '10px',
            cursor: 'pointer',
            borderRadius: '4px',
            fontWeight: 'bold'
          }}
        >
          🗑 Clear All History
        </button>

        <div style={{ flex: 1, overflowY: 'auto' }}>
          {history.length > 0 ? history.map((item) => (
            <div key={item.id} style={{ fontSize: '12px', marginBottom: '10px', padding: '10px', background: '#2c2c2c', borderRadius: '5px' }}>
              <p style={{ margin: 0, color: '#03dac6' }}><b>You:</b> {item.userMessage.substring(0, 30)}...</p>
              <p style={{ margin: '5px 0 0 0', color: '#bbb' }}>{new Date(item.timestamp).toLocaleString()}</p>
            </div>
          )) : <p style={{ color: '#666' }}>No history found.</p>}
        </div>
      </div>

      {/* MAIN CHAT AREA */}
      <div style={{ flex: 1, padding: '50px', display: 'flex', flexDirection: 'column' }}>
        <h1 style={{ color: '#bb86fc' }}>Arya AI Dashboard</h1>

        <div style={{ flex: 1, overflowY: 'auto', marginBottom: '20px', padding: '20px', background: '#1e1e1e', borderRadius: '8px', border: '1px solid #333' }}>
          {response ? (
            <div style={{ lineHeight: '1.6' }}>
              <strong style={{ color: '#bb86fc', fontSize: '18px' }}>Arya:</strong>
              <p style={{ fontSize: '16px', marginTop: '10px' }}>{response}</p>
            </div>
          ) : (
            <p style={{ color: '#666' }}>Ask Arya something to get started...</p>
          )}
          {/* Reference for auto-scroll */}
          <div ref={chatEndRef} />
        </div>

        <div style={{ display: 'flex', gap: '10px' }}>
          <input
            type="text"
            value={input}
            onChange={(e) => setInput(e.target.value)}
            onKeyPress={(e) => e.key === 'Enter' && askArya()}
            placeholder="Type your command here..."
            style={{
              flex: 1,
              padding: '15px',
              borderRadius: '5px',
              border: '1px solid #444',
              background: '#2c2c2c',
              color: 'white',
              outline: 'none'
            }}
          />
          <button
            onClick={askArya}
            style={{
              padding: '10px 30px',
              background: '#bb86fc',
              color: 'black',
              border: 'none',
              borderRadius: '5px',
              cursor: 'pointer',
              fontWeight: 'bold'
            }}
          >
            Ask
          </button>
        </div>
      </div>
    </div>
  );
}

export default App;