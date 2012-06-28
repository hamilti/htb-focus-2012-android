package org.alpha.focus2012.data;

public class Hour implements Comparable<Object> {

    public int hour, minute;

    public Hour(int hh, int mm) {
        this.hour = hh;
        this.minute = mm;
      }

      public boolean equals(Object o) {
        Hour t = (Hour) o;
        return hour == t.hour && minute == t.minute;
      }

      public int hashCode() {
        return 60 * hour + minute;
      }

	@Override
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
    

}
