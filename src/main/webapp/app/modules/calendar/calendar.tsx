import React, { Component } from 'react';
import { Calendar, globalizeLocalizer } from 'react-big-calendar';
import './main.scss';
import './style.scss';
// import 'bootstrap/dist/css/bootstrap.min.css'
import globalize from 'globalize';
import { connect } from 'react-redux';
import { getFacturesByDate, updateCalendarUser, getTempsTravail } from './calendar-reducer';

const localizer = globalizeLocalizer(globalize);
// import 'react-big-calendar/lib/sass/styles.scss'

export class MyCalendar extends React.Component {
  componentDidMount() {
    var date = new Date(),
      y = date.getFullYear(),
      m = date.getMonth();
    var firstDay = new Date(y, m, 1);
    var lastDay = new Date(y, m + 1, 0);
    this.props.getFacturesByDate(this.props.match.params.userId, firstDay, lastDay);
    this.props.getTempsTravail(this.props.match.params.userId, firstDay, lastDay);
    // this.props.updateCalendarUser(this.props.match.params.userId);
  }

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
                let y = date.getFullYear(),
                  m = date.getMonth();
                var firstDay = new Date(y, m, 1);
                var lastDay = new Date(y, m + 1, 0);
                this.props.getFacturesByDate(this.props.match.params.userId, firstDay, lastDay);
              }}
            />
          </div>
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ calendar }) => ({
  events: calendar.events,
  userId: calendar.userId
});

const mapDispatchToProps = {
  getFacturesByDate,
  getTempsTravail,
  updateCalendarUser
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(MyCalendar);
// export default connect(mapStateToProps, )(MyCalendar);
