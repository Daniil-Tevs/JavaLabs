package Model;

import java.util.*;

public class ScheduleCinema {
    private final Map<String, Map<String, List<Object[]>>> schedule;

    public ScheduleCinema(){
        schedule = new HashMap<>();
    }

    public void addScheduleItem(String date, String time, Session session, boolean isFull, List<int[]> usedPlaces){
        Object[] scheduleParam = {session, isFull, usedPlaces};
        if(schedule.containsKey(date)){
            if(!schedule.get(date).containsKey(time)) {
                schedule.get(date).put(time, new ArrayList<>());
                schedule.get(date).get(time).add(scheduleParam);
            }
            else {
                schedule.get(date).get(time).add(scheduleParam);
            }
        }
        else{
            Map<String, List<Object[]>> timeList = new HashMap<>();
            timeList.put(time,new ArrayList<>());
            timeList.get(time).add(scheduleParam);
            schedule.put(date,timeList);
        }
    }

    public boolean removeScheduleItem(String date, String time, Session session){
        List<Object[]> needDateTime = schedule.get(date).get(time);
        if(needDateTime == null)
            return false;
        Object[] needDateTimeParam;
        for (Object[] paramDateTime:needDateTime) {
            Session sessionDateTime = (Session) paramDateTime[0];
            if(Objects.equals(session.getSessionId(), sessionDateTime.getSessionId())){
                schedule.get(date).get(time).remove(paramDateTime);
                for (String dateTmp:schedule.keySet()) {
                    for (String timeTmp:schedule.get(dateTmp).keySet()) {
                        if(schedule.get(dateTmp).get(timeTmp).isEmpty()){
                            schedule.get(dateTmp).remove(timeTmp);
                        }
                    }
                }
                for (String dateTmp:schedule.keySet()) {
                    if (schedule.get(dateTmp).isEmpty())
                        schedule.remove(dateTmp);
                }
                return true;
            }
        }

        return false;
    }

    public Map<String, Map<String, List<Object[]>>> getSchedule() {
        return schedule;
    }
}
