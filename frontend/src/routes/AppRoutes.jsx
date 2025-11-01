import { Routes, Route, Navigate } from "react-router-dom";
import CreditRequest from "../pages/CreditRequest.jsx";
import CreditList from "../pages/CreditList.jsx";
import CustomerList from "../pages/CustomerList.jsx";
import ProcessMonitor from "../pages/ProcessMonitor.jsx";

export default function AppRoutes() {
  return (
    <Routes>
      <Route path="/" element={<Navigate to="/credit/new" replace />} />
      <Route path="/credit/new" element={<CreditRequest />} />
      <Route path="/credit/list" element={<CreditList />} />
      <Route path="/customers" element={<CustomerList />} />
      <Route path="/monitor" element={<ProcessMonitor />} />
      <Route path="*" element={<div>Página não encontrada.</div>} />
    </Routes>
  );
}
