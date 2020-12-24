import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import SecuredRoute from './securityUtils/SecureRoute';
import { Provider } from 'react-redux';
import store from './store';
import Dashboard from './components/Dashboard';
import Header from './components/Layout/Header';
import Landing from './components/Layout/Landing';
import AddProject from './components/Project/AddProject';
import UpdateProject from './components/Project/UpdateProject';
import ProjectBoard from './components/ProjectBoard/ProjectBoard';
import AddProjectTask from './components/ProjectBoard/ProjectTasks/AddProjectTask';
import UpdateProjectTask from './components/ProjectBoard/ProjectTasks/UpdateProjectTask';
import Register from './components/UserManagement/Register';
import Login from './components/UserManagement/Login';
import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import jwt_decode from 'jwt-decode';
import setJWTToken from './securityUtils/setJWTToken';
import { SET_CURRENT_USER } from './actions/types';
import { logout } from './actions/securityActions';

const jwtToken = localStorage.jwtToken;

if (jwtToken) {
  setJWTToken(jwtToken);
  const decoded = jwt_decode(jwtToken);
  store.dispatch({
    type: SET_CURRENT_USER,
    payload: decoded,
  });

  const currentTime = Date.now() / 1000;
  if (decoded.exp < currentTime) {
    store.dispatch(logout());
    window.location.href = '/';
  }
}

const App = () => {
  return (
    <Provider store={store}>
      <Router>
        <div className="App">
          <Header />
          {
            // Public Routes
          }
          <Route exact path="/" component={Landing} />
          <Route exact path="/register" component={Register} />
          <Route exact path="/login" component={Login} />
          {
            // Private routes
          }
          <Switch>
            <SecuredRoute exact path="/dashboard" component={Dashboard} />
            <SecuredRoute exact path="/addProject" component={AddProject} />
            <SecuredRoute
              exact
              path="/updateProject/:projectIdentifier"
              component={UpdateProject}
            />
            <SecuredRoute
              exact
              path="/projectBoard/:projectIdentifier"
              component={ProjectBoard}
            />
            <SecuredRoute
              exact
              path="/addProjectTask/:projectIdentifier"
              component={AddProjectTask}
            />
            <SecuredRoute
              exact
              path="/updateProjectTask/:backlog_id/:pt_id"
              component={UpdateProjectTask}
            />
          </Switch>
        </div>
      </Router>
    </Provider>
  );
};

export default App;
