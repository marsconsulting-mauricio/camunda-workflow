import React from 'react'
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'
import './index.css'
import CreditRequest from './pages/CreditRequest'

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<CreditRequest />} />
        <Route path="/credit" element={<CreditRequest />} />
      </Routes>
    </Router>
  )
}

export default App
