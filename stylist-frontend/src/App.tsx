import React from 'react';
import { BrowserRouter as Router, Route, Switch, Link } from 'react-router-dom';
import Dashboard from './pages/Dashboard';
import Analysis from './pages/Analysis';
import Admin from './pages/Admin';

const App: React.FC = () => {
  return (
    <Router>
      <div className="min-h-screen bg-gray-100">
        <nav className="bg-white shadow mb-4">
          <div className="container mx-auto px-4 py-2 flex justify-between">
            <Link to="/" className="text-xl font-bold">🕶️ MUSINSA STYLIST 대시보드</Link>
            <div className="space-x-4">
              <Link to="/" className="text-gray-700 hover:text-blue-500">📑 브랜드 현황</Link>
              <Link to="/admin" className="text-gray-700 hover:text-blue-500">🔑 관리자전용</Link>
              {/* <Link to="/analysis" className="text-gray-700 hover:text-blue-500">📊 분석 업무</Link> */}
            </div>
          </div>
        </nav>
        <div className="container mx-auto px-4">
          <Switch>
            <Route exact path="/" component={Dashboard} />
            <Route path="/admin" component={Admin} />
            {/* <Route path="/analysis" component={Analysis} /> */}
          </Switch>
        </div>
      </div>
    </Router>
  );
};

export default App;