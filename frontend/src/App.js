import { useEffect } from 'react'
import {
  BrowserRouter as Router,
  Routes,
  Route,
  useNavigate,
  Navigate,
} from 'react-router-dom'

import LoginPage from './pages/LoginPage'
import Home from './pages/HomePage'
import Mypage from './pages/Mypage'
import ContentDetailPage from './pages/ContentDetailPage'
import WritePage from './pages/WritePage'
import SignupPage from './pages/SignupPage'
import EditProfilePage from './pages/EditProfilePage'
import EditArticlePage from './pages/EditArticlePage'
import PrivateRoute from './components/PrivateRoute'


function MainRouter() {
  const navigate = useNavigate()

  useEffect(() => {
    const userId = localStorage.getItem('userId')
    const pathname = window.location.pathname

    const openPaths = ['/login', '/signup']

    if (!userId && !openPaths.includes(pathname)) {
      navigate('/login')
    }
  }, [navigate])


  return (
    <Routes>
      <Route path="/" element={<Navigate to="/home" />} />
      <Route
        path="/home"
        element={
          localStorage.getItem('userId')
            ? <Home />
            : <Navigate to="/login" />
        }
      />
      <Route path="/login" element={<LoginPage />} />
      <Route path="/signup" element={<SignupPage />} />
      <Route path="/edit/:id" element={<EditArticlePage />} />
      <Route path="/mypage" element={<PrivateRoute><Mypage /></PrivateRoute>} />
      <Route path="/mypage/edit" element={<PrivateRoute><EditProfilePage /></PrivateRoute>} />
      <Route path="/write" element={<PrivateRoute><WritePage /></PrivateRoute>} />
      <Route path="/content/:id" element={<PrivateRoute><ContentDetailPage /></PrivateRoute>} />
    </Routes>
  )
}

export default function App() {
  return (
    <Router>
      <MainRouter />
    </Router>
  )
}
