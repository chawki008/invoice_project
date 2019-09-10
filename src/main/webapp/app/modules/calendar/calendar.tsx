import React, { Component } from 'react';
import { Calendar, globalizeLocalizer } from 'react-big-calendar';
import './main.scss';
import './style.scss';
// import 'bootstrap/dist/css/bootstrap.min.css'
import globalize from 'globalize';
import { connect } from 'react-redux';
import { getFacturesByDate, updateCalendarUser, getTempsTravail } from './calendar-reducer';
import { getUser } from '../administration/user-management/user-management.reducer';

const localizer = globalizeLocalizer(globalize);
// import 'react-big-calendar/lib/sass/styles.scss'

export class MyCalendar extends React.Component {
  constructor(props) {
    super(props);
    this.state = {};
    const date = new Date(),
      y = date.getFullYear(),
      m = date.getMonth();
    const firstDay = new Date(y, m, 1);
    const lastDay = new Date(y, m + 1, 0);
    const userLogin = this.props.match.params.userLogin;
    this.props.getUser(userLogin);
  }
  componentDidMount() {}
  static getDerivedStateFromProps = (props, state) => {
    const date = new Date(),
      y = date.getFullYear(),
      m = date.getMonth();
    const firstDay = new Date(y, m, 1);
    const lastDay = new Date(y, m + 1, 0);
    if (state && !state.isUserLoaded && props.user.id !== '') {
      props.updateCalendarUser(props.user);
      props.getFacturesByDate(props.user, firstDay, lastDay);
      props.getTempsTravail(props.user.id, firstDay, lastDay);
      return { isUserLoaded: true };
    }
    return {};
  };

  render() {
    //   const { factureList, match, totalItems } = this.props;
    return (
      <div className="app">
        <div className="examples">
          <div className="example">
            <Calendar
              localizer={localizer}
              events={this.props.events.facture.concat(this.props.events.tempstravail)}
              startAccessor="start"
              endAccessor="end"
              onNavigate={(date, view, action) => {
                const y = date.getFullYear(),
                  m = date.getMonth();
                const firstDay = new Date(y, m, 1);
                const lastDay = new Date(y, m + 1, 0);
                this.props.getFacturesByDate(this.props.user, firstDay, lastDay);
              }}
            />
          </div>
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ calendar, userManagement }) => ({
  events: calendar.events,
  userId: calendar.userId,
  user: userManagement.user
});

const mapDispatchToProps = {
  getFacturesByDate,
  getTempsTravail,
  updateCalendarUser,
  getUser
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(MyCalendar);
// export default connect(mapStateToProps, )(MyCalendar);
