import React from 'react';
import { Navigate } from 'react-router-dom';

const PrivateRoute = ({ isAuthenticated, loading, children}) => {
   if (loading) {
    return <div>Yükleniyor...</div>; 
  }
  return isAuthenticated ? children : <Navigate to="/login" />;
};

export default PrivateRoute;
