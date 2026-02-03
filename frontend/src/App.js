import React, { useState, useCallback } from 'react';
import SpeechRecognition, { useSpeechRecognition } from 'react-speech-recognition';
import axios from 'axios';
import './App.css';

const BACKEND_URL = 'http://localhost:2004';  // Fixed port + path

function App() {
  const [isListening, setIsListening] = useState(false);
  const [history, setHistory] = useState([]);
  const [status, setStatus] = useState('Click mic to speak');  // Fixed: setStatus (capital S)
  
  const { transcript, resetTranscript, listening } = useSpeechRecognition();

  // FIXED: startListening - actually STARTS listening
  const startListening = useCallback(() => {
    setIsListening(true);
    SpeechRecognition.startListening({ continuous: true, language: 'mr-IN' });  // STARTS mic
    setStatus('🗣️ Listening... Speak Marathi/Hindi/English');
  }, []);

  // FIXED: stopListening function (was missing!)
  const stopListening = useCallback(() => {
    setIsListening(false);
    SpeechRecognition.stopListening();
    if (transcript) {
      handleVoiceCommand(transcript);
      resetTranscript();
    }
    setStatus('Processing...');
  }, [transcript, resetTranscript]);

  // FIXED: API paths + field names
  const handleVoiceCommand = async (voiceText) => {
    try {
      const translatedText = voiceText.includes('नोटपॅड') || voiceText.includes('notepad')
        ? 'Open notepad'
        : voiceText;

      const response = await axios.post(`${BACKEND_URL}/api/v1/voice/command`, {  // Fixed path
        originalVoiceText: voiceText,  // Fixed: originalVoiceText (capital V)
        translatedText: translatedText,
        language: 'mr'
      });

      const result = response.data;
      setHistory(prev => [result, ...prev.slice(0, 9)]);
      setStatus(`✅ ${result.aiResponse}`);
      
    } catch (error) {
      setStatus('❌ Error: ' + error.message);
    }
  };

  const loadHistory = async () => {
    try {
      const response = await axios.get(`${BACKEND_URL}/api/v1/voice/history`);  // Fixed path
      setHistory(response.data);
    } catch (error) {
      console.error('History loading failed');
    }
  };

  return (
    <div className="App">
      <header className="App-header">
        <h1>🗣️ LaptopFriend AI</h1>
        <p>{status}</p>
        
        {/* Voice Controls */}
        <div className="voice-controls">
          <button 
            onClick={startListening}
            disabled={listening}
            className="mic-btn listen"
          >
            🎤 Start Listening
          </button>
          <button 
            onClick={stopListening}  // Now defined!
            disabled={!listening}
            className="mic-btn stop"
          >
            ⏹️ Stop
          </button>
          <button onClick={loadHistory} className="history-btn">
            📜 Load History
          </button>
        </div>

        {/* Recent Transcript */}
        {transcript && (
          <div className="transcript">
            <strong>Heard:</strong> <span>{transcript}</span>
          </div>
        )}

        {/* Command History */}
        <div className="history">
          <h3>Recent Commands (Last 10):</h3>
          {history.map((cmd, i) => (
            <div key={i} className="history-item">
              <strong>{cmd.originalVoiceText}</strong> 
              → <code>{cmd.commandExecuted}</code>
              <br/><small>{cmd.aiResponse}</small>
            </div>
          ))}
        </div>
      </header>
    </div>
  );
}

export default App;
