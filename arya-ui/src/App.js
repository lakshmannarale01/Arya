import React, { useState } from 'react';
import axios from 'axios';

function App() {
  const [input, setInput] = useState("");
  const [response, setResponse] = useState("");

  const askArya = async () => {
    const res = await axios.get(`http://localhost:8080/ask?message=${input}&persona=Assistant`);
    setResponse(res.data);
  };

  return (
    <div style={{ padding: '50px', background: '#282c34', color: 'white', minHeight: '100vh' }}>
      <h1>Arya AI Dashboard (React)</h1>
      <input
        type="text"
        value={input}
        onChange={(e) => setInput(e.target.value)}
        placeholder="Tell Arya to do something..."
        style={{ padding: '10px', width: '300px' }}
      />
      <button onClick={askArya} style={{ padding: '10px 20px', marginLeft: '10px' }}>Ask Arya</button>
      <div style={{ marginTop: '20px', color: '#61dafb' }}>
        <strong>Arya:</strong> {response}
      </div>
    </div>
  );
}

export default App;